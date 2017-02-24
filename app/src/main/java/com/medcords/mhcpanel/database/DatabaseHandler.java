package com.medcords.mhcpanel.database;

/**
 * Created by sidharthsethia on 24/02/17.
 */

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.medcords.mhcpanel.utilities.Utility;

public class DatabaseHandler extends SQLiteOpenHelper {

    // All Static variables
    // Database Version
    private static final int DATABASE_VERSION = 2;

    // Database Name
    private static final String DATABASE_NAME = "images.db";

    // Contacts table name
    private static final String TABLE_IMAGES = "images";

    // Contacts Table Columns names
    private static final String KEY_ID = "id";
    private static final String KEY_PATH = "path";
    private static final String KEY_BATCH_ID = "batch_id";
    private static final String KEY_DOCTOR_NAME = "doctor_name";
    private static final String KEY_DOC_TYPE = "doc_type";
    private static final String KEY_PATIENT_ID = "patient_id";
    private static final String KEY_REPORT_TYPE = "report_type";
    private static final String KEY_DATE = "date";
    private static final String KEY_TAGS = "tags";
    private static final String KEY_UPLOADED = "has_been_uploaded";

    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_CONTACTS_TABLE = "CREATE TABLE " + TABLE_IMAGES + "("
                + KEY_ID + " INTEGER PRIMARY KEY," + KEY_PATH + " TEXT,"
                + KEY_BATCH_ID + " TEXT,"
                + KEY_DOCTOR_NAME + " TEXT,"
                + KEY_PATIENT_ID + " TEXT,"
                + KEY_DOC_TYPE + " TEXT,"
                + KEY_REPORT_TYPE + " TEXT,"
                + KEY_DATE + " TEXT,"
                + KEY_TAGS + " TEXT,"
                + KEY_UPLOADED + " TEXT"
                + ")";
        db.execSQL(CREATE_CONTACTS_TABLE);
    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_IMAGES);

        // Create tables again
        onCreate(db);
    }

    /**
     * All CRUD(Create, Read, Update, Delete) Operations
     */

    // Adding new image
    public void addImage(ImageRecord imageRecord) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_PATH, imageRecord.getPath());
        values.put(KEY_BATCH_ID, imageRecord.getBatchId());
        values.put(KEY_DOCTOR_NAME, imageRecord.getDoctorName());
        values.put(KEY_PATIENT_ID, imageRecord.getPatientId());
        values.put(KEY_DOC_TYPE, imageRecord.getDocType());
        values.put(KEY_REPORT_TYPE, imageRecord.getReportType());
        values.put(KEY_DATE, imageRecord.getDate());
        values.put(KEY_TAGS, imageRecord.getTags());
        values.put(KEY_UPLOADED, imageRecord.getHasBeenUploaded());

        // Inserting Row
        db.insert(TABLE_IMAGES, null, values);
        db.close(); // Closing database connection
    }

    // Getting single image
    public ImageRecord getImage(int id) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_IMAGES, new String[] { KEY_ID,
                        KEY_PATH, KEY_BATCH_ID, KEY_DOCTOR_NAME, KEY_PATIENT_ID, KEY_DOC_TYPE,
                        KEY_REPORT_TYPE, KEY_DATE, KEY_TAGS, KEY_UPLOADED}, KEY_ID + "=?",
                new String[] { String.valueOf(id) }, null, null, null, null);

        if (cursor != null)
            cursor.moveToFirst();

        ImageRecord imageRecord = new ImageRecord(Integer.parseInt(cursor.getString(0)),
                cursor.getString(1), cursor.getString(2), cursor.getString(3),cursor.getString(4),cursor.getString(5),cursor.getString(6),
                cursor.getString(7),cursor.getString(8),Integer.parseInt(cursor.getString(9)));
        // return image
        return imageRecord;
    }

    // Getting All Images
    public List<ImageRecord> getAllImages() {
        List<ImageRecord> imageList = new ArrayList<ImageRecord>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_IMAGES;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                ImageRecord imageRecord = new ImageRecord();
                imageRecord.setId(Integer.parseInt(cursor.getString(0)));
                imageRecord.setPath(cursor.getString(1));
                imageRecord.setBatchId(cursor.getString(2));
                imageRecord.setDoctorName(cursor.getString(3));
                imageRecord.setPatientId(cursor.getString(4));
                imageRecord.setDocType(cursor.getString(5));
                imageRecord.setReportType(cursor.getString(6));
                imageRecord.setDate(cursor.getString(7));
                imageRecord.setTags(cursor.getString(8));
                imageRecord.setHasBeenUploaded(Integer.parseInt(cursor.getString(9)));
                // Adding image to list
                imageList.add(imageRecord);
            } while (cursor.moveToNext());
        }

        // return image list
        return imageList;
    }

    // Updating single image
    public int updateImageRecord(ImageRecord imageRecord) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_PATH, imageRecord.getPath());
        values.put(KEY_BATCH_ID, imageRecord.getBatchId());
        values.put(KEY_DOCTOR_NAME, imageRecord.getDoctorName());
        values.put(KEY_PATIENT_ID, imageRecord.getPatientId());
        values.put(KEY_DOC_TYPE, imageRecord.getDocType());
        values.put(KEY_REPORT_TYPE, imageRecord.getReportType());
        values.put(KEY_DATE, imageRecord.getDate());
        values.put(KEY_TAGS, imageRecord.getTags());
        values.put(KEY_UPLOADED, imageRecord.getHasBeenUploaded());


        // updating row
        return db.update(TABLE_IMAGES, values, KEY_ID + " = ?",
                new String[] { String.valueOf(imageRecord.getId()) });
    }

    // Deleting single image
    public void deleteImage(ImageRecord image) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_IMAGES, KEY_ID + " = ?",
                new String[] { String.valueOf(image.getId()) });
        db.close();

        Utility.deleteImage(image.getPath());
    }


    // Getting images Count
    public int getImageCount() {
        String countQuery = "SELECT  * FROM " + TABLE_IMAGES;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        int count = cursor.getCount();
        cursor.close();
        // return count
        return count;
    }

    // Getting image record id
    public int getImageId(String path) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_IMAGES, new String[] { KEY_ID,
                        KEY_PATH, KEY_BATCH_ID, KEY_DOCTOR_NAME, KEY_PATIENT_ID, KEY_DOC_TYPE,
                        KEY_REPORT_TYPE, KEY_DATE, KEY_TAGS, KEY_UPLOADED}, KEY_PATH + "=?",
                new String[] { path }, null, null, null, null);

        if (cursor != null)
            cursor.moveToFirst();

        // return image id
        return Integer.parseInt(cursor.getString(0));
    }


}