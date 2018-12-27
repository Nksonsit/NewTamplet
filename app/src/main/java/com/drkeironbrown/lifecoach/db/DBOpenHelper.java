package com.drkeironbrown.lifecoach.db;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Environment;
import android.util.Log;

import com.drkeironbrown.lifecoach.helper.Functions;
import com.drkeironbrown.lifecoach.model.Gallery;
import com.drkeironbrown.lifecoach.model.Image;
import com.drkeironbrown.lifecoach.model.Inspiration;
import com.drkeironbrown.lifecoach.model.Journal;
import com.drkeironbrown.lifecoach.model.PersonalInspiration;
import com.drkeironbrown.lifecoach.model.Slideshow;

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
    public static final String DATABASE_PATH = Environment.getDataDirectory() + "/data/com.drkeironbrown.lifecoach/databases/";
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
        db.execSQL("CREATE TABLE Demo(id,name)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    @Override
    public void onOpen(SQLiteDatabase db) {
        super.onOpen(db);
        db.disableWriteAheadLogging();
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

    public static void addImages(Slideshow slideshow) {
        SQLiteDatabase sb = DatabaseManager.getInstance().openDatabase();
        String addSlideData = "INSERT INTO Slideshow ('SlideshowName','SlideshowDateTime','NotiId','AudioPath') VALUES('" + slideshow.getSlideshowName() + "','" + slideshow.getSlideshowDateTime() + "'," + slideshow.getNotiId() + ",'" + slideshow.getAudioPath() + "')";
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
                slideshow.setNotiId(cursor.getInt(3));
                slideshow.setAudioPath(cursor.getString(4));
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

    public static void addImagesToGallery(Gallery gallery) {
        SQLiteDatabase sb = DatabaseManager.getInstance().openDatabase();
        String addSlideData = "INSERT INTO Gallery ('GalleryName','GalleryDateTime','NotiId') VALUES('" + gallery.getGalleryName() + "','" + gallery.getGalleryDateTime() + "'," + gallery.getNotiId() + ")";
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
                gallery.setNotiId(cursor.getInt(3));
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

    public static void updateSlideshow(Slideshow slideshow) {
        SQLiteDatabase sb = DatabaseManager.getInstance().openDatabase();
        sb.execSQL("UPDATE Slideshow SET SlideshowName = '" + slideshow.getSlideshowName() + "', SlideshowDateTime = '" + slideshow.getSlideshowDateTime() + "', AudioPath = '" + slideshow.getAudioPath() + "'");
        sb.execSQL("DELETE FROM Images WHERE SlideshowId = " + slideshow.getSlideshowId());
        List<Image> list = Functions.copyPasteAllImages(slideshow.getImages());
        for (int i = 0; i < slideshow.getImages().size(); i++) {
            File file = new File(slideshow.getImages().get(i).getImagePath());
            if (file.exists()) {
                file.delete();
            }
        }
        for (int i = 0; i < list.size(); i++) {
            String addImages = "INSERT INTO Images ('SlideshowId','ImagePath') VALUES('" + slideshow.getSlideshowId() + "','" + list.get(i).getImagePath() + "')";
            sb.execSQL(addImages);
        }
        DatabaseManager.getInstance().closeDatabase();
    }

    public static void updateGallery(Gallery gallery) {
        SQLiteDatabase sb = DatabaseManager.getInstance().openDatabase();
        sb.execSQL("UPDATE Gallery SET GalleryName = '" + gallery.getGalleryName() + "', GalleryDateTime = '" + gallery.getGalleryDateTime() + "'");
        sb.execSQL("DELETE FROM Images WHERE GalleryId = " + gallery.getGalleryId());
        List<Image> list = Functions.copyPasteAllImages(gallery.getImages());
        for (int i = 0; i < gallery.getImages().size(); i++) {
            File file = new File(gallery.getImages().get(i).getImagePath());
            if (file.exists()) {
                file.delete();
            }
        }
        for (int i = 0; i < list.size(); i++) {
            String addImages = "INSERT INTO Images ('GalleryId','ImagePath') VALUES('" + gallery.getGalleryId() + "','" + list.get(i).getImagePath() + "')";
            sb.execSQL(addImages);
        }
        DatabaseManager.getInstance().closeDatabase();
    }

    public static void addJournal(Journal journal) {
        SQLiteDatabase sb = DatabaseManager.getInstance().openDatabase();
        sb.execSQL("INSERT INTO Journal(JournalText) VALUES('" + journal.getJournal() + "')");
        DatabaseManager.getInstance().closeDatabase();
    }

    public static void updateJournal(Journal journal) {
        SQLiteDatabase sb = DatabaseManager.getInstance().openDatabase();
        sb.execSQL("UPDATE Journal SET JournalText = '" + journal.getJournal() + "' WHERE JournalId = " + journal.getJournalId());
        DatabaseManager.getInstance().closeDatabase();
    }

    public static void deleteJournal(int journalId) {
        SQLiteDatabase sb = DatabaseManager.getInstance().openDatabase();
        sb.execSQL("DELETE FROM Journal WHERE JournalId = " + journalId);
        DatabaseManager.getInstance().closeDatabase();
    }

    public static List<Journal> getJournal() {
        List<Journal> list = new ArrayList<>();
        SQLiteDatabase sb = DatabaseManager.getInstance().openDatabase();
        Cursor cursor = sb.rawQuery("SELECT * FROM Journal", null);
        if (cursor != null && cursor.getCount() > 0) {
            cursor.moveToFirst();
            do {
                Journal journal = new Journal();
                journal.setJournalId(cursor.getInt(0));
                journal.setJournal(cursor.getString(1));
                list.add(journal);
            } while (cursor.moveToNext());
        }
        DatabaseManager.getInstance().closeDatabase();
        return list;
    }

    public static void deletePInspirational(int pInspirationalId) {
        SQLiteDatabase sb = DatabaseManager.getInstance().openDatabase();
        sb.execSQL("DELETE FROM PersonalInspirational WHERE PersonalInspirationalId = " + pInspirationalId);
        DatabaseManager.getInstance().closeDatabase();
    }

    public static void updatePersonalInspirational(PersonalInspiration personalInspiration) {
        SQLiteDatabase sb = DatabaseManager.getInstance().openDatabase();
        sb.execSQL("UPDATE PersonalInspirational SET PersonalInspirational = '" + personalInspiration.getPInspirational() + "' WHERE PersonalInspirationalId = " + personalInspiration.getPInspirationalId());
        DatabaseManager.getInstance().closeDatabase();
    }

    public static void addPersonalInspirational(PersonalInspiration personalInspiration) {
        SQLiteDatabase sb = DatabaseManager.getInstance().openDatabase();
        sb.execSQL("INSERT INTO PersonalInspirational(PersonalInspirational,IsByUser) VALUES('" + personalInspiration.getPInspirational() + "',1)");
        DatabaseManager.getInstance().closeDatabase();
    }

    public static List<PersonalInspiration> getPersonalInspirational() {
        List<PersonalInspiration> list = new ArrayList<>();
        SQLiteDatabase sb = DatabaseManager.getInstance().openDatabase();
        Cursor cursor = sb.rawQuery("SELECT * FROM PersonalInspirational", null);
        if (cursor != null && cursor.getCount() > 0) {
            cursor.moveToFirst();
            do {
                PersonalInspiration personalInspiration = new PersonalInspiration();
                personalInspiration.setPInspirationalId(cursor.getInt(0));
                personalInspiration.setPInspirational(cursor.getString(1));
                personalInspiration.setIsByUser(cursor.getInt(2));
                list.add(personalInspiration);
            } while (cursor.moveToNext());
        }
        DatabaseManager.getInstance().closeDatabase();
        return list;
    }


    public static List<Inspiration> getInspirational() {
        List<Inspiration> list = new ArrayList<>();
        SQLiteDatabase sb = DatabaseManager.getInstance().openDatabase();
        Cursor cursor = sb.rawQuery("SELECT * FROM Inspirational", null);
        if (cursor != null && cursor.getCount() > 0) {
            cursor.moveToFirst();
            do {
                Inspiration inspiration = new Inspiration();
                inspiration.setInspirationalId(cursor.getInt(0));
                inspiration.setInspirational(cursor.getString(1));
                list.add(inspiration);
            } while (cursor.moveToNext());
        }
        DatabaseManager.getInstance().closeDatabase();

        return list;
    }

    public static List<Image> getImagesFromGallery(int galleryId) {
        List<Image> list = new ArrayList<>();
        SQLiteDatabase sb = DatabaseManager.getInstance().openDatabase();
        Cursor cursor = sb.rawQuery("SELECT * FROM Images WHERE GalleryId = " + galleryId, null);
        if (cursor != null && cursor.getCount() > 0) {
            cursor.moveToFirst();
            do {
                Image image = new Image();
                image.setImageId(cursor.getInt(0));
                image.setImagePath(cursor.getString(3));
                list.add(image);
            } while (cursor.moveToNext());
        }
        DatabaseManager.getInstance().closeDatabase();
        return list;
    }

    public static void removeImage(int imageId) {
        SQLiteDatabase sb = DatabaseManager.getInstance().openDatabase();
        sb.execSQL("DELETE FROM Images WHERE ImageId = " + imageId);
        DatabaseManager.getInstance().closeDatabase();
    }

    public static List<Image> getImagesFromSlideshow(int slideshowId) {
        List<Image> list = new ArrayList<>();
        SQLiteDatabase sb = DatabaseManager.getInstance().openDatabase();
        Cursor cursor = sb.rawQuery("SELECT * FROM Images WHERE SlideshowId = " + slideshowId, null);
        if (cursor != null && cursor.getCount() > 0) {
            cursor.moveToFirst();
            do {
                Image image = new Image();
                image.setImageId(cursor.getInt(0));
                image.setImagePath(cursor.getString(3));
                list.add(image);
            } while (cursor.moveToNext());
        }
        DatabaseManager.getInstance().closeDatabase();
        return list;
    }

    public static Slideshow getLastSlideShow() {
        Slideshow slideshow = new Slideshow();
        SQLiteDatabase sb = DatabaseManager.getInstance().openDatabase();
        Cursor cursor = sb.rawQuery("SELECT * FROM Slideshow", null);
        if (cursor != null && cursor.getCount() > 0) {
            cursor.moveToLast();
            slideshow.setSlideshowId(cursor.getInt(0));
            slideshow.setSlideshowName(cursor.getString(1));
            slideshow.setSlideshowDateTime(cursor.getString(2));
            slideshow.setNotiId(cursor.getInt(3));
        }
        DatabaseManager.getInstance().closeDatabase();
        return slideshow;
    }

    public static Gallery getLastGallery() {
        SQLiteDatabase sb = DatabaseManager.getInstance().openDatabase();
        Gallery gallery = new Gallery();
        Cursor cursor = sb.rawQuery("SELECT * FROM Gallery", null);
        if (cursor != null && cursor.getCount() > 0) {
            cursor.moveToLast();
            gallery.setGalleryId(cursor.getInt(0));
            gallery.setGalleryName(cursor.getString(1));
            gallery.setGalleryDateTime(cursor.getString(2));
            gallery.setNotiId(cursor.getInt(3));
        }
        DatabaseManager.getInstance().closeDatabase();
        return gallery;
    }

    public static Inspiration getRandomInspirational() {
        SQLiteDatabase sb = DatabaseManager.getInstance().openDatabase();
        Inspiration inspiration = new Inspiration();
        Cursor cursor = sb.rawQuery("SELECT * FROM Inspirational WHERE InspirationalId IN (SELECT InspirationalId FROM Inspirational ORDER BY RANDOM() LIMIT 1)", null);
        if (cursor != null && cursor.getCount() > 0) {
            cursor.moveToFirst();
            inspiration.setInspirationalId(cursor.getInt(0));
            inspiration.setInspirational(cursor.getString(1));
        }
        DatabaseManager.getInstance().closeDatabase();
        return inspiration;
    }

    public static PersonalInspiration getRandomPInspirational() {
        SQLiteDatabase sb = DatabaseManager.getInstance().openDatabase();
        PersonalInspiration inspiration = new PersonalInspiration();
        Cursor cursor = sb.rawQuery("SELECT * FROM PersonalInspirational WHERE PersonalInspirationalId IN (SELECT PersonalInspirationalId FROM PersonalInspirational ORDER BY RANDOM() LIMIT 1)", null);
        if (cursor != null && cursor.getCount() > 0) {
            cursor.moveToFirst();
            inspiration.setPInspirationalId(cursor.getInt(0));
            inspiration.setPInspirational(cursor.getString(1));
        }
        DatabaseManager.getInstance().closeDatabase();
        return inspiration;
    }

    public static int getPInspirationalCount() {
        SQLiteDatabase sb = DatabaseManager.getInstance().openDatabase();
        PersonalInspiration inspiration = new PersonalInspiration();
        Cursor cursor = sb.rawQuery("SELECT * FROM PersonalInspirational", null);
        if (cursor != null && cursor.getCount() > 0) {
            DatabaseManager.getInstance().closeDatabase();
            return cursor.getCount();
        } else {
            DatabaseManager.getInstance().closeDatabase();
            return 0;
        }
    }

    public static Slideshow getSlideshow(int id) {
        Slideshow slideshow = new Slideshow();
        SQLiteDatabase sb = DatabaseManager.getInstance().openDatabase();
        Cursor cursor = sb.rawQuery("SELECT * FROM Slideshow WHERE SlideshowId = " + id, null);
        if (cursor != null && cursor.getCount() > 0) {
            cursor.moveToFirst();
            slideshow.setSlideshowId(cursor.getInt(0));
            slideshow.setSlideshowName(cursor.getString(1));
            slideshow.setSlideshowDateTime(cursor.getString(2));
            slideshow.setNotiId(cursor.getInt(3));
            slideshow.setAudioPath(cursor.getString(4));
        }
        DatabaseManager.getInstance().closeDatabase();
        return slideshow;
    }
}
