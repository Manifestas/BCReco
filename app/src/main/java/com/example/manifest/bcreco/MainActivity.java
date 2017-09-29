package com.example.manifest.bcreco;

import android.app.Activity;
import android.hardware.Camera;
import android.os.Bundle;
import android.os.Handler;
import android.widget.FrameLayout;
import android.widget.TextView;

import net.sourceforge.zbar.Config;
import net.sourceforge.zbar.Image;
import net.sourceforge.zbar.ImageScanner;
import net.sourceforge.zbar.Symbol;
import net.sourceforge.zbar.SymbolSet;


public class MainActivity extends Activity {

    private ImageScanner scanner;
    private TextView barcodeText;
    private Camera camera;
    private CameraPreview cameraPreview;
    private String lastScannedCode;
    private boolean isBarcodeScanned = false;
    private Image codeImage;
    private FrameLayout framePreview;
    private Handler autoFocusHandler;
    private boolean previewing = true;

    static {
        System.loadLibrary("iconv");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        scanner = new ImageScanner();
        // dont know why this numbers
        scanner.setConfig(0, Config.X_DENSITY, 3);
        scanner.setConfig(0, Config.Y_DENSITY, 3);

        barcodeText = (TextView) findViewById(R.id.barcode_text);
        framePreview = (FrameLayout) findViewById(R.id.camera_preview);
        autoFocusHandler = new Handler();

    }

    @Override
    protected void onResume() {
        super.onResume();
        resumeCamera();
    }

    @Override
    protected void onPause() {
        super.onPause();
        releaseCamera();
    }

    private void resumeCamera() {
        camera = getCameraInstance();
        cameraPreview = new CameraPreview(this, camera, previewCallback, autoFocusCallback);
        framePreview.removeAllViews();
        framePreview.addView(cameraPreview);
        if (camera != null) {
            Camera.Parameters parameters = camera.getParameters();
            Camera.Size size = parameters.getPreviewSize();
            codeImage = new Image(size.width, size.height, "Y800");
            previewing = true;
            cameraPreview.refreshDrawableState();
        }
    }
    /**
     * A safe way to get an instance of the Camera object.
     */
    public static Camera getCameraInstance() {
        Camera c = null;
        try {
            c = Camera.open();
        } catch (Exception e) {
            //
        }
        return c;
    }

    Camera.PreviewCallback previewCallback = new Camera.PreviewCallback() {
        @Override
        public void onPreviewFrame(byte[] bytes, Camera camera) {
            codeImage.setData(bytes);
            int result = scanner.scanImage(codeImage);
            if (result != 0) {
                SymbolSet symbols = scanner.getResults();
                for (Symbol sym : symbols) {
                    lastScannedCode = sym.getData();
                    if (lastScannedCode != null) {
                        barcodeText.setText(lastScannedCode);
                        isBarcodeScanned = true;
                    }
                }
            }
            camera.addCallbackBuffer(bytes);
        }
        };

    // Mimic continuous auto-focusing
    final Camera.AutoFocusCallback autoFocusCallback = new Camera.AutoFocusCallback() {
        public void onAutoFocus(boolean success, Camera camera) {
            autoFocusHandler.postDelayed(doAutoFocus, 1000);
        }
    };

    private Runnable doAutoFocus = new Runnable() {
        @Override
        public void run() {
            if (previewing && camera != null) {
                camera.autoFocus(autoFocusCallback);
            }
        }
    };

    private void releaseCamera() {
        if (camera != null) {
            previewing = false;
            camera.cancelAutoFocus();
            camera.setPreviewCallback(null);
            camera.stopPreview();
            camera.release();
            camera = null;
        }
    }

}
