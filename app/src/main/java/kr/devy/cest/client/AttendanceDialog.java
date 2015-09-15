package kr.devy.cest.client;

import android.app.Dialog;
import android.content.Context;

import java.util.ArrayList;

import kr.devy.cest.db.Token;

/**
 * Created by pc on 2015-08-01.
 */
public class AttendanceDialog extends Dialog {
    ArrayList<Token> tokens;

    public AttendanceDialog(Context context,ArrayList<Token> tokens) {
        super(context);
        this.tokens = tokens;


    }
}
