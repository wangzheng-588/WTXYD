package com.wz.wtxyd.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by wz on 17-6-6.
 */

public class BookBean implements Serializable {



    private int id;
    private String author;
    private String name;
    private String newChapter;
    private String size;
    private int type;
    private String typeName;
    private String updateTime;
    private List<ChpaterBean> chapterList;
    private String path;
    private String encoding;
    private long accessTime = 0;
    private boolean isCheck;
    private String pathMD5;
    private long begin;
    private long end;
    private boolean isDeleteState;//是否进入删除编辑状态

    public long getEnd() {
        return end;
    }

    public void setEnd(long end) {
        this.end = end;
    }

    public long getBegin() {
        return begin;
    }

    public void setBegin(long begin) {
        this.begin = begin;
    }

    public boolean isDeleteState() {
        return isDeleteState;
    }

    public void setDeleteState(boolean deleteState) {
        isDeleteState = deleteState;
    }

    public String getPathMD5() {
        return pathMD5;
    }

    public void setPathMD5(String pathMD5) {
        this.pathMD5 = pathMD5;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getEncoding() {
        return encoding;
    }

    public void setEncoding(String encoding) {
        this.encoding = encoding;
    }

    public long getAccessTime() {
        return accessTime;
    }

    public void setAccessTime(long accessTime) {
        this.accessTime = accessTime;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNewChapter() {
        return newChapter;
    }

    public void setNewChapter(String newChapter) {
        this.newChapter = newChapter;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }

    public List<ChpaterBean> getChapterList() {
        return chapterList;
    }

    public void setChapterList(List<ChpaterBean> chapterList) {

        this.chapterList = chapterList;
    }

    public boolean isCheck() {
        return isCheck;
    }

    public void setCheck(boolean check) {
        isCheck = check;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        BookBean bookBean = (BookBean) o;

        if (id != bookBean.id) return false;
        if (type != bookBean.type) return false;
        if (accessTime != bookBean.accessTime) return false;
        if (isCheck != bookBean.isCheck) return false;
        if (author != null ? !author.equals(bookBean.author) : bookBean.author != null)
            return false;
        if (name != null ? !name.equals(bookBean.name) : bookBean.name != null) return false;
        if (newChapter != null ? !newChapter.equals(bookBean.newChapter) : bookBean.newChapter != null)
            return false;
        if (size != null ? !size.equals(bookBean.size) : bookBean.size != null) return false;
        if (typeName != null ? !typeName.equals(bookBean.typeName) : bookBean.typeName != null)
            return false;
        if (updateTime != null ? !updateTime.equals(bookBean.updateTime) : bookBean.updateTime != null)
            return false;
        if (chapterList != null ? !chapterList.equals(bookBean.chapterList) : bookBean.chapterList != null)
            return false;
        if (path != null ? !path.equals(bookBean.path) : bookBean.path != null) return false;
        return encoding != null ? encoding.equals(bookBean.encoding) : bookBean.encoding == null;

    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + (author != null ? author.hashCode() : 0);
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (newChapter != null ? newChapter.hashCode() : 0);
        result = 31 * result + (size != null ? size.hashCode() : 0);
        result = 31 * result + type;
        result = 31 * result + (typeName != null ? typeName.hashCode() : 0);
        result = 31 * result + (updateTime != null ? updateTime.hashCode() : 0);
        result = 31 * result + (chapterList != null ? chapterList.hashCode() : 0);
        result = 31 * result + (path != null ? path.hashCode() : 0);
        result = 31 * result + (encoding != null ? encoding.hashCode() : 0);
        result = 31 * result + (int) (accessTime ^ (accessTime >>> 32));
        result = 31 * result + (isCheck ? 1 : 0);
        return result;
    }
}
