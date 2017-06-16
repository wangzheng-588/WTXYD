package com.wz.wtxyd.common.util;

import com.wz.wtxyd.bean.ChpaterBean;

import java.util.Comparator;

/**
 * Created by wz on 17-6-8.
 */

public class ComparatorChapterSort implements Comparator {

    @Override
    public int compare(Object o1, Object o2) {
        ChpaterBean chpaterBean1=(ChpaterBean)o1;
        ChpaterBean chpaterBean2=(ChpaterBean)o2;



        return (int) (chpaterBean1.getCid()-chpaterBean2.getCid());
    }
}
