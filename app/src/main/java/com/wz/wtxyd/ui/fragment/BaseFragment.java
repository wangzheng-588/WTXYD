package com.wz.wtxyd.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.wz.wtxyd.AppApplication;
import com.wz.wtxyd.di.component.AppComponent;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by wz on 17-6-6.
 */

public abstract class BaseFragment extends Fragment{

    private Unbinder mBind;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(setLayoutResID(),null);
        mBind = ButterKnife.bind(this, view);
        AppComponent appComponent = ((AppApplication) (getActivity().getApplication())).getAppComponent();
        setupAppComponent(appComponent);
        return view;
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        init();
    }

    protected abstract void init();

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initData();
    }


    protected abstract int setLayoutResID();

    protected abstract void setupAppComponent(AppComponent appComponent);

    protected abstract void initData();


    @Override
    public void onDestroyView() {
        if (mBind!=mBind.EMPTY){
            mBind.unbind();
        }
        super.onDestroyView();
    }
}
