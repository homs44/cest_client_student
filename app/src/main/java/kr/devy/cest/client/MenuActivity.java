package kr.devy.cest.client;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.os.Bundle;
import android.os.Environment;
import android.os.Message;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import kr.devy.cest.R;
import kr.devy.cest.bluetooth.BluetoothService;
import kr.devy.cest.db.DBHelper;
import kr.devy.cest.db.Token;
import kr.devy.cest.db.ClassInfo;
import kr.devy.cest.http.APIManager;
import kr.devy.cest.support.MHandler;
import kr.devy.cest.support.OnHandlerMessage;
import kr.devy.cest.support.Util;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;
import retrofit.mime.TypedFile;


public class MenuActivity extends Activity implements OnClickListener, OnHandlerMessage {

    TextView tv_name;
    TextView tv_room;
    TextView tv_time;
    CardView cv_check;
    CardView cv_search;

    ClassInfo info;
    ArrayList<Token> list;
    BluetoothService bs;
    MHandler handler = new MHandler(this);
    int c_user_id;
    String uuid;
    String attendance_time;

    public static final int REQUEST_CODE_CAMERA = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        SharedPreferences pref = getSharedPreferences(C.PREF_NAME, MODE_PRIVATE);
        c_user_id = pref.getInt(C.PREF_C_USER_ID, -1);
        info = getIntent().getExtras().getParcelable("classinfo");
        list = new ArrayList<Token>();
        tv_name = (TextView) findViewById(R.id.menu_tv_name);
        tv_room = (TextView) findViewById(R.id.menu_tv_room);
        tv_time = (TextView) findViewById(R.id.menu_tv_time);
        tv_name.setText(info.getName());
        tv_room.setText(info.getRoom());
        tv_time.setText(info.getTime());
        cv_check = (CardView) findViewById(R.id.menu_cv_check);
        cv_check.setOnClickListener(this);
        cv_search = (CardView) findViewById(R.id.menu_cv_search);
        cv_search.setOnClickListener(this);
        bs = BluetoothService.getInstance(getApplicationContext(), handler, info.getMac_addresses());
        Log.d("cest", info.toString());
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.menu_cv_check:
                //checkToken();
                checkActiveAttendance();
                break;
            case R.id.menu_cv_search:
                searchResult();
                break;
        }
    }

    private void searchResult() {
        Intent i = new Intent(this, AttendanceResultActivity.class);
        i.putExtra("c_user_id", c_user_id);
        i.putExtra("classinfo", info);
        startActivity(i);
    }


    private void checkToken() {
        DBHelper.getInstance(getApplicationContext()).selectTokens(info.getC_class_id(), list);
        if (list.size() == 0) {
            Toast.makeText(getApplicationContext(), "사용할 수 있는 출석토큰이 없습니다.", Toast.LENGTH_SHORT).show();
        } else {
            if (bs.isEnabled()) {
                bs.scanDevice(true);
            } else {
                // need to enable Bluetooth;
                Toast.makeText(getApplicationContext(), "블루투스를 활성화 하세요.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void checkActiveAttendance(){
        APIManager.getInstance().getAPI().getActiveAttendance(info.getC_class_id(),c_user_id, new Callback<JsonElement>() {
            @Override
            public void success(JsonElement jsonElement, Response response) {
                JsonObject json = jsonElement.getAsJsonObject();
                uuid = json.get("uuid").getAsString();
               // attendance_time = json.get("attendance_time").getAsString();
                Log.d(C.TAG, "checkActiveAttendance   uuid : " + uuid);
                int flag = json.get("flag").getAsInt();
                if (flag == 1) {
                    Toast.makeText(getApplicationContext(), "출석을 시작하지 않았습니다.", Toast.LENGTH_SHORT).show();
                } else if (flag == 2) {
                    if (bs.isEnabled()) {
                        bs.scanDevice(true);
                    } else {
                        Toast.makeText(getApplicationContext(), "블루투스를 활성화 하세요.", Toast.LENGTH_SHORT).show();
                    }
                } else if(flag ==3) {
                    Toast.makeText(getApplicationContext(), "이미 출석하였습니다.", Toast.LENGTH_SHORT).show();
                }


            }

            @Override
            public void failure(RetrofitError error) {

            }
        });



    }

    private void replyAttendance() {

        APIManager.getInstance().getAPI().replyAttendance(uuid, c_user_id,Util.getCurrentDateTime(System.currentTimeMillis()), new Callback<JsonElement>() {
            @Override
            public void success(JsonElement jsonElement, Response response) {
                JsonObject json = jsonElement.getAsJsonObject();
                int status = json.get("status").getAsInt();
                ///////String uuid = json.get("uuid").getAsString();

                if (status == 200) {
                    Toast.makeText(getApplicationContext(), "출석체크 하였습니다. \n결과를 확인하세요", Toast.LENGTH_SHORT).show();
                } else if (status == 500) {
                    String error = json.get("error").getAsJsonObject().toString();
                    Log.d("cest", "useToken - " + error);
                } else {
                    Log.d("cest", "useToken - what the fuck");
                }
            }

            @Override
            public void failure(RetrofitError error) {
                Log.d("cest", error.toString());
            }
        });

    }







/*
    private void useToken() {



        Log.d("cest", "list.size =  " + list.size());
        for (Token token : list) {
            Log.d("cest", " in  -> " + token.toString());
            APIManager.getInstance().getAPI().uesToken(token.getUuid(), c_user_id, token.getAttendacne_time(), token.getValid_time(), token.getReceive_time()
                    , Util.getCurrentDateTime(System.currentTimeMillis()), new Callback<JsonElement>() {

                @Override
                public void success(JsonElement jsonElement, Response response) {
                    // token 사용
                    JsonObject json = jsonElement.getAsJsonObject();
                    int status = json.get("status").getAsInt();
                    String uuid = json.get("uuid").getAsString();
                    Toast.makeText(getApplicationContext(), "출석체크 하였습니다. \n결과를 확인하세요", Toast.LENGTH_SHORT).show();
                    if (status == 200) {
                        DBHelper.getInstance(getApplicationContext()).useToken(uuid);
                    } else if (status == 500) {
                        String error = json.get("error").getAsJsonObject().toString();
                        Log.d("cest", "useToken - " + error);
                    } else {
                        Log.d("cest", "useToken - what the fuck");
                    }

                }

                @Override
                public void failure(RetrofitError error) {
                    Log.d("cest", error.toString());
                }
            });
        }


    }
*/

    @Override
    public void handleMessage(Message msg) {
        switch (msg.what) {
            case BluetoothService.FOUND_MAC_ADDRESS:
                replyAttendance();
                //startActivityForResult(new Intent(this,CameraActivity.class),REQUEST_CODE_CAMERA);
                Log.d("cest", "MenuActivity - handleMessage - FOUND_MAC_ADDRESSES : " + info.getMac_addresses());
                break;
            case BluetoothService.START_SCAN:
                Log.d("cest", "MenuActivity - handleMessage - START_SCAN");
                break;

            case BluetoothService.STOP_SCAN:
                Log.d("cest", "MenuActivity - handleMessage - STOP_SCAN");
                break;


        }
    }



    @Override
    protected void onActivityResult(int requestCode,int resultCode,Intent data){
        if(requestCode==REQUEST_CODE_CAMERA){
            if(resultCode ==RESULT_OK){
                    byte[] temp = data.getByteArrayExtra("data");
                    if(temp!=null){
                        processAttendance(temp);
                    }else{
                        Log.d(C.TAG,"data is null in onActivityResult in MenuActivity");
                    }
            }else{
            }

        }
    }


    private void processAttendance(byte[] data){
        Log.d(C.TAG,"in processAttendance");
        File file = getOutputMediaFile();
        if(file ==null){
            Log.d(C.TAG,"error in processAttendance in MenuActivity");
            return;
        }

        try{
            FileOutputStream fos = new FileOutputStream(file);
            fos.write(data);
            fos.close();
            for (Token token : list) {
                APIManager.getInstance().getAPI().useToken1(new TypedFile("multipart/form-data", file),token.getUuid(), c_user_id, token.getAttendacne_time(), token.getValid_time(), token.getReceive_time()
                        , Util.getCurrentDateTime(System.currentTimeMillis()), new Callback<JsonElement>() {

                    @Override
                    public void success(JsonElement jsonElement, Response response) {
                        // token 사용
                        JsonObject json = jsonElement.getAsJsonObject();
                        int status = json.get("status").getAsInt();
                        String uuid = json.get("uuid").getAsString();
                        Toast.makeText(getApplicationContext(), "출석체크 하였습니다. \n결과를 확인하세요", Toast.LENGTH_SHORT).show();
                        if (status == 200) {
                            DBHelper.getInstance(getApplicationContext()).useToken(uuid);
                        } else if (status == 500) {
                            String error = json.get("error").getAsJsonObject().toString();
                            Log.d("cest", "useToken - " + error);
                        } else {
                            Log.d("cest", "useToken - what the fuck");
                        }

                    }

                    @Override
                    public void failure(RetrofitError error) {
                        Log.d("cest", error.toString());
                    }
                });
            }


        }catch(FileNotFoundException e){
            e.printStackTrace();
        }catch(IOException e){
            e.printStackTrace();
        }
    }



    private File getOutputMediaFile(){
        // SD카드가 마운트 되어있는지 먼저 확인해야합니다
        // Environment.getExternalStorageState() 로 마운트 상태 확인 가능합니다

        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES), "cest_attendance");
        // 굳이 이 경로로 하지 않아도 되지만 가장 안전한 경로이므로 추천함.

        // 없는 경로라면 따로 생성한다.
        if (! mediaStorageDir.exists()){
            if (! mediaStorageDir.mkdirs()){
                Log.d("MyCamera", "failed to create directory");
                return null;
            }
        }

        // 파일명을 적당히 생성. 여기선 시간으로 파일명 중복을 피한다.
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        File mediaFile;

        mediaFile = new File(mediaStorageDir.getPath() + File.separator + "IMG_"+ timeStamp + ".jpg");
        Log.i("MyCamera", "Saved at"+Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES));

        return mediaFile;
    }
}
