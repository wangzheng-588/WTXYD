package com.wz.wtxyd;

import android.app.Application;
import android.content.Context;

import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechUtility;
import com.wz.wtxyd.di.component.AppComponent;
import com.wz.wtxyd.di.component.DaggerAppComponent;
import com.wz.wtxyd.di.module.AppModule;
import com.wz.wtxyd.di.module.HttpModule;

/**
 * Created by wz on 17-6-6.
 */

public class AppApplication extends Application{

    private static Context mContext;

    public AppApplication getContext(Context context){
        return (AppApplication) context.getApplicationContext();
    }

    public static Context getContext(){
        return mContext;
    }

    public AppComponent mAppComponent;

    public AppComponent getAppComponent(){
        return mAppComponent;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        SpeechUtility.createUtility(this, SpeechConstant.APPID +"=5941fd86");
        mAppComponent = DaggerAppComponent.builder().appModule(new AppModule((AppApplication)getApplicationContext()))
                .httpModule(new HttpModule()).build();

        mContext = getApplicationContext();


    }



}
