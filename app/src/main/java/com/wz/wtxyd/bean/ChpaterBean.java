package com.wz.wtxyd.bean;

import java.io.Serializable;

/**
 * Created by wz on 17-6-6.
 * 小说章节
 */

public class ChpaterBean implements Serializable{
    private int bookId;
    private long cid;
    private String name;
    private long bookCatalogueStartPos;


    public ChpaterBean() {
    }

    public ChpaterBean(int bookId, long cid, String name) {
        this.bookId = bookId;
        this.cid = cid;
        this.name = name;
    }



    public int getBookId() {
        return bookId;
    }

    public void setBookId(int bookId) {
        this.bookId = bookId;
    }

    public long getCid() {
        return cid;
    }

    public void setCid(long cid) {
        this.cid = cid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public long getBookCatalogueStartPos() {
        return bookCatalogueStartPos;
    }

    public void setBookCatalogueStartPos(long bookCatalogueStartPos) {
        this.bookCatalogueStartPos = bookCatalogueStartPos;
    }
}
