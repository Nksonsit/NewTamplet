package com.drkeironbrown.lifecoach.db;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.drkeironbrown.lifecoach.model.Gallery;
import com.drkeironbrown.lifecoach.model.Image;
import com.drkeironbrown.lifecoach.model.Slideshow;
import com.drkeironbrown.lifecoach.ui.AddGalleryActivity;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class DBOpenHelper extends SQLiteOpenHelper {


    public static final String DB_NAME = "LifeCoach.db";
    public static final int DB_VERSION = 1;
    private static DBOpenHelper instance;
    private static final String DATABASE_PATH = "/data/data/com.drkeironbrown.lifecoach/databases/";
    public static final String DB_FULLPATH = DATABASE_PATH + DB_NAME;
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

    public static DBOpenHelper getInstance(Context context) {
        if (null == instance) {
            instance = new DBOpenHelper(context);
        }

        return instance;
    }

    public DBOpenHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);

    }

    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public void createDataBase(Context context) {

        boolean dbExist = checkDataBase();
        if (dbExist) {
        } else {
            this.getReadableDatabase();
            try {
                copyDataBase(context);
            } catch (IOException e) {
                e.printStackTrace();
                throw new Error("Error copying database");
            }
        }
    }

    private void copyDataBase(Context context) throws IOException {

        InputStream myInput = context.getAssets().open(DB_NAME);
        String outFileName = DATABASE_PATH + DB_NAME;
        OutputStream myOutput = new FileOutputStream(outFileName);
        byte[] buffer = new byte[1024];
        int length;

        while ((length = myInput.read(buffer)) > 0) {
            myOutput.write(buffer, 0, length);
        }

        myOutput.flush();
        myOutput.close();
        myInput.close();
    }

    private boolean checkDataBase() {

        File folder = new File(DATABASE_PATH);
        if (!folder.exists()) {
            folder.mkdir();
        }
        File dbFile = new File(DATABASE_PATH + DB_NAME);
        return dbFile.exists();
    }

    public static void addImages(Context context, Slideshow slideshow) {
        SQLiteDatabase sb = DatabaseManager.getInstance().openDatabase();
        String addSlideData = "INSERT INTO Slideshow ('SlideshowName','SlideshowDateTime') VALUES('" + slideshow.getSlideshowName() + "','" + slideshow.getSlideshowDateTime() + "')";
        sb.execSQL(addSlideData);

        Cursor cursor = sb.rawQuery("SELECT * FROM Slideshow", null);
        int slideShowId = 0;
        if (cursor != null && cursor.getCount() > 0) {
            cursor.moveToLast();
            slideShowId = cursor.getInt(0);
        }

        for (int i = 0; i < slideshow.getImages().size(); i++) {
            String addImages = "INSERT INTO Images ('SlideshowId','ImagePath') VALUES('" + slideShowId + "','" + slideshow.getImages().get(i).getImagePath() + "')";
            sb.execSQL(addImages);
        }

        DatabaseManager.getInstance().closeDatabase();
    }

    public static List<Slideshow> getSlideshowList() {
        List<Slideshow> list = new ArrayList<>();
        SQLiteDatabase sb = DatabaseManager.getInstance().openDatabase();
        Cursor cursor = sb.rawQuery("SELECT * FROM Slideshow", null);
        if (cursor != null && cursor.getCount() > 0) {
            cursor.moveToFirst();
            do {
                Slideshow slideshow = new Slideshow();
                List<Image> imageList = new ArrayList<>();
                slideshow.setSlideshowId(cursor.getInt(0));
                slideshow.setSlideshowName(cursor.getString(1));
                slideshow.setSlideshowDateTime(cursor.getString(2));
                Cursor imageCursor = sb.rawQuery("SELECT * FROM Images WHERE SlideshowId = " + cursor.getInt(0), null);
                if (imageCursor != null && imageCursor.getCount() > 0) {
                    imageCursor.moveToFirst();
                    do {
                        Image image = new Image();
                        image.setImageId(imageCursor.getInt(0));
                        image.setImagePath(imageCursor.getString(3));
                        imageList.add(image);
                    } while (imageCursor.moveToNext());
                }
                slideshow.setImages(imageList);
                slideshow.setTotalImage(imageList.size());
                list.add(slideshow);
            } while (cursor.moveToNext());
        }
        DatabaseManager.getInstance().closeDatabase();
        return list;
    }

    public static void deleteSlideshow(int slideshowId) {
        SQLiteDatabase sb = DatabaseManager.getInstance().openDatabase();

        List<Slideshow> slideshowList = getSlideshowList();
        for (int i = 0; i < slideshowList.size(); i++) {
            if (slideshowList.get(i).getSlideshowId() == slideshowId) {
                for (int j = 0; j < slideshowList.get(i).getImages().size(); j++) {
                    File file = new File(slideshowList.get(i).getImages().get(j).getImagePath());
                    Log.e("delete", file.getAbsolutePath());
                    if (file.exists()) {
                        file.delete();
                    }
                }
            }
        }
        sb.execSQL("DELETE FROM Slideshow WHERE SlideshowId = " + slideshowId);
        sb.execSQL("DELETE FROM Images WHERE SlideshowId = " + slideshowId);

        DatabaseManager.getInstance().closeDatabase();
    }

    public static void addImagesToGallery(Context context, Gallery gallery) {
        SQLiteDatabase sb = DatabaseManager.getInstance().openDatabase();
        String addSlideData = "INSERT INTO Gallery ('GalleryName','GalleryDateTime') VALUES('" + gallery.getGalleryName() + "','" + gallery.getGalleryDateTime() + "')";
        sb.execSQL(addSlideData);

        Cursor cursor = sb.rawQuery("SELECT * FROM Gallery", null);
        int galleryId = 0;
        if (cursor != null && cursor.getCount() > 0) {
            cursor.moveToLast();
            galleryId = cursor.getInt(0);
        }

        for (int i = 0; i < gallery.getImages().size(); i++) {
            String addImages = "INSERT INTO Images ('GalleryId','ImagePath') VALUES('" + galleryId + "','" + gallery.getImages().get(i).getImagePath() + "')";
            sb.execSQL(addImages);
        }

        DatabaseManager.getInstance().closeDatabase();
    }



    public static List<Gallery> getGalleryList() {
        List<Gallery> list = new ArrayList<>();
        SQLiteDatabase sb = DatabaseManager.getInstance().openDatabase();
        Cursor cursor = sb.rawQuery("SELECT * FROM Gallery", null);
        if (cursor != null && cursor.getCount() > 0) {
            cursor.moveToFirst();
            do {
                Gallery gallery = new Gallery();
                List<Image> imageList = new ArrayList<>();
                gallery.setGalleryId(cursor.getInt(0));
                gallery.setGalleryName(cursor.getString(1));
                gallery.setGalleryDateTime(cursor.getString(2));
                Cursor imageCursor = sb.rawQuery("SELECT * FROM Images WHERE GalleryId = " + cursor.getInt(0), null);
                if (imageCursor != null && imageCursor.getCount() > 0) {
                    imageCursor.moveToFirst();
                    do {
                        Image image = new Image();
                        image.setImageId(imageCursor.getInt(0));
                        image.setImagePath(imageCursor.getString(3));
                        imageList.add(image);
                    } while (imageCursor.moveToNext());
                }
                gallery.setImages(imageList);
                gallery.setTotalImage(imageList.size());
                list.add(gallery);
            } while (cursor.moveToNext());
        }
        DatabaseManager.getInstance().closeDatabase();
        return list;
    }

    public static void deleteGallery(int galleryId) {
        SQLiteDatabase sb = DatabaseManager.getInstance().openDatabase();

        List<Gallery> galleryList = getGalleryList();
        for (int i = 0; i < galleryList.size(); i++) {
            if (galleryList.get(i).getGalleryId() == galleryId) {
                for (int j = 0; j < galleryList.get(i).getImages().size(); j++) {
                    File file = new File(galleryList.get(i).getImages().get(j).getImagePath());
                    Log.e("delete", file.getAbsolutePath());
                    if (file.exists()) {
                        file.delete();
                    }
                }
            }
        }
        sb.execSQL("DELETE FROM Gallery WHERE GalleryId = " + galleryId);
        sb.execSQL("DELETE FROM Images WHERE GalleryId = " + galleryId);

        DatabaseManager.getInstance().closeDatabase();
    }
}
