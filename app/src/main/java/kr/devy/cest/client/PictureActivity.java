package kr.devy.cest.client;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import kr.devy.cest.R;

public class PictureActivity extends ActionBarActivity {

    ImageView iv;
    Button bt_check;
    Button bt_cancel;
    Bitmap bitmap;
    byte[] data ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_picture);
        data = getIntent().getByteArrayExtra("data");
        iv = (ImageView)findViewById(R.id.picture_img);
        bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
        bitmap = rotateBitmap(bitmap);

        iv.setImageBitmap(bitmap);
        bt_check = (Button)findViewById(R.id.picture_bt_check);
        bt_check.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent();
                i.putExtra("data",data);
                setResult(RESULT_OK,i);
                finish();
            }
        });
        bt_cancel = (Button)findViewById(R.id.picture_bt_cancel);
        bt_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    private Bitmap rotateBitmap(Bitmap bitmap){
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        Matrix matrix = new Matrix();
        matrix.postRotate(90);
        Bitmap rBitmap = Bitmap.createBitmap(bitmap,0,0,width,height,matrix,true);
        bitmap.recycle();

        return rBitmap;
    }

}
