package kr.devy.cest.client;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import kr.devy.cest.R;
import kr.devy.cest.db.DBHelper;


public class IntroActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);


        new Handler(){
            @Override
            public void handleMessage(Message msg){
                DBHelper.getInstance(getApplicationContext());
                finish();
            }
        }.sendEmptyMessageDelayed(0,1000);


    }


}
