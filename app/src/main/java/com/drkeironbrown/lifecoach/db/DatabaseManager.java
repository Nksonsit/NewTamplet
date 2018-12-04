package com.drkeironbrown.lifecoach.db;

import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Environment;

import java.util.concurrent.atomic.AtomicInteger;

public class DatabaseManager {

    private static AtomicInteger openCount = new AtomicInteger();
    private static DatabaseManager instance;
    private static DBOpenHelper openHelper;
    private static SQLiteDatabase database;

    public static synchronized DatabaseManager getInstance() {
        if (null == instance) {
            throw new IllegalStateException(DatabaseManager.class.getSimpleName()
                    + " is not initialized, call initialize(..) method first.");
        }
        return instance;
    }

    public static synchronized void initialize(DBOpenHelper helper) {
        if (null == instance) {
            instance = new DatabaseManager();
        }
        openHelper = helper;
    }

    public synchronized SQLiteDatabase openDatabase() {
        if (openCount.incrementAndGet() == 1) {
            String myPath = "";
            if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                myPath = openHelper.getWritableDatabase().getPath();

            } else {
                myPath = DBOpenHelper.DATABASE_PATH + DBOpenHelper.DB_NAME;
            }

            database = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READWRITE);
//            database = openHelper.getWritableDatabase();
        }
        return database;
    }

    public synchronized void closeDatabase() {
        if (openCount.decrementAndGet() == 0) {
            database.close();
        }
    }
}
