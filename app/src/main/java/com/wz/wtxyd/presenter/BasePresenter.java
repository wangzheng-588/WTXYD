package com.wz.wtxyd.presenter;

import android.content.Context;
import android.support.v4.app.Fragment;

/**
 * Created by wz on 17-6-2.
 */

public class BasePresenter<M,V> {

    protected M mModel;
    protected V mView;
    protected Context mContext;

    public BasePresenter(M model, V view) {
        mModel = model;
        mView = view;
        initContext();
    }

    private void initContext(){
        if (mView instanceof Fragment){
            mContext = ((Fragment)mView).getActivity();
        } else{
            mContext = (Context) mView;
        }
    }
}
