package co.il.sqlcore;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

import co.il.model.model.ChatObject;
import co.il.model.model.Gallery;
import co.il.model.model.MessageObject;
import co.il.model.model.MishnayotItem;
import co.il.model.model.PagesItem;
import co.il.model.model.User;
import co.il.model.model.VideoPart;

import static co.il.sqlcore.DBKeys.CHAT_TABLE;
import static co.il.sqlcore.DBKeys.DATABASE_NAME;
import static co.il.sqlcore.DBKeys.DATABASE_VERSION;
import static co.il.sqlcore.DBKeys.GEMARA_GALLERY_TABLE;
import static co.il.sqlcore.DBKeys.GEMARA_TABLE;
import static co.il.sqlcore.DBKeys.GEMARA_VIDEO_PARTS_TABLE;
import static co.il.sqlcore.DBKeys.IS_MINE;
import static co.il.sqlcore.DBKeys.KEY_AUDIO_URL;
import static co.il.sqlcore.DBKeys.KEY_CHAPTER_NUMBER;
import static co.il.sqlcore.DBKeys.KEY_CHAT_ID;
import static co.il.sqlcore.DBKeys.KEY_CHAT_TITLE;
import static co.il.sqlcore.DBKeys.KEY_CHAT_TYPE;
import static co.il.sqlcore.DBKeys.KEY_CREATED_AT;
import static co.il.sqlcore.DBKeys.KEY_DURATION;
import static co.il.sqlcore.DBKeys.KEY_FROM_USER;
import static co.il.sqlcore.DBKeys.KEY_GEMARA_GALLEY_ORDER;
import static co.il.sqlcore.DBKeys.KEY_GEMARA_ID;
import static co.il.sqlcore.DBKeys.KEY_ID;
import static co.il.sqlcore.DBKeys.KEY_IMAGE;
import static co.il.sqlcore.DBKeys.KEY_IMAGE_URL;
import static co.il.sqlcore.DBKeys.KEY_IS_GEMARA;
import static co.il.sqlcore.DBKeys.KEY_LESSON_ID;
import static co.il.sqlcore.DBKeys.KEY_UNREAD_MESSAGES;
import static co.il.sqlcore.DBKeys.KEY_LAST_MESSAGE;
import static co.il.sqlcore.DBKeys.KEY_LAST_MESSAGE_TIME;
import static co.il.sqlcore.DBKeys.KEY_MASECHET_NAME;
import static co.il.sqlcore.DBKeys.KEY_MASECHET_ORDER;
import static co.il.sqlcore.DBKeys.KEY_MEDIA_TYPE;
import static co.il.sqlcore.DBKeys.KEY_MESSAGE_ID;
import static co.il.sqlcore.DBKeys.KEY_MISHNA_GALLEY_ORDER;
import static co.il.sqlcore.DBKeys.KEY_MISHNA_ID;
import static co.il.sqlcore.DBKeys.KEY_PAGE_NUMBER;
import static co.il.sqlcore.DBKeys.KEY_PAGE_URL;
import static co.il.sqlcore.DBKeys.KEY_PART_TITLE;
import static co.il.sqlcore.DBKeys.KEY_PRESENT_ID;
import static co.il.sqlcore.DBKeys.KEY_READ;
import static co.il.sqlcore.DBKeys.KEY_SEDER_ORDER;
import static co.il.sqlcore.DBKeys.KEY_SENT_AT;
import static co.il.sqlcore.DBKeys.KEY_TIME_LINE;
import static co.il.sqlcore.DBKeys.KEY_TITLE;
import static co.il.sqlcore.DBKeys.KEY_TO_USER;
import static co.il.sqlcore.DBKeys.KEY_VIDEO_PART_TIME_LINE;
import static co.il.sqlcore.DBKeys.KEY_VIDEO_URL;
import static co.il.sqlcore.DBKeys.MESSAGE;
import static co.il.sqlcore.DBKeys.MESSAGE_TABLE;
import static co.il.sqlcore.DBKeys.MESSAGE_TYPE;
import static co.il.sqlcore.DBKeys.MISHNA_GALLERY_TABLE;
import static co.il.sqlcore.DBKeys.MISHNA_TABLE;
import static co.il.sqlcore.DBKeys.MISHNA_VIDEO_PARTS_TABLE;


public class DBHandler extends SQLiteOpenHelper {


    public DBHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);

    }





    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

    }





    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {

        if (newVersion > oldVersion){

            try {
                sqLiteDatabase.execSQL("ALTER TABLE " + CHAT_TABLE +
                        " ADD COLUMN " + KEY_LESSON_ID +" INTEGER ");
            } catch (SQLException e) {
                e.printStackTrace();
            }

            try {
                sqLiteDatabase.execSQL("ALTER TABLE " + CHAT_TABLE +
                        " ADD COLUMN " + KEY_IS_GEMARA +" INTEGER ");
            } catch (SQLException e) {
                e.printStackTrace();
            }


//            onCreate(sqLiteDatabase);
        }

    }





    public void createGemaraTable() {

        final SQLiteDatabase database = getWritableDatabase();
        String CREATE_GEMARA_TABLE = "CREATE TABLE if not exists " + "\"" + GEMARA_TABLE + "\"" + "("
                + KEY_ID + " INTEGER PRIMARY KEY,"
                + KEY_GEMARA_ID + " INTEGER,"
                + KEY_SEDER_ORDER + " INTEGER,"
                + KEY_MASECHET_ORDER + " INTEGER,"
                + KEY_MASECHET_NAME + " TEXT,"
                + KEY_CHAPTER_NUMBER + " INTEGER,"
                + KEY_PAGE_NUMBER + " INTEGER,"
                + KEY_VIDEO_URL + " TEXT,"
                + KEY_AUDIO_URL + " TEXT,"
                + KEY_PRESENT_ID + " INTEGER,"
                + KEY_DURATION + " DOUBLE,"
                + KEY_TIME_LINE + " LONG,"
                + KEY_MEDIA_TYPE + " TEXT,"
                + KEY_PAGE_URL + " TEXT " +
                ")";

        database.execSQL(CREATE_GEMARA_TABLE);
        database.close();


    }


    public void createGemaraVideoPartsTable() {

        final SQLiteDatabase database = getWritableDatabase();
        String CREATE_GEMARA_VIDEO_PARTS_TABLE = "CREATE TABLE if not exists " + "\"" + GEMARA_VIDEO_PARTS_TABLE + "\"" + "("
                + KEY_ID + " INTEGER PRIMARY KEY,"
                + KEY_VIDEO_PART_TIME_LINE + " TEXT,"
                + KEY_PART_TITLE + " TEXT,"
                + KEY_GEMARA_ID + " INTEGER " +
                ")";

        database.execSQL(CREATE_GEMARA_VIDEO_PARTS_TABLE);
        database.close();

    }


    public void createGemaraGalleryTable() {

        final SQLiteDatabase database = getWritableDatabase();
        String CREATE_GEMARA_VIDEO_PARTS_TABLE = "CREATE TABLE if not exists " + "\"" + GEMARA_GALLERY_TABLE + "\"" + "("
                + KEY_ID + " INTEGER PRIMARY KEY,"
                + KEY_IMAGE_URL + " TEXT,"
                + KEY_GEMARA_ID + " TEXT,"
                + KEY_GEMARA_GALLEY_ORDER + " INTEGER,"
                + KEY_TITLE + " TEXT " +
                ")";

        database.execSQL(CREATE_GEMARA_VIDEO_PARTS_TABLE);
        database.close();

    }


    public void createMishnaTable() {

        final SQLiteDatabase database = getWritableDatabase();
        String CREATE_MISHNA_TABLE = "CREATE TABLE if not exists " + "\"" + MISHNA_TABLE + "\"" + "("
                + KEY_ID + " INTEGER PRIMARY KEY,"
                + KEY_MISHNA_ID + " INTEGER,"
                + KEY_SEDER_ORDER + " INTEGER,"
                + KEY_MASECHET_ORDER + " INTEGER,"
                + KEY_MASECHET_NAME + " TEXT,"
                + KEY_CHAPTER_NUMBER + " INTEGER,"
                + KEY_PAGE_NUMBER + " INTEGER,"
                + KEY_VIDEO_URL + " TEXT,"
                + KEY_AUDIO_URL + " TEXT,"
                + KEY_PRESENT_ID + " INTEGER,"
                + KEY_DURATION + " DOUBLE,"
                + KEY_TIME_LINE + " DOUBLE,"
                + KEY_MEDIA_TYPE + " TEXT,"
                + KEY_PAGE_URL + " TEXT " +
                ")";

        database.execSQL(CREATE_MISHNA_TABLE);
        database.close();


    }


    public void createMishnaVideoPartsTable() {

        final SQLiteDatabase database = getWritableDatabase();
        String CREATE_GEMARA_VIDEO_PARTS_TABLE = "CREATE TABLE if not exists " + "\"" + MISHNA_VIDEO_PARTS_TABLE + "\"" + "("
                + KEY_ID + " INTEGER PRIMARY KEY,"
                + KEY_VIDEO_PART_TIME_LINE + " TEXT,"
                + KEY_PART_TITLE + " TEXT,"
                + KEY_MISHNA_ID + " INTEGER " +
                ")";

        database.execSQL(CREATE_GEMARA_VIDEO_PARTS_TABLE);
        database.close();

    }


    public void createMishnaGalleryTable() {

        final SQLiteDatabase database = getWritableDatabase();
        String CREATE_GEMARA_VIDEO_PARTS_TABLE = "CREATE TABLE if not exists " + "\"" + MISHNA_GALLERY_TABLE + "\"" + "("
                + KEY_ID + " INTEGER PRIMARY KEY,"
                + KEY_IMAGE_URL + " TEXT,"
                + KEY_MISHNA_ID + " TEXT,"
                + KEY_MISHNA_GALLEY_ORDER + " INTEGER,"
                + KEY_TITLE + " TEXT " +
                ")";

        database.execSQL(CREATE_GEMARA_VIDEO_PARTS_TABLE);
        database.close();

    }


    public boolean addTalmudToTable(PagesItem pagesItemDB, String tableType) {


        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(KEY_GEMARA_ID, pagesItemDB.getId());
        values.put(KEY_SEDER_ORDER, pagesItemDB.getSederOrder());
        values.put(KEY_MASECHET_ORDER, pagesItemDB.getMasechetOrder());
        values.put(KEY_MASECHET_NAME, pagesItemDB.getMasechetName());
        values.put(KEY_CHAPTER_NUMBER, pagesItemDB.getChapter());
        values.put(KEY_PAGE_NUMBER, pagesItemDB.getPageNumber());
        values.put(KEY_VIDEO_URL, pagesItemDB.getVideo());
        values.put(KEY_AUDIO_URL, pagesItemDB.getAudio());

        if (pagesItemDB.getPresenter() != null) {

            values.put(KEY_PRESENT_ID, pagesItemDB.getPresenter().getId());
        }

        values.put(KEY_DURATION, pagesItemDB.getDuration());
        values.put(KEY_TIME_LINE, pagesItemDB.getTimeLine());
        values.put(KEY_MEDIA_TYPE, pagesItemDB.getMediaType());
        values.put(KEY_PAGE_URL, pagesItemDB.getPage());

        db.insert(tableType, null, values);

        db.close();

        return true;


    }


    public void deletePageFromGemaraTable(PagesItem pagesItem) {

        SQLiteDatabase db = this.getWritableDatabase();

        db.delete(GEMARA_TABLE, KEY_GEMARA_ID + " = ?", new String[]{String.valueOf(pagesItem.getGemaraId())});

        db.close();
    }


    public void deletePageFromMishnaTable(MishnayotItem mishnayotItem) {

        SQLiteDatabase db = this.getWritableDatabase();

        db.delete(MISHNA_TABLE, KEY_MISHNA_ID + " = ?", new String[]{String.valueOf(mishnayotItem.getId())});

        db.close();
    }


    public boolean addGemaraVideoPartsToTable(VideoPart videoPart, int gemaraId) {

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(KEY_GEMARA_ID, gemaraId);
        values.put(KEY_VIDEO_PART_TIME_LINE, videoPart.getVideoPartTimeLine());
        values.put(KEY_PART_TITLE, videoPart.getPartTitle());

        db.insert(GEMARA_VIDEO_PARTS_TABLE, null, values);

        db.close();

        return true;


    }


    public boolean addGemaraGalleryToTable(Gallery gallery, int gemaraId) {

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(KEY_GEMARA_ID, gemaraId);
        values.put(KEY_IMAGE_URL, gallery.getImage());
        values.put(KEY_GEMARA_GALLEY_ORDER, gallery.getOrder());
        values.put(KEY_TITLE, gallery.getTitle());

        db.insert(GEMARA_GALLERY_TABLE, null, values);

        db.close();

        return true;
    }


    public boolean addMishnaVideoPartsToTable(VideoPart videoPart, int mishnaID) {

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(KEY_MISHNA_ID, mishnaID);
        values.put(KEY_VIDEO_PART_TIME_LINE, videoPart.getVideoPartTimeLine());
        values.put(KEY_PART_TITLE, videoPart.getPartTitle());

        db.insert(MISHNA_VIDEO_PARTS_TABLE, null, values);

        db.close();

        return true;


    }


    public boolean addMishnaGalleryToTable(Gallery gallery, int mishnaID) {

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(KEY_MISHNA_ID, mishnaID);
        values.put(KEY_IMAGE_URL, gallery.getImage());
        values.put(KEY_MISHNA_GALLEY_ORDER, gallery.getOrder());
        values.put(KEY_TITLE, gallery.getTitle());

        db.insert(MISHNA_VIDEO_PARTS_TABLE, null, values);

        db.close();

        return true;
    }


    public int updateGemaraAudioToTable(String gemaraId, String audioUrl, String tableName) {

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(KEY_AUDIO_URL, audioUrl);

        int i = db.update(tableName, values, KEY_GEMARA_ID + "=?",
                new String[]{String.valueOf(gemaraId)});
        db.close();

        return i;
    }


    public int updateMishnaAudioToTable(String gemaraId, String audioUrl, String tableName) {

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(KEY_AUDIO_URL, audioUrl);

        int i = db.update(tableName, values, KEY_MISHNA_ID + "=?",
                new String[]{String.valueOf(gemaraId)});
        db.close();

        return i;
    }


    public int updateVideoToTable(String gemaraId, String videoUrl, String tableName) {

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(KEY_VIDEO_URL, videoUrl);

        int i = db.update(tableName, values, KEY_GEMARA_ID + "=?",
                new String[]{String.valueOf(gemaraId)});
        db.close();

        return i;
    }


    public int updateVideoMishnaToTable(String gemaraId, String videoUrl, String tableName) {

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(KEY_VIDEO_URL, videoUrl);

        int i = db.update(tableName, values, KEY_MISHNA_ID + "=?",
                new String[]{String.valueOf(gemaraId)});
        db.close();

        return i;
    }


    public int updateTimeLineInTable(String gemaraId, long timeLine, String tableName) {

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(KEY_TIME_LINE, timeLine);

        if (tableName.equals(GEMARA_TABLE)) {

            int i = db.update(tableName, values, KEY_GEMARA_ID + "=?",
                    new String[]{String.valueOf(gemaraId)});
            db.close();

            return i;
        } else {
            int i = db.update(tableName, values, KEY_MISHNA_ID + "=?",
                    new String[]{String.valueOf(gemaraId)});
            db.close();

            return i;
        }


    }


    public List<PagesItem> getAllGemaraDownloadTalmud(String tableName) {

        try {
            List<PagesItem> pagesItemList = new ArrayList<>();

            String selectQuery = "SELECT * FROM " + '"' + tableName + '"' + " " +
                    "WHERE (" +
                    KEY_AUDIO_URL + " != '" + "" + "' OR " + KEY_VIDEO_URL + " != " + "''" + " )" + " ORDER BY " + KEY_SEDER_ORDER + ", " + KEY_MASECHET_ORDER;

            SQLiteDatabase db = this.getWritableDatabase();

            Cursor cursor = db.rawQuery(selectQuery, null);

            if (cursor.moveToFirst()) {
                do {

                    PagesItem dataBaseObject = new PagesItem();

                    setFieldsdataBaseObjectFromCurser(cursor, dataBaseObject);

                    pagesItemList.add(dataBaseObject);

                }
                while (cursor.moveToNext());
            }
            cursor.close();

            db.close();

            return pagesItemList;
        } catch (Exception e) {
            return new ArrayList<>();
        }
    }


    public List<PagesItem> getMasechetDownloads(String tableName, String masechetName) {

        try {
            List<PagesItem> pagesItemList = new ArrayList<>();


            String selectQuery =
                    "SELECT " +
                            "* " +
                            "FROM " + '"' +
                            tableName + '"' + " " +
                            "WHERE " +
                            KEY_MASECHET_NAME + " = '" + masechetName + "' AND (" +
                            KEY_AUDIO_URL + " != '" + "" + "' OR " + KEY_VIDEO_URL + " != " + "''" + " )";


            SQLiteDatabase db = this.getWritableDatabase();

            Cursor cursor = db.rawQuery(selectQuery, null);

            if (cursor.moveToFirst()) {
                do {

                    PagesItem dataBaseObject = new PagesItem();

                    setFieldsdataBaseObjectFromCurser(cursor, dataBaseObject);

                    pagesItemList.add(dataBaseObject);

                }
                while (cursor.moveToNext());
            }
            cursor.close();

            db.close();

            return pagesItemList;
        } catch (Exception e) {
            return new ArrayList<>();
        }
    }


    public List<MishnayotItem> getMasechetMishnaDownloads(String tableName, String masechetName) {

        try {
            List<MishnayotItem> pagesItemList = new ArrayList<>();


            String selectQuery =
                    "SELECT " +
                            "* " +
                            "FROM " + '"' +
                            tableName + '"' + " " +
                            "WHERE " +
                            KEY_MASECHET_NAME + " = '" + masechetName + "' AND (" +
                            KEY_AUDIO_URL + " != '" + "" + "' OR " + KEY_VIDEO_URL + " != " + "''" + " )";


            SQLiteDatabase db = this.getWritableDatabase();

            Cursor cursor = db.rawQuery(selectQuery, null);

            if (cursor.moveToFirst()) {
                do {

                    MishnayotItem dataBaseObject = new MishnayotItem();

                    setMishnaFieldsdataBaseObjectFromCurser(cursor, dataBaseObject);

                    pagesItemList.add(dataBaseObject);

                }
                while (cursor.moveToNext());
            }
            cursor.close();

            db.close();

            return pagesItemList;
        } catch (Exception e) {
            return new ArrayList<>();
        }
    }

    public List<VideoPart> getVideoPart(String tableName, String masechetId) {

        try {
            List<VideoPart> pagesVideoPart = new ArrayList<>();


            String selectQuery =
                    "SELECT " +
                            "* " +
                            "FROM " + '"' +
                            tableName + '"' + " " +
                            "WHERE " +
                            KEY_GEMARA_ID + " = '" + masechetId + "' ";


            SQLiteDatabase db = this.getWritableDatabase();

            Cursor cursor = db.rawQuery(selectQuery, null);

            if (cursor.moveToFirst()) {
                do {

                    VideoPart videoPart = new VideoPart();

                    setVideoPartObjectFromCursor(cursor, videoPart);

                    pagesVideoPart.add(videoPart);

                }
                while (cursor.moveToNext());
            }
            cursor.close();

            db.close();

            return pagesVideoPart;
        } catch (Exception e) {
            return new ArrayList<>();
        }
    }


    public List<VideoPart> getVideoPartForMishna(String tableName, String masechetId) {

        try {
            List<VideoPart> pagesVideoPart = new ArrayList<>();


            String selectQuery =
                    "SELECT " +
                            "* " +
                            "FROM " + '"' +
                            tableName + '"' + " " +
                            "WHERE " +
                            KEY_MISHNA_ID + " = '" + masechetId + "' ";


            SQLiteDatabase db = this.getWritableDatabase();

            Cursor cursor = db.rawQuery(selectQuery, null);

            if (cursor.moveToFirst()) {
                do {

                    VideoPart videoPart = new VideoPart();

                    setMishnaVideoPartObjectFromCursor(cursor, videoPart);

                    pagesVideoPart.add(videoPart);

                }
                while (cursor.moveToNext());
            }
            cursor.close();

            db.close();

            return pagesVideoPart;
        } catch (Exception e) {
            return new ArrayList<>();
        }
    }


    public PagesItem findGemaraById(String Id, String tableName) {

        PagesItem pagesItemDB = null;
        String selectQuery =
                "SELECT " +
                        "* " +
                        "FROM " + '"' +
                        tableName + '"' + " " +
                        "WHERE " +
                        KEY_GEMARA_ID + " = '" + Id + "' ";

        SQLiteDatabase db = this.getWritableDatabase();

        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {

            pagesItemDB = new PagesItem();

            setFieldsdataBaseObjectFromCurser(cursor, pagesItemDB);

        }

        cursor.close();

        db.close();

        return pagesItemDB;

    }


    private void setFieldsdataBaseObjectFromCurser(Cursor cursor, PagesItem pagesItemDB) {


        pagesItemDB.setId(cursor.getInt(cursor.getColumnIndex(KEY_GEMARA_ID)));
        pagesItemDB.setSederOrder(cursor.getInt(cursor.getColumnIndex(KEY_SEDER_ORDER)));
        pagesItemDB.setMasechetOrder(cursor.getInt(cursor.getColumnIndex(KEY_MASECHET_ORDER)));
        pagesItemDB.setMasechetName(cursor.getString(cursor.getColumnIndex(KEY_MASECHET_NAME)));
        pagesItemDB.setChapter(cursor.getInt(cursor.getColumnIndex(KEY_CHAPTER_NUMBER)));
        pagesItemDB.setPageNumber(cursor.getInt(cursor.getColumnIndex(KEY_PAGE_NUMBER)));
        pagesItemDB.setVideo(cursor.getString(cursor.getColumnIndex(KEY_VIDEO_URL)));
        pagesItemDB.setAudio(cursor.getString(cursor.getColumnIndex(KEY_AUDIO_URL)));
        User presentor = new User();
        presentor.setId(cursor.getInt(cursor.getColumnIndex(KEY_PRESENT_ID)));
        pagesItemDB.setPresenter(presentor);
        pagesItemDB.setDuration(cursor.getInt(cursor.getColumnIndex(KEY_DURATION)));
        pagesItemDB.setTimeLine(cursor.getLong(cursor.getColumnIndex(KEY_TIME_LINE)));
        pagesItemDB.setMediaType(cursor.getString(cursor.getColumnIndex(KEY_MEDIA_TYPE)));
        pagesItemDB.setPage(cursor.getString(cursor.getColumnIndex(KEY_PAGE_URL)));


    }


    public MishnayotItem findMishnaById(String mishnaID, String mishnaTable) {

        MishnayotItem mishnayotItem = null;
        String selectQuery =
                "SELECT " +
                        "* " +
                        "FROM " + '"' +
                        mishnaTable + '"' + " " +
                        "WHERE " +
                        KEY_MISHNA_ID + " = '" + mishnaID + "' ";

        SQLiteDatabase db = this.getWritableDatabase();

        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {

            mishnayotItem = new MishnayotItem();

            setMishnaFieldsdataBaseObjectFromCurser(cursor, mishnayotItem);

        }

        cursor.close();

        db.close();

        return mishnayotItem;


    }


    private void setMishnaFieldsdataBaseObjectFromCurser(Cursor cursor, MishnayotItem mishnayotItem) {


        mishnayotItem.setId(cursor.getInt(cursor.getColumnIndex(KEY_MISHNA_ID)));
        mishnayotItem.setSederOrder(cursor.getInt(cursor.getColumnIndex(KEY_SEDER_ORDER)));
        mishnayotItem.setMasechetOrder(cursor.getInt(cursor.getColumnIndex(KEY_MASECHET_ORDER)));
        mishnayotItem.setMasechetName(cursor.getString(cursor.getColumnIndex(KEY_MASECHET_NAME)));
        mishnayotItem.setChapter(cursor.getInt(cursor.getColumnIndex(KEY_CHAPTER_NUMBER)));
        mishnayotItem.setVideo(cursor.getString(cursor.getColumnIndex(KEY_VIDEO_URL)));
        mishnayotItem.setAudio(cursor.getString(cursor.getColumnIndex(KEY_AUDIO_URL)));
        User presentor = new User();
        presentor.setId(cursor.getInt(cursor.getColumnIndex(KEY_PRESENT_ID)));
        mishnayotItem.setPresenter(presentor);
        mishnayotItem.setDuration(cursor.getInt(cursor.getColumnIndex(KEY_DURATION)));
        mishnayotItem.setTimeLine(cursor.getLong(cursor.getColumnIndex(KEY_TIME_LINE)));
        mishnayotItem.setMediaType(cursor.getString(cursor.getColumnIndex(KEY_MEDIA_TYPE)));
        mishnayotItem.setMishna(cursor.getInt(cursor.getColumnIndex(KEY_PAGE_NUMBER)));
        mishnayotItem.setPage(cursor.getString(cursor.getColumnIndex(KEY_PAGE_URL)));

    }


    private void setVideoPartObjectFromCursor(Cursor cursor, VideoPart videoPart) {


        videoPart.setId(cursor.getInt(cursor.getColumnIndex(KEY_GEMARA_ID)));
        videoPart.setVideoPartTimeLine(cursor.getString(cursor.getColumnIndex(KEY_VIDEO_PART_TIME_LINE)));
        videoPart.setPartTitle(cursor.getString(cursor.getColumnIndex(KEY_PART_TITLE)));


    }


    private void setMishnaVideoPartObjectFromCursor(Cursor cursor, VideoPart videoPart) {


        videoPart.setId(cursor.getInt(cursor.getColumnIndex(KEY_MISHNA_ID)));
        videoPart.setVideoPartTimeLine(cursor.getString(cursor.getColumnIndex(KEY_VIDEO_PART_TIME_LINE)));
        videoPart.setPartTitle(cursor.getString(cursor.getColumnIndex(KEY_PART_TITLE)));


    }


    public boolean addMishnaToTable(MishnayotItem mishnayotItem) {

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(KEY_MISHNA_ID, mishnayotItem.getId());
        values.put(KEY_SEDER_ORDER, mishnayotItem.getSederOrder());
        values.put(KEY_MASECHET_ORDER, mishnayotItem.getMasechetOrder());
        values.put(KEY_MASECHET_NAME, mishnayotItem.getMasechetName());
        values.put(KEY_CHAPTER_NUMBER, mishnayotItem.getChapter());
        values.put(KEY_VIDEO_URL, mishnayotItem.getVideo());
        values.put(KEY_AUDIO_URL, mishnayotItem.getAudio());
        values.put(KEY_PAGE_NUMBER, mishnayotItem.getMishna());

        if (mishnayotItem.getPresenter() != null) {

            values.put(KEY_PRESENT_ID, mishnayotItem.getPresenter().getId());
        }

        values.put(KEY_DURATION, mishnayotItem.getDuration());
        values.put(KEY_TIME_LINE, mishnayotItem.getTimeLine());
        values.put(KEY_MEDIA_TYPE, mishnayotItem.getMediaType());
        values.put(KEY_PAGE_URL, mishnayotItem.getPage());

        db.insert(MISHNA_TABLE, null, values);

        db.close();

        return true;

    }


    public List<MishnayotItem> getAllMishnaDownloadTalmud(String mishnaTable) {

        try {
            List<MishnayotItem> mishnayotItemList = new ArrayList<>();

            String selectQuery = "SELECT * FROM " + '"' + mishnaTable + '"' + " " +
                    "WHERE (" +
                    KEY_AUDIO_URL + " != '" + "" + "' OR " + KEY_VIDEO_URL + " != " + "''" + " )" + " ORDER BY " + KEY_SEDER_ORDER + ", " + KEY_MASECHET_ORDER;

//            String selectQuery = "SELECT * FROM " + '"' + mishnaTable + '"' + " ORDER BY " + KEY_SEDER_ORDER + ", " + KEY_MASECHET_ORDER;

            SQLiteDatabase db = this.getWritableDatabase();

            Cursor cursor = db.rawQuery(selectQuery, null);

            if (cursor.moveToFirst()) {
                do {

                    MishnayotItem dataBaseObject = new MishnayotItem();

                    setFieldsdataMishnaBaseObjectFromCurser(cursor, dataBaseObject);

                    mishnayotItemList.add(dataBaseObject);

                }
                while (cursor.moveToNext());
            }
            cursor.close();

            db.close();

            return mishnayotItemList;
        } catch (Exception e) {
            return new ArrayList<>();
        }

    }


    private void setFieldsdataMishnaBaseObjectFromCurser(Cursor cursor, MishnayotItem mishnayotItem) {

        mishnayotItem.setId(cursor.getInt(cursor.getColumnIndex(KEY_MISHNA_ID)));
        mishnayotItem.setSederOrder(cursor.getInt(cursor.getColumnIndex(KEY_SEDER_ORDER)));
        mishnayotItem.setMasechetOrder(cursor.getInt(cursor.getColumnIndex(KEY_MASECHET_ORDER)));
        mishnayotItem.setMasechetName(cursor.getString(cursor.getColumnIndex(KEY_MASECHET_NAME)));
        mishnayotItem.setChapter(cursor.getInt(cursor.getColumnIndex(KEY_CHAPTER_NUMBER)));
        mishnayotItem.setVideo(cursor.getString(cursor.getColumnIndex(KEY_VIDEO_URL)));
        mishnayotItem.setAudio(cursor.getString(cursor.getColumnIndex(KEY_AUDIO_URL)));
        User presentor = new User();
        presentor.setId(cursor.getInt(cursor.getColumnIndex(KEY_PRESENT_ID)));
        mishnayotItem.setPresenter(presentor);
        mishnayotItem.setDuration(cursor.getInt(cursor.getColumnIndex(KEY_DURATION)));
        mishnayotItem.setTimeLine(cursor.getLong(cursor.getColumnIndex(KEY_TIME_LINE)));
        mishnayotItem.setMediaType(cursor.getString(cursor.getColumnIndex(KEY_MEDIA_TYPE)));
        mishnayotItem.setMishna(cursor.getInt(cursor.getColumnIndex(KEY_PAGE_NUMBER)));
        mishnayotItem.setPage(cursor.getString(cursor.getColumnIndex(KEY_PAGE_URL)));
    }


    public List<Gallery> getGallery(String tableName, String masechetId) {


        try {
            List<Gallery> galleryList = new ArrayList<>();


            String selectQuery =
                    "SELECT " +
                            "* " +
                            "FROM " + '"' +
                            tableName + '"' + " " +
                            "WHERE " +
                            KEY_GEMARA_ID + " = '" + masechetId + "' ";


            SQLiteDatabase db = this.getWritableDatabase();

            Cursor cursor = db.rawQuery(selectQuery, null);

            if (cursor.moveToFirst()) {
                do {

                    Gallery gallery = new Gallery();

                    setGalleryObjectFromCursor(cursor, gallery);

                    galleryList.add(gallery);

                }
                while (cursor.moveToNext());
            }
            cursor.close();

            db.close();

            return galleryList;
        } catch (Exception e) {
            return new ArrayList<>();
        }


    }


    public List<Gallery> getMishnaGallery(String tableName, String masechetId) {


        try {
            List<Gallery> galleryList = new ArrayList<>();


            String selectQuery =
                    "SELECT " +
                            "* " +
                            "FROM " + '"' +
                            tableName + '"' + " " +
                            "WHERE " +
                            KEY_MISHNA_ID + " = '" + masechetId + "' ";


            SQLiteDatabase db = this.getWritableDatabase();

            Cursor cursor = db.rawQuery(selectQuery, null);

            if (cursor.moveToFirst()) {
                do {

                    Gallery gallery = new Gallery();

                    setMishnaGalleryObjectFromCursor(cursor, gallery);

                    galleryList.add(gallery);

                }
                while (cursor.moveToNext());
            }
            cursor.close();

            db.close();

            return galleryList;
        } catch (Exception e) {
            return new ArrayList<>();
        }


    }


    private void setMishnaGalleryObjectFromCursor(Cursor cursor, Gallery gallery) {

        gallery.setId(cursor.getInt(cursor.getColumnIndex(KEY_MISHNA_ID)));
        gallery.setOrder(cursor.getInt(cursor.getColumnIndex(KEY_MISHNA_GALLEY_ORDER)));
        gallery.setTitle(cursor.getString(cursor.getColumnIndex(KEY_TITLE)));
        gallery.setImage(cursor.getString(cursor.getColumnIndex(KEY_IMAGE_URL)));

    }


    private void setGalleryObjectFromCursor(Cursor cursor, Gallery gallery) {

        gallery.setId(cursor.getInt(cursor.getColumnIndex(KEY_GEMARA_ID)));
        gallery.setOrder(cursor.getInt(cursor.getColumnIndex(KEY_GEMARA_GALLEY_ORDER)));
        gallery.setTitle(cursor.getString(cursor.getColumnIndex(KEY_TITLE)));
        gallery.setImage(cursor.getString(cursor.getColumnIndex(KEY_IMAGE_URL)));


    }


    public void createChatTable() {

        final SQLiteDatabase database = getWritableDatabase();
        String CREATE_CHAT_TABLE = "CREATE TABLE if not exists " + "\"" + CHAT_TABLE + "\"" + "("
                + KEY_ID + " INTEGER PRIMARY KEY,"
                + KEY_CHAT_ID + " INTEGER,"
                + KEY_CHAT_TITLE + " TEXT,"
                + KEY_LAST_MESSAGE_TIME + " LONG,"
                + KEY_FROM_USER + " TEXT,"
                + KEY_TO_USER + " TEXT,"
                + KEY_LAST_MESSAGE + " TEXT,"
                + KEY_IMAGE + " TEXT,"
                + KEY_CHAT_TYPE + " INTEGER,"
                + KEY_UNREAD_MESSAGES + " INTEGER,"
                + KEY_LESSON_ID + " INTEGER,"
                + KEY_IS_GEMARA + " INTEGER,"
                + KEY_CREATED_AT + " INTEGER " +
                ")";

        database.execSQL(CREATE_CHAT_TABLE);
        database.close();

    }


    public void createMessageTable() {


        final SQLiteDatabase database = getWritableDatabase();
        String CREATE_MESSAGE_TABLE = "CREATE TABLE if not exists " + "\"" + MESSAGE_TABLE + "\"" + "("
                + KEY_ID + " INTEGER PRIMARY KEY,"
                + KEY_MESSAGE_ID + " INTEGER,"
                + KEY_CHAT_ID + " INTEGER,"
                + KEY_SENT_AT + " LONG,"
                + KEY_READ + " INTEGER,"
                + KEY_FROM_USER + " TEXT,"
                + KEY_TO_USER + " TEXT,"
                + MESSAGE_TYPE + " TEXT,"
                + KEY_CHAT_TITLE + " TEXT,"
                + MESSAGE + " TEXT,"
                + IS_MINE + " INTEGER" +
                ")";

        database.execSQL(CREATE_MESSAGE_TABLE);
        database.close();


    }



    public boolean addChatToTable(ChatObject chatObject) {

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(KEY_CHAT_ID, chatObject.getChatId());
        values.put(KEY_CHAT_TITLE, chatObject.getTitle());
        values.put(KEY_CHAT_TYPE, chatObject.getChatType());
        values.put(KEY_FROM_USER, chatObject.getFromUser());
        values.put(KEY_TO_USER, chatObject.getToUser());
        values.put(KEY_CREATED_AT, chatObject.getCreatedAt());
        values.put(KEY_LAST_MESSAGE_TIME, chatObject.getLastMessageTime());
        values.put(KEY_LAST_MESSAGE, chatObject.getLastMessage());
        values.put(KEY_UNREAD_MESSAGES, chatObject.getUnreadMessages());
        values.put(KEY_LESSON_ID, chatObject.getLessonId());
        values.put(KEY_IS_GEMARA, chatObject.getIsGemara());

        db.insert(CHAT_TABLE, null, values);

        db.close();

        return true;


    }


    public boolean addMessageToTable(MessageObject messageObject) {

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(KEY_MESSAGE_ID, messageObject.getMessageId());
        values.put(KEY_CHAT_ID, messageObject.getChatId());
        values.put(KEY_SENT_AT, messageObject.getSentAt());
        values.put(MESSAGE_TYPE, messageObject.getMessageType());
        values.put(MESSAGE, messageObject.getMessage());
        values.put(IS_MINE, messageObject.getIsMine());
        values.put(KEY_READ, messageObject.isRead());
        values.put(KEY_FROM_USER, messageObject.getFromUser());
        values.put(KEY_TO_USER, messageObject.getToUser());
        values.put(KEY_CHAT_TITLE, messageObject.getTitle());

        db.insert(MESSAGE_TABLE, null, values);

        db.close();

        return true;

    }


    public List<ChatObject> getAllChats(String tableName) {

        try {
            List<ChatObject> messageMainObjectList = new ArrayList<>();

            String selectQuery = "SELECT * FROM " + '"' + tableName + '"' + " ORDER BY " + KEY_LAST_MESSAGE_TIME + " DESC";

            SQLiteDatabase db = this.getWritableDatabase();

            Cursor cursor = db.rawQuery(selectQuery, null);

            if (cursor.moveToFirst()) {
                do {

                    ChatObject messageMainObject = new ChatObject();

                    setChatFieldsDataBaseObjectFromCurser(cursor, messageMainObject);

                    messageMainObjectList.add(messageMainObject);

                }
                while (cursor.moveToNext());
            }
            cursor.close();

            db.close();

            return messageMainObjectList;
        } catch (Exception e) {
            return new ArrayList<>();
        }
    }



    private void setChatFieldsDataBaseObjectFromCurser(Cursor cursor, ChatObject chatObject) {


        chatObject.setChatId(cursor.getInt(cursor.getColumnIndex(KEY_CHAT_ID)));
        chatObject.setTitle(cursor.getString(cursor.getColumnIndex(KEY_CHAT_TITLE)));
        chatObject.setChatType(cursor.getInt(cursor.getColumnIndex(KEY_CHAT_TYPE)));
        chatObject.setCreatedAt(cursor.getLong(cursor.getColumnIndex(KEY_CREATED_AT)));
        chatObject.setLastMessageTime(cursor.getLong(cursor.getColumnIndex(KEY_LAST_MESSAGE_TIME)));
        chatObject.setLastMessage(cursor.getString(cursor.getColumnIndex(KEY_LAST_MESSAGE)));
        chatObject.setToUser(cursor.getInt(cursor.getColumnIndex(KEY_TO_USER)));
        chatObject.setFromUser(cursor.getInt(cursor.getColumnIndex(KEY_FROM_USER)));
        chatObject.setUnreadMessages(cursor.getInt(cursor.getColumnIndex(KEY_UNREAD_MESSAGES)));
        chatObject.setLessonId(cursor.getInt(cursor.getColumnIndex(KEY_LESSON_ID)));
        chatObject.setIsGemara(cursor.getInt(cursor.getColumnIndex(KEY_IS_GEMARA)) == 1);

    }

    public List<MessageObject> getAllMessages(String tableName, int chatId) {

        try {
            List<MessageObject> messageObjectList = new ArrayList<>();

            String selectQuery =
                    "SELECT " +
                            "* " +
                            "FROM " + '"' +
                            tableName + '"' + " " +
                            "WHERE " +
                            KEY_CHAT_ID + " = '" + chatId + "' ";

            SQLiteDatabase db = this.getWritableDatabase();

            Cursor cursor = db.rawQuery(selectQuery, null);

            if (cursor.moveToFirst()) {
                do {

                    MessageObject messageObject = new MessageObject();

                    setMessageFieldsDataBaseObjectFromCursor(cursor, messageObject);

                    messageObjectList.add(messageObject);

                }
                while (cursor.moveToNext());
            }
            cursor.close();

            db.close();

            return messageObjectList;
        } catch (Exception e) {
            return new ArrayList<>();
        }
    }


    private void setMessageFieldsDataBaseObjectFromCursor(Cursor cursor, MessageObject messageObject) {


        messageObject.setChatId(cursor.getInt(cursor.getColumnIndex(KEY_CHAT_ID)));
        messageObject.setMessageId(cursor.getInt(cursor.getColumnIndex(KEY_MESSAGE_ID)));
        messageObject.setSentAt(cursor.getLong(cursor.getColumnIndex(KEY_SENT_AT)));
        messageObject.setMessageType(cursor.getInt(cursor.getColumnIndex(MESSAGE_TYPE)));
        messageObject.setIsMine(cursor.getInt(cursor.getColumnIndex(IS_MINE)) == 1);
        messageObject.setMessage(cursor.getString(cursor.getColumnIndex(MESSAGE)));
        messageObject.setRead(cursor.getInt(cursor.getColumnIndex(KEY_READ))  == 1);
        messageObject.setFromUser(cursor.getInt(cursor.getColumnIndex(KEY_FROM_USER)));
        messageObject.setToUser(cursor.getInt(cursor.getColumnIndex(KEY_TO_USER)));
        messageObject.setTitle(cursor.getString(cursor.getColumnIndex(KEY_CHAT_TITLE)));


    }


    public void updateChatTable(ChatObject chatObject, String tableName) {

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(KEY_LAST_MESSAGE, chatObject.getLastMessage());
        values.put(KEY_LAST_MESSAGE_TIME, chatObject.getLastMessageTime());
        values.put(KEY_UNREAD_MESSAGES, chatObject.getUnreadMessages());

        int i = db.update(tableName, values, KEY_CHAT_ID + "=?",
                new String[]{String.valueOf(chatObject.getChatId())});
        db.close();


    }


    public void updateUnreadMessagesToChatTable(ChatObject chatObject, String tableName) {

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(KEY_UNREAD_MESSAGES, chatObject.getUnreadMessages());

        int i = db.update(tableName, values, KEY_CHAT_ID + "=?",
                new String[]{String.valueOf(chatObject.getChatId())});
        db.close();


    }
}

