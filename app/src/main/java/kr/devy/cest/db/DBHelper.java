package kr.devy.cest.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import kr.devy.cest.support.Util;

/**
 * Created by pc on 2015-08-01.
 */
public class DBHelper extends SQLiteOpenHelper {

    private static DBHelper instance;

    private SQLiteDatabase sqlDB;
    private static final String DB_NAME = "cest";
    private static final int DB_VERSION = 9;

    public static synchronized DBHelper getInstance(Context context) {
        if (instance == null) {
            instance = new DBHelper(context);
        }
        return instance;
    }

    private DBHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
        sqlDB = getWritableDatabase();
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE cest(" +
                        "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                        "uuid TEXT," +
                        "c_class_id INT," +
                        "attendance_time DATETIME," +
                        "valid_time DATETIME," +
                        "receive_time DATETIME,"+
                        "done INT DEFAULT 0," +
                        "name TEXT)"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL("DROP TABLE IF EXISTS cest");
        onCreate(db);
    }

    public long addToken(Token token){
        ContentValues values = new ContentValues();
        values.put("uuid",token.getUuid());
        values.put("c_class_id",token.getC_class_id());
        values.put("attendance_time",token.getAttendacne_time());
        values.put("valid_time",token.getValid_time());
        values.put("receive_time",token.getReceive_time());
        values.put("name",token.getName());
        values.put("done",0);
        return sqlDB.insert("cest",null,values);
    }

    public void selectTokens(int c_class_id,ArrayList<Token> tokens) {
        Cursor c;
        if(tokens==null){
            tokens = new ArrayList<Token>();
        }else{
            tokens.clear();
        }

        Calendar cal = Calendar.getInstance();
        Date date = new Date(System.currentTimeMillis());
        String current_time = Util.getCurrentDateTime(System.currentTimeMillis());
        current_time = cal.get(Calendar.YEAR)+"-"+(cal.get(Calendar.MONTH)+1)+"-"+cal.get(Calendar.DATE) +
                " "+cal.get(Calendar.HOUR_OF_DAY)+":"+cal.get(Calendar.MINUTE)+":"+cal.get(Calendar.SECOND);
        c = sqlDB.rawQuery("select * from cest where '" + current_time + "' < valid_time  and done = 0 and c_class_id = " + c_class_id, null);
        if (c.moveToFirst()) {
            do {
                Token token = new Token();
                token.setId(c.getInt(0));
                token.setUuid(c.getString(1));
                token.setC_class_id(c.getInt(2));
                token.setAttendacne_time(c.getString(3));
                token.setValid_time(c.getString(4));
                token.setReceive_time(c.getString(5));
                token.setDone(c.getInt(6));
                token.setName(c.getString(7));
                tokens.add(token);

                Log.d("cest", "current -  " + current_time);
                Log.d("cest", "valid -  " + token.getValid_time());

            }while(c.moveToNext());
        }

    }

    public long useToken(String uuid){
        ContentValues values = new ContentValues();
        values.put("done",1);
        return sqlDB.update("cest",values,"uuid = '"+uuid+"'",null);
    }
}
