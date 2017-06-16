package com.wz.wtxyd.common.util;

import com.wz.wtxyd.bean.BookBean;
import com.wz.wtxyd.bean.FileEntry;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by wz on 17-6-8.
 */

public class SearchBookUtils {

    private final List<BookBean> mBooks;
    private final List<FileEntry> mFileEntries;

    public SearchBookUtils() {
        mBooks = new ArrayList<>();
        mFileEntries = new ArrayList<>();
    }


    public List<BookBean> findBook(final String path, final String ext) {

        File directory = new File(path);
        if (directory.exists()) {
            String[] list = directory.list();
            //List<BookBean> books = new ArrayList<>();
            BookBean bookBean = null;
            for (int i = 0; i < list.length; i++) {
                File childFile = new File(directory, list[i]);
                if (childFile.isDirectory()) {
                    findBook(childFile.getPath(), ext);
//
                } else if (childFile.isFile()) {
                    if (childFile.getName().endsWith(ext)) {
                        bookBean = new BookBean();
                        bookBean.setPath(childFile.getPath());
                        bookBean.setPathMD5(MD5Util.MD5(childFile.getPath()));
                        bookBean.setName(childFile.getName());
                        bookBean.setSize(childFile.length() + "");
                        mBooks.add(bookBean);
                    }

                }
            }
            //Log.e("TAG",books.size()+"是");
            return mBooks;
        }
        return null;
    }


    public List<FileEntry> findFile(final String path, final String ext) {

        File directory = new File(path);
        if (directory.exists()) {
            String[] list = directory.list();
            //List<BookBean> books = new ArrayList<>();
            FileEntry fileEntry = null;
            for (int i = 0; i < list.length; i++) {
                File childFile = new File(directory, list[i]);
                if (childFile.isDirectory()) {
                    findFile(childFile.getPath(), ext);
//
                } else if (childFile.isFile()) {
                    if (childFile.getName().endsWith(ext)) {
                        fileEntry = new FileEntry();
                        fileEntry.setFile(true);
                        fileEntry.setPath(childFile.getPath());
                        mFileEntries.add(fileEntry);
                    }

                }
            }
            return mFileEntries;
        }
        return null;
    }

}
