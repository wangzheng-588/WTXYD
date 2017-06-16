package com.wz.wtxyd.bean;

import android.support.v4.app.Fragment;

/**
 * Created by wz on 17-6-10.
 */

public class FragmentInfo {

    private Fragment mFragment;
    private String fragmentTag;

    public FragmentInfo(Fragment fragment, String fragmentTag) {
        mFragment = fragment;
        this.fragmentTag = fragmentTag;
    }

    public Fragment getFragment() {
        return mFragment;
    }

    public void setFragment(Fragment fragment) {
        mFragment = fragment;
    }

    public String getFragmentTag() {
        return fragmentTag;
    }

    public void setFragmentTag(String fragmentTag) {
        this.fragmentTag = fragmentTag;
    }
}
