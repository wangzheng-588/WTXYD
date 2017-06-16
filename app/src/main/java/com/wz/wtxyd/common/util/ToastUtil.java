package com.wz.wtxyd.common.util;

import android.widget.Toast;

import com.wz.wtxyd.AppApplication;

/**
 * Created by wz on 17-4-26.
 */

public class ToastUtil {

    private static Toast mToast;

    public static void show(String text){
        if (mToast == null){
            mToast = Toast.makeText(AppApplication.getContext(),text,Toast.LENGTH_SHORT);
        } else {
            mToast.setText(text);
        }

        mToast.show();
    }

}
