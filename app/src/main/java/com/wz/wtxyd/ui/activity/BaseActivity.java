package com.wz.wtxyd.ui.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.wz.wtxyd.AppApplication;
import com.wz.wtxyd.di.component.AppComponent;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by wz on 17-6-6.
 */

public abstract class BaseActivity extends AppCompatActivity {

    private Unbinder mBind;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(setLayoutResID());
        mBind = ButterKnife.bind(this);

        AppComponent appComponent = ((AppApplication)getApplication()).getAppComponent();
        setupAppComponent(appComponent);
        init();
    }

    protected abstract void setupAppComponent(AppComponent appComponent);

    protected abstract int setLayoutResID() ;

    protected abstract void init();


    @Override
    protected void onDestroy() {
        if (mBind!=mBind.EMPTY){
            mBind.unbind();
        }
        super.onDestroy();
    }
}
