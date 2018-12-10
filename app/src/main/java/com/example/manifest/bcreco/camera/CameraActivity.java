package com.example.manifest.bcreco.camera;

import android.app.Activity;
import android.content.Intent;
import android.hardware.Camera;
import android.os.Bundle;
import android.os.Handler;
import android.widget.FrameLayout;

import com.example.manifest.bcreco.R;

import net.sourceforge.zbar.Config;
import net.sourceforge.zbar.Image;
import net.sourceforge.zbar.ImageScanner;
import net.sourceforge.zbar.Symbol;
import net.sourceforge.zbar.SymbolSet;

public class CameraActivity extends Activity {

    public static final String EXTRA_BCVALUE = "bcValue";
    private ImageScanner scanner;
    private Camera camera;
    private CameraPreview cameraPreview;
    private String lastScannedCode;
    private boolean isBarcodeScanned = false;
    private Image codeImage;
    private Handler autoFocusHandler;
    private FrameLayout cameraScanner;
    private boolean previewing = true;

    static {
        System.loadLibrary("iconv");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);
        scanner = new ImageScanner();
        // dont know why this numbers
        scanner.setConfig(0, Config.X_DENSITY, 3);
        scanner.setConfig(0, Config.Y_DENSITY, 3);

        cameraScanner = (FrameLayout) findViewById(R.id.camera_scanner);
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
        cameraScanner.removeAllViews();
        cameraScanner.addView(cameraPreview);
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

    // Send to the scanner every new frame from camera preview
    Camera.PreviewCallback previewCallback = new Camera.PreviewCallback() {
        @Override
        public void onPreviewFrame(byte[] bytes, Camera camera) {
            codeImage.setData(bytes);
            int result = scanner.scanImage(codeImage);
            if (result != 0) {
                SymbolSet symbols = scanner.getResults();
                for (Symbol sym : symbols) {
                    //get recognized code
                    lastScannedCode = sym.getData();
                    // TODO check checksum here
                    if (lastScannedCode != null) {
                        isBarcodeScanned = true;
                        // Return 1st found QR code value to the calling Activity.
                        Intent resultIntent = new Intent ();
                        resultIntent.putExtra(EXTRA_BCVALUE, lastScannedCode);
                        setResult(Activity.RESULT_OK, resultIntent);
                        finish();
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
