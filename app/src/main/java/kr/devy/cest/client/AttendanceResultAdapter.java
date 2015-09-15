package kr.devy.cest.client;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.util.Comparator;

import kr.devy.cest.R;
import kr.devy.cest.db.ClassInfo;

/**
 * Created by pc on 2015-07-22.
 */
public class AttendanceResultAdapter extends RecyclerView.Adapter<AttendanceResultAdapter.ViewHolder> {
    private JsonArray array;
    private Context context;

    public AttendanceResultAdapter(Context context, JsonArray array) {
        this.context = context;
        this.array = array;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ViewHolder vh = new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_attendance_result, parent, false), context);

        return vh;
    }

    @Override
    public int getItemCount() {
        return array.size();
    }


    public JsonObject getItem(int position) {
        return array.get(position).getAsJsonObject();
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        Log.d("gtd", "here --- " + position);
        JsonObject json = array.get(position).getAsJsonObject();
        holder.tv_time.setText(json.get("attendance_time").getAsString().replace("T"," ").replace(".000Z",""));
        if (json.get("result").getAsInt() == 1) {
            holder.tv_result.setText("출석" );
            holder.tv_result.setTextColor(Color.parseColor("#3F51B5" ));
        } else if (json.get("result").getAsInt() == 2) {
            holder.tv_result.setText("지각");
            holder.tv_result.setTextColor(Color.parseColor("#9E9E9E" ));
        } else {
            holder.tv_result.setText("결석");
            holder.tv_result.setTextColor(Color.parseColor("#E91E63" ));
        }


    }
/*
    private class TimeCompare implements Comparator<JsonObject> {
        @Override
        public int compare(JsonObject arg0, JsonObject arg1) {


            return arg0.get("attendance_time").getAsString() < arg1.get("attendance_time").getAsString() ? -1 : arg0.get("attendance_time").getAsString() > arg1.get("attendance_time").getAsString() ? 1 : 0;
        }

    }
*/
    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView tv_time;
        public TextView tv_result;
        private Context context;

        public ViewHolder(View view, Context context) {
            super(view);
            this.context = context;
            tv_time = (TextView) view.findViewById(R.id.item_attendance_result_tv_time);
            tv_result = (TextView) view.findViewById(R.id.item_attendance_result_tv_result);

        }


    }
}


