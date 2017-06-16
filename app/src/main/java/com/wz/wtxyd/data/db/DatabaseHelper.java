package com.wz.wtxyd.data.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by wz on 17-6-9.
 */

class DatabaseHelper extends SQLiteOpenHelper{

    private static final int VERSION = 1;


    public DatabaseHelper(Context context) {
        super(context, "book.db", null, VERSION);
    }



    @Override
    public void onCreate(SQLiteDatabase db) {
        System.out.println("create a database");

        db.execSQL("create table chapter(id integer primary key ,chapter_name varchar(20),pathMD5 varchar(20),pos int)");
        db.execSQL("create table book(id integer primary key ,bookid int,name varchar(20)" +
                ",author varchar(20),size varchar(20),updatetime varchar(20)" +
                ",path varchar(20),pathMD5 varchar(20),encoding varchar(20),accessTime int" +
                ",begin int,end int)");




    }

    @Override
    public void onUpgrade(SQLiteDatabase arg0, int arg1, int arg2) {

    }
}
