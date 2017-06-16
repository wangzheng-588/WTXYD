package com.wz.wtxyd.bean;

import java.util.List;

/**
 * Created by wz on 17-6-6.
 */

public class BookTypeBodyEntry {

    private int ret_code;
    private List<BookCat> typeList;

    public int getRet_code() {
        return ret_code;
    }

    public void setRet_code(int ret_code) {
        this.ret_code = ret_code;
    }

    public List<BookCat> getTypeList() {
        return typeList;
    }

    public void setTypeList(List<BookCat> typeList) {
        this.typeList = typeList;
    }
}
