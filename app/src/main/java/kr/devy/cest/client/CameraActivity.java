package kr.devy.cest.client;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.hardware.Camera;
import android.os.Environment;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import kr.devy.cest.R;

public class CameraActivity extends Activity {

    Camera mCamera;
    CameraPreview mPreview;
    public static final int REQUEST_CODE_PIC = 1;
    FrameLayout preview;
    Button bt;


    private Camera.PictureCallback  mPicture = new Camera.PictureCallback() {
            @Override
            public void onPictureTaken(byte[] data, Camera camera) {
                Log.d(C.TAG, "data length =----------------------- - " + data.length);
                //Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
                Intent i = new Intent(CameraActivity.this, PictureActivity.class);
                i.putExtra("data", data);
                startActivityForResult(i, REQUEST_CODE_PIC);
            }
        };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(C.TAG, "CameraActivity - onCreate");
        setContentView(R.layout.activity_camera);
        preview = (FrameLayout) findViewById(R.id.camera_frame);
        mPreview = new CameraPreview(this);
        preview.addView(mPreview);

        Button bt = (Button) findViewById(R.id.camera_bt);
        bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mCamera != null) {
                    mCamera.takePicture(null, null, mPicture);
                }
            }
        });


    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d(C.TAG, "CameraActivity - onStart");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(C.TAG, "CameraActivity - onResume");
        onResumeProcess();

    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d(C.TAG, "CameraActivity - onPause");
        onPauseProcess();
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d(C.TAG, "CameraActivity - onStop");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(C.TAG, "CameraActivity - onDestroy");
    }


    private void onResumeProcess() {
        if (mCamera == null) {
            if (checkCameraHardware(getApplicationContext())) {
                mCamera = getCameraInstance();
            }
        }
        mPreview.setCamera(mCamera);

    }


    private void onPauseProcess() {
        try{
            mCamera.stopPreview();
            mCamera.release();
            mCamera = null;
            mPreview.freeCamera();
        }catch(Exception e ){
            e.printStackTrace();
        }
    }


    private boolean checkCameraHardware(Context context) {
        if (context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA)) {
            // this device has a camera
            return true;
        } else {
            // no camera on this device
            return false;
        }
    }

    public static Camera getCameraInstance() {
        Camera c = null;
        try {
            c = Camera.open(); // attempt to get a Camera instance

        } catch (Exception e) {
            // Camera is not available (in use or does not exist)
        }
        return c; // returns null if camera is unavailable
    }

    @Override
    public void onActivityResult(int request_code, int result_code, Intent data) {
        if (request_code == REQUEST_CODE_PIC) {
            if(result_code==RESULT_OK){
                setResult(RESULT_OK,data);
            }
        }
    }


}
