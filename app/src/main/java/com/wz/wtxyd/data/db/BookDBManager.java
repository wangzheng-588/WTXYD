package com.wz.wtxyd.data.db;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.wz.wtxyd.AppApplication;
import com.wz.wtxyd.bean.BookBean;
import com.wz.wtxyd.bean.ChpaterBean;
import com.wz.wtxyd.common.util.MD5Util;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wz on 17-6-9.
 */

public class BookDBManager {

    public static final String TABLE_BOOK = "book";
    public static final String TABLE_CHAPTER = "chapter";


    DatabaseHelper dbhelper;
    public SQLiteDatabase sqlitedatabase;


    //打开数据库连接
    public void opendb() {
        dbhelper = new DatabaseHelper(AppApplication.getContext());
        sqlitedatabase = dbhelper.getWritableDatabase();
    }

    //关闭数据库连接
    public void closedb() {
        if (sqlitedatabase.isOpen()) {
            sqlitedatabase.close();
        }
    }

    public int getBookCount(String pathMD5) {
        opendb();
        String selection = "pathMD5=?";
        String[] args = new String[]{pathMD5};

        Cursor cursor = sqlitedatabase.query(TABLE_BOOK, null, selection, args, null, null, null);
        int count = cursor.getCount();
        cursor.close();
        closedb();
        return count;
    }

    //查询表
    public List<BookBean> getBookList() {
        opendb();
        Cursor cursor = sqlitedatabase.query(TABLE_BOOK, null, null, null, null, null, "accessTime");
        List<BookBean> books = new ArrayList<>();
        while (cursor != null && cursor.moveToNext()) {

            BookBean book = new BookBean();
            int bookid = cursor.getInt(cursor.getColumnIndexOrThrow("bookid"));
            book.setId(bookid);
            String name = cursor.getString(cursor.getColumnIndex("name"));
            book.setName(name);
            String author = cursor.getString(cursor.getColumnIndexOrThrow("author"));
            book.setAuthor(author);
            String size = cursor.getString(cursor.getColumnIndexOrThrow("size"));
            book.setSize(size);
            String updatetime = cursor.getString(cursor.getColumnIndexOrThrow("updatetime"));
            book.setUpdateTime(updatetime);
            String path = cursor.getString(cursor.getColumnIndexOrThrow("path"));
            book.setPath(path);
            String pathMD5Str = cursor.getString(cursor.getColumnIndexOrThrow("pathMD5"));
            book.setPathMD5(pathMD5Str);
            String encoding = cursor.getString(cursor.getColumnIndexOrThrow("encoding"));
            book.setEncoding(encoding);
            long accessTime = cursor.getLong(cursor.getColumnIndexOrThrow("accessTime"));
            book.setAccessTime(accessTime);
            books.add(book);

        }

        cursor.close();
        closedb();
        return books;
    }


    public BookBean getBook(String table_name, String pathMD5) {
        Cursor cursor = null;
        BookBean book = null;
        try {
            opendb();
            String selection = "pathMD5=?";
            String[] args = new String[]{pathMD5};

            cursor = sqlitedatabase.query(table_name, null, selection, args, null, null, null);

            while (cursor != null && cursor.moveToNext()) {
                book = new BookBean();

                int bookid = cursor.getInt(cursor.getColumnIndexOrThrow("bookid"));
                book.setId(bookid);
                String name = cursor.getString(cursor.getColumnIndex("name"));
                book.setName(name);
                String author = cursor.getString(cursor.getColumnIndexOrThrow("author"));
                book.setAuthor(author);
                String size = cursor.getString(cursor.getColumnIndexOrThrow("size"));
                book.setSize(size);
                String updatetime = cursor.getString(cursor.getColumnIndexOrThrow("updatetime"));
                book.setUpdateTime(updatetime);
                String path = cursor.getString(cursor.getColumnIndexOrThrow("path"));
                book.setPath(path);
                String pathMD5Str = cursor.getString(cursor.getColumnIndexOrThrow("pathMD5"));
                book.setPathMD5(pathMD5Str);
                String encoding = cursor.getString(cursor.getColumnIndexOrThrow("encoding"));
                book.setEncoding(encoding);
                long accessTime = cursor.getLong(cursor.getColumnIndexOrThrow("accessTime"));
                book.setAccessTime(accessTime);

            }

        } finally {
            if (cursor != null) {
                cursor.close();
            }
            closedb();
        }

        return book;
    }

    //插入表数据
    public void insertBook(String table_name, BookBean book) {
        List<BookBean> bookBeen = new ArrayList<>();
        bookBeen.add(book);
        insertBookList(bookBeen);
    }

    //插入表数据
    public boolean updateBookProgress(BookBean book) {
        boolean success;
        try {
            opendb();
            sqlitedatabase.beginTransaction();

            String whereClause = "pathMD5=?";
            String[] args = new String[]{MD5Util.MD5(book.getPath())};

            ContentValues values = new ContentValues();
            values.put("begin", book.getBegin());
            values.put("end", book.getEnd());

            sqlitedatabase.update(TABLE_BOOK,values,whereClause,args);
            sqlitedatabase.setTransactionSuccessful();
        } finally {
            sqlitedatabase.endTransaction();
            success = true;
            closedb();
        }


        return success;
    }


    //插入book数据
    public boolean insertBookList(List<BookBean> bookBeen) {
        Cursor cursor = null;
        boolean success;
        try {
            opendb();
            sqlitedatabase.beginTransaction();
            for (int i = 0; i < bookBeen.size(); i++) {

                String selection = "pathMD5=?";
                String[] args = new String[]{MD5Util.MD5(bookBeen.get(i).getPath())};

                cursor = sqlitedatabase.query(TABLE_BOOK, null, selection, args, null, null, null);
                int count = cursor.getCount();

                if (count >= 1) {
                    Log.e("TAG", "已经存在了");
                    continue;
                }

                BookBean book = bookBeen.get(i);
                ContentValues values = new ContentValues();
                values.put("bookid", book.getId());
                values.put("name", book.getName());
                values.put("pathMD5", book.getPathMD5());
                values.put("author", book.getAuthor());
                values.put("size", book.getSize());
                values.put("updatetime", book.getUpdateTime());
                values.put("path", book.getPath());
                values.put("encoding", book.getEncoding());
                values.put("accessTime", book.getAccessTime());
                sqlitedatabase.insert(TABLE_BOOK, null, values);
            }
            sqlitedatabase.setTransactionSuccessful();
        } finally {
            sqlitedatabase.endTransaction();
            success = true;
            closedb();
            if (cursor != null) {

                cursor.close();
            }
        }


        return success;
    }


    //插入目录数据
    public boolean insertChapterList(List<ChpaterBean> chpaterBeen, String path) {
        Cursor cursor = null;
        boolean success;
        try {
            opendb();
            sqlitedatabase.beginTransaction();
            for (int i = 0; i < chpaterBeen.size(); i++) {

//                pathMD5 varchar(20),chap_name varchar(20)" +
//                ",pos int,end int)"

                ChpaterBean chpaterBean = chpaterBeen.get(i);
                ContentValues values = new ContentValues();
                values.put("chapter_name", chpaterBean.getName());
                values.put("pathMD5", MD5Util.MD5(path));
                values.put("pos", chpaterBean.getBookCatalogueStartPos());
                sqlitedatabase.insert(TABLE_CHAPTER, null, values);
            }
            sqlitedatabase.setTransactionSuccessful();
        } finally {
            sqlitedatabase.endTransaction();
            success = true;
            closedb();
            if (cursor != null) {
                cursor.close();
            }
        }

        return success;
    }



    //删除一条book数据
    public int deleteBook(BookBean book) {
        opendb();
        int delete = 0;
        try {
            String whereClause = "pathMD5=?";
            String[] whereArgs = new String[]{MD5Util.MD5(book.getPath())};
            delete = sqlitedatabase.delete(TABLE_BOOK, whereClause, whereArgs);


        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            closedb();
        }
        return delete;
    }

    //删除book列表数据
    public int deleteBooks(List<BookBean> books) {
        opendb();
        int delete = 0;
        try {
            for (int i = 0; i < books.size(); i++) {
                String whereClause = "pathMD5=?";
                String[] whereArgs = new String[]{MD5Util.MD5(books.get(i).getPath())};
                delete = sqlitedatabase.delete(TABLE_BOOK, whereClause, whereArgs);
            }


            // sqlitedatabase.delete(TABLE_BOOK, null, null);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            closedb();
        }
        return delete;
    }


    //删除目录数据
    public void deleteChapter(String path) {
        opendb();

        try {

            String whereClause = "pathMD5=?";
            String[] whereArgs = new String[]{MD5Util.MD5(path)};
            sqlitedatabase.delete(TABLE_CHAPTER, whereClause, whereArgs);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            closedb();
        }

    }


    //查询目录
    public List<ChpaterBean> getBookChapter(BookBean book) {
        opendb();
        String[] args = new String[]{MD5Util.MD5(book.getPath())};
        Cursor cursor = sqlitedatabase.query(TABLE_CHAPTER, null, "pathMD5=?", args, null, null, "pos");
        List<ChpaterBean> chapters = new ArrayList<>();
        while (cursor != null && cursor.moveToNext()) {

            ChpaterBean chapter = new ChpaterBean();

            String name = cursor.getString(cursor.getColumnIndex("chapter_name"));
            chapter.setName(name);

            int pos = cursor.getInt(cursor.getColumnIndexOrThrow("pos"));
            chapter.setBookCatalogueStartPos(pos);
            chapters.add(chapter);

        }

        cursor.close();
        closedb();
        return chapters;
    }



    //查询目录
    public BookBean getBookProgress(BookBean book) {
        opendb();
        String[] args = new String[]{MD5Util.MD5(book.getPath())};
        Cursor cursor = sqlitedatabase.query(TABLE_BOOK, null, "pathMD5=?", args, null, null, null);
        if (cursor != null) {
            cursor.moveToNext();
            int begin = cursor.getInt(cursor.getColumnIndex("begin"));
            book.setBegin(begin);

            int end = cursor.getInt(cursor.getColumnIndexOrThrow("end"));
            book.setEnd(end);
        }

        if (cursor!=null){

            cursor.close();
        }
        closedb();
        return book;
    }

}
