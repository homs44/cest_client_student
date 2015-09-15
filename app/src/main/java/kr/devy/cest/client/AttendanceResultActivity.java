package kr.devy.cest.client;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.TextView;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import kr.devy.cest.R;
import kr.devy.cest.db.ClassInfo;
import kr.devy.cest.http.APIManager;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;


public class AttendanceResultActivity extends Activity {

    RecyclerView rv;
    TextView tv;
    AttendanceResultAdapter ara;
    JsonArray array;
    private LinearLayoutManager mLayoutManager;
    int c_user_id;
    ClassInfo info;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attendance_result);
        info = getIntent().getExtras().getParcelable("classinfo");
        c_user_id = getIntent().getIntExtra("c_user_id", -1);

        tv= (TextView)findViewById(R.id.attendance_result_tv_name);
        tv.setText(info.getName());
        rv = (RecyclerView) findViewById(R.id.attendance_result_rv);
        array  = new JsonArray();
        ara = new AttendanceResultAdapter(getApplicationContext(), array);
        mLayoutManager = new LinearLayoutManager(getApplicationContext());
        mLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);

        rv.setAdapter(ara);
        rv.setLayoutManager(mLayoutManager);
        rv.setItemAnimator(new DefaultItemAnimator());
        getResults();
    }

    private void getResults() {
        APIManager.getInstance().getAPI().getMyAttendanceResult(info.getC_class_id(), c_user_id, new Callback<JsonElement>() {
            @Override
            public void success(JsonElement jsonElement, Response response) {
                JsonObject json = jsonElement.getAsJsonObject();
                if (json.get("status").getAsInt() == 200) {
                    array.addAll(json.get("results").getAsJsonArray());
                    ara.notifyDataSetChanged();
                } else if(json.get("status").getAsInt()==500){
                   Log.d("cest","AttendanceResultActivity - "+json.get("error").getAsJsonObject().toString() );
                }
            }

            @Override
            public void failure(RetrofitError error) {

            }
        });

    }
}
