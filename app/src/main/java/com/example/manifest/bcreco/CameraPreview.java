package com.example.manifest.bcreco;

import android.content.Context;
import android.graphics.ImageFormat;
import android.hardware.Camera;
import android.hardware.Camera.AutoFocusCallback;
import android.hardware.Camera.Parameters;
import android.view.SurfaceView;
import android.view.SurfaceHolder;

import java.io.IOException;

/** A basic Camera preview class */
public class CameraPreview extends SurfaceView implements SurfaceHolder.Callback {

    private SurfaceHolder holder;
    private Camera camera;
    private Camera.PreviewCallback previewCallback;
    private AutoFocusCallback autoFocusCallback;

    public CameraPreview(Context context, Camera camera, Camera.PreviewCallback previewCallback, AutoFocusCallback autoFocusCallback) {
        super(context);
        this.camera = camera;
        this.previewCallback = previewCallback;
        this.autoFocusCallback = autoFocusCallback;
        // Install a SurfaceHolder.Callback so we get notified when the
        // underlying surface is created and destroyed.
        holder = getHolder();
        holder.addCallback(this);
        /*
         * Set camera to continuous focus if supported, otherwise use
         * software auto-focus. Only works for API level >=9.
         */
        Camera.Parameters parameters = camera.getParameters();
        if (parameters.getSupportedFocusModes().contains(Parameters.FOCUS_MODE_CONTINUOUS_PICTURE)) {
            parameters.setFocusMode(Parameters.FOCUS_MODE_CONTINUOUS_PICTURE);
        }
    }

    @Override
    public void surfaceCreated(SurfaceHolder surfaceHolder) {
        // Install a SurfaceHolder.Callback so we get notified when the
        // underlying surface is created and destroyed.
        try {
            if (camera != null) {
                camera.setPreviewDisplay(holder);
            }
        } catch (IOException e) {
                e.printStackTrace();
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) {
        // If your preview can change or rotate, take care of those events here.
        // Make sure to stop the preview before resizing or reformatting it
        if (holder.getSurface() == null) {
            // preview surface does not exist
            return;
        }

        // stop preview before making changes
        try {
            camera.stopPreview();
        } catch (Exception e) {
            // ignore: tried to stop a non-existent preview
        }
        try {
            Parameters parameters = camera.getParameters();
            Camera.Size previewSize = parameters.getPreviewSize();
            int imageFormat = parameters.getPreviewFormat();
            int bufferSize = previewSize.width * previewSize.height * ImageFormat.getBitsPerPixel(imageFormat) / 8;
            byte[] cameraBuffer = new byte[bufferSize];
            // Hard code camera surface rotation 90 degs to match Activity view in portrait
            camera.setDisplayOrientation(90);

            camera.setPreviewDisplay(holder);
            camera.setPreviewCallbackWithBuffer(null);
            camera.setPreviewCallbackWithBuffer(previewCallback);
            camera.addCallbackBuffer(cameraBuffer);
            camera.startPreview();
            camera.autoFocus(autoFocusCallback);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
        // empty. Take care of releasing the Camera preview in your activity.
    }
}
