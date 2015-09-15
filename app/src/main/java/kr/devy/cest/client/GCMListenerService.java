package kr.devy.cest.client;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.gcm.GcmListenerService;

import kr.devy.cest.R;
import kr.devy.cest.db.DBHelper;
import kr.devy.cest.db.Token;
import kr.devy.cest.support.Util;

public class GCMListenerService extends GcmListenerService {

    public static final int NOTFICATION_ID = 1000;
    @Override
    public void onMessageReceived(String from, Bundle data){
        String uuid  = data.getString("uuid");
        int c_class_id  = Integer.parseInt(data.getString("c_class_id"));
        String attendance_time  = data.getString("attendance_time");
        String valid_time  = data.getString("valid_time");
        String name = data.getString("name");


        Token token = new Token();
        token.setDone(0);
        token.setUuid(uuid);
        token.setAttendacne_time(attendance_time);
        token.setReceive_time(Util.getCurrentDateTime(System.currentTimeMillis()));
        token.setC_class_id(c_class_id);
        token.setValid_time(valid_time);
        token.setName(name);
        DBHelper.getInstance(getApplicationContext()).addToken(token);
        setNotification(token);

    }


    private void setNotification(Token token){
        Log.d("cest",token.toString());

        NotificationManager nm = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        PendingIntent pi = PendingIntent.getActivity(getApplicationContext(),0,new Intent(this,SignInActivity.class), PendingIntent.FLAG_UPDATE_CURRENT);

        Notification.Builder mBuilder = new Notification.Builder(this);
        mBuilder.setTicker("Attendance Token" );
        mBuilder.setSmallIcon(R.drawable.circle_green);
        mBuilder.setContentTitle("Token : " + token.getName());
        mBuilder.setContentText(token.getUuid());
        mBuilder.setAutoCancel(true);
        mBuilder.setContentIntent(pi);

        nm.notify(NOTFICATION_ID,mBuilder.build());
    }
}
