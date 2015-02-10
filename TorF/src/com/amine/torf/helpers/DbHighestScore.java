package com.amine.torf.helpers;
import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.amine.torf.pojo.Scoredata;
 
public class DbHighestScore extends SQLiteOpenHelper {
 
    // All Static variables
    // Database Version
    private static final int DATABASE_VERSION = 2;
 
    // Database Name
    private static final String DATABASE_NAME = "scoresimple";
 
    // Contacts table name
    private static final String TABLE_CONTACTS = "highscore";
 
    // Contacts Table Columns names
    private static final String KEY_ID = "id";
    private static final String KEY_NAME = "name";
    private static final String KEY_SCORE = "score";

    
    
    public DbHighestScore(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
 
    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_CONTACTS_TABLE = "CREATE TABLE " + TABLE_CONTACTS + "("
                + KEY_ID + " INTEGER PRIMARY KEY," + KEY_NAME + " TEXT, " 
                + KEY_SCORE + " TEXT" + ")";
        
        db.execSQL(CREATE_CONTACTS_TABLE);
    }
 
    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
    	
    }
 
    /**
     * All CRUD(Create, Read, Update, Delete) Operations
     */
 
    public // Adding new contact
    void addContact(Scoredata score) {
        SQLiteDatabase db = this.getWritableDatabase();
 
        ContentValues values = new ContentValues();
 
        values.put(KEY_NAME, score.getName()); 
        values.put(KEY_SCORE, score.getScore()); 
          
        db.insert(TABLE_CONTACTS, null, values);
        db.close(); // Closing database connection
    }
 
     

    // Getting All Contacts
    public List<Scoredata> getAllContacts() {
        List<Scoredata> scoreList = new ArrayList<Scoredata>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_CONTACTS + " ORDER BY id DESC";
 
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
 
        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Scoredata score = new Scoredata();
                score.setId(Integer.valueOf(cursor.getString(0)));
                score.setName(cursor.getString(1));
                score.setScore(cursor.getString(2));
    
                
                scoreList.add(score);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
 
        // return contact list
        return scoreList;
    }
 
 
}