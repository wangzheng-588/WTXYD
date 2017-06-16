package com.wz.wtxyd.ui.fragment;

import android.support.v4.app.Fragment;

import com.wz.wtxyd.bean.FragmentInfo;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by wz on 17-6-3.
 */

public class FragmentFactory {
    public static final String BOOK_SELF_FRAGMENT = "BookSelfFragment";
    public static final String BOOK_LIBRARY_FRAGMENT = "BookLibraryFragment";
    //
//    private static Map<Integer, Fragment> mFragments = new HashMap<>();
//
//
//    public static Fragment createFragment(int position) {
//        Fragment fragment = null;
//        fragment = mFragments.get(position);  //在集合中取出来Fragment
//        if (fragment == null)   //如果在集合中没有取出来，需要重新创建
//        {
//            if (position == 0) {
//                fragment = new BookSelfFragment();
//                FragmentInfo fragmentInfo = new FragmentInfo(fragment,"BookSelfFragment");
//
//            } else if (position == 1) {
//                fragment = new BookLibraryFragment();
//            }
//            if (fragment != null) {
//                mFragments.put(position, fragment);
//            }
//        }
//        return fragment;
//
//    }
//
//


    private static Map<Integer, FragmentInfo> mFragments = new HashMap<>();


    public static FragmentInfo createFragment(int position) {
        FragmentInfo fragmentInfo = null;
        Fragment fragment = null;
        fragmentInfo = mFragments.get(position);  //在集合中取出来Fragment
        if (fragmentInfo == null)   //如果在集合中没有取出来，需要重新创建
        {
            if (position == 0) {
                fragment = new BookSelfFragment();
                fragmentInfo = new FragmentInfo(fragment, BOOK_SELF_FRAGMENT);

            } else if (position == 1) {
                fragment = new BookLibraryFragment();
                fragmentInfo = new FragmentInfo(fragment, BOOK_LIBRARY_FRAGMENT);
            }
            if (fragment != null) {
                mFragments.put(position, fragmentInfo);
            }
        }
        return fragmentInfo;

    }
}
