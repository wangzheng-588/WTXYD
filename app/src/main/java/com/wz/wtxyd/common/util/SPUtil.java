package com.wz.wtxyd.common.util;

import android.content.Context;
import android.content.SharedPreferences;

import com.wz.wtxyd.AppApplication;

/**
 * Created by wz on 17-4-18.
 * QQ:1136918218
 * 微信：wz1136918218
 * 作用：
 */

public class SPUtil {

    SharedPreferences config = AppApplication.getContext().getSharedPreferences("config", Context.MODE_PRIVATE);
    SharedPreferences bookCfg = AppApplication.getContext().getSharedPreferences("bookConfig", Context.MODE_PRIVATE);

    private static SPUtil instance;

    private SPUtil(){

    }

    public static SPUtil getInstance() {
        if(instance == null){
            synchronized(SPUtil.class){
                if(instance == null){
                    instance = new SPUtil();
                }
            }
        }
        return instance;
    }

    public  void putBoolean(String key,boolean value){
        config.edit().putBoolean(key,value).apply();
    }

    public  void putString(String key,String value){
        SharedPreferences.Editor edit = config.edit();
        edit.putString(key,value);
        edit.apply();
    }

    public  void putInt(String key,int value){
        config.edit().putInt(key,value).apply();
    }

    public  void putFloat(String key,float value){
        SharedPreferences.Editor edit = config.edit();
        edit.putFloat(key,value);
        edit.apply();
    }

    public  String getString(String key,String defVal){
        return config.getString(key, defVal);
    }

    public int getInt(String key,int defVal){
        return config.getInt(key,defVal);
    }

    public  boolean getBoolean(String key,boolean defVal){
        return config.getBoolean(key, defVal);
    }



    public float getFloat(String fontSizeKey, float dimension) {
        return config.getFloat(fontSizeKey,dimension);
    }
}
