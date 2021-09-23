package com.example.bluechatapp.Static.Utils;

import android.content.Context;
import android.widget.Toast;

import androidx.annotation.StringRes;

public class ToastUtil {
    private static Toast mToast ;
    public final static int state_on = 1 ;
    public final static int state_close = -1 ;
    private static int state = state_on ;
    // 封装Toast，提高Toast的时间灵活性，避免等待
    // 短时间显示
    public static void setState(int statement){
        state = statement ;
    }

    public static int getState() {
        return state;
    }
    public static void changeState(){
        state = (state==state_on)?state_close:state_on ;
    }
    public static void showShortMessage(Context context, String message){
        if (state==state_on){
            if (mToast==null)
                mToast = Toast.makeText(context,message,Toast.LENGTH_SHORT) ;
            else
                mToast.setText(message);
            mToast.show();
        }
    }
    public static void showShortMessage(Context context, @StringRes int message){
        if (state==state_on){
            if (mToast==null)
                mToast = Toast.makeText(context,message,Toast.LENGTH_SHORT) ;
            else
                mToast.setText(message);
            mToast.show();
        }
    }
    // 长时间显示
    public static void showLongMessage(Context context,String message){
        if (state==state_on){
            if (mToast==null)
                mToast = Toast.makeText(context,message,Toast.LENGTH_LONG) ;
            else
                mToast.setText(message);
            mToast.show();
        }
    }
}
