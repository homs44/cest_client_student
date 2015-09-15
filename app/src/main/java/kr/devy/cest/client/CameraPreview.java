package kr.devy.cest.client;

import android.content.Context;
import android.hardware.Camera;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.io.IOException;

import kr.devy.cest.client.C;

/**
 * Created by pc on 2015-08-29.
 */
public class CameraPreview extends SurfaceView implements SurfaceHolder.Callback {
    private SurfaceHolder mHolder;
    private Camera mCamera;
    Context context;

    public CameraPreview(Context context) {
        super(context);
        this.context = context;
        Log.d(C.TAG,"CameraPreview constructor");
        // Install a SurfaceHolder.Callback so we get notified when the
        // underlying surface is created and destroyed.
        mHolder = getHolder();
        mHolder.addCallback(this);
        // deprecated setting, but required on Android versions prior to 3.0
        mHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
    }

    public void setCamera(Camera mCamera){
        this.mCamera = mCamera;
    }

    public void freeCamera(){
        mCamera = null;
    }






    public void surfaceCreated(SurfaceHolder holder) {
        // The Surface has been created, now tell the camera where to draw the preview.
        Log.d(C.TAG,"CameraPreview surfaceCreated");
        try {
            mCamera.setPreviewDisplay(holder);
            mCamera.startPreview();
        } catch (IOException e) {
            Log.d(C.TAG, "Error setting camera preview: " + e.getMessage());
        }
    }



    public void surfaceDestroyed(SurfaceHolder holder) {
        // empty. Take care of releasing the Camera preview in your activity.
        Log.d(C.TAG,"CameraPreview surfaceDestroyed");
    }

    public void surfaceChanged(SurfaceHolder holder, int format, int w, int h) {
        // If your preview can change or rotate, take care of those events here.
        // Make sure to stop the preview before resizing or reformatting it.
        Log.d(C.TAG,"CameraPreview surfaceChanged");
        if (mHolder.getSurface() == null){
            // preview surface does not exist
            return;
        }

        // stop preview before making changes
        try {
            mCamera.stopPreview();
        } catch (Exception e){
            // ignore: tried to stop a non-existent preview
        }

        // set preview size and make any resize, rotate or
        // reformatting changes here

        // start preview with new settings
        try {

            // Camera.Parameters params= mCamera.getParameters();
            //Display display = ((WindowManager)context.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();

            mCamera.setDisplayOrientation(90);



            mCamera.setPreviewDisplay(mHolder);
            mCamera.startPreview();

        } catch (Exception e){
            Log.d(C.TAG, "Error starting camera preview: " + e.getMessage());
        }
    }
}