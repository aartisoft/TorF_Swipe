package com.amine.torf.helpers;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.amine.torf.pojo.QuizPojo;

public class DataBaseHelper extends SQLiteOpenHelper {
	private static String TAG = "DataBaseHelper";
	public static String DB_PATH = "";
	public static String DB_NAME = "quiz_torf";
	private SQLiteDatabase mDataBase;
	private final Context mContext;

	// Contacts table name
	private static final String TABLE_CONTACTS = "quiz";
	private static final String KEY_ID = "id";
	private static final String KEY_QUESTION = "question";
	private static final String KEY_ISTRUE = "isTrue";
	private static final String KEY_CATEGORYNAME = "category";
	private static final String KEY_COMMENT = "comment";
	private static final String KEY_DIFFICULTY = "difficulty";

	public DataBaseHelper(Context context) {
		super(context, DB_NAME, null, 3);// 1? its Database Version
		DB_PATH = "/data/data/" + context.getPackageName() + "/databases/";
		this.mContext = context;
	}

	public void createDataBase() throws IOException {
		// If database not exists copy it from the assets

		boolean mDataBaseExist = checkDataBase();
		if (!mDataBaseExist) {
			this.getReadableDatabase();
			this.close();
			try {
				// Copy the database from assests
				copyDataBase();
				Log.e(TAG, "createDatabase database created");
			} catch (IOException mIOException) {
				throw new Error("ErrorCopyingDataBase");
			}
		}
	}

	// Check that the database exists here: /data/data/your package/databases/Da
	// Name
	private boolean checkDataBase() {
		File dbFile = new File(DB_PATH + DB_NAME);
		// Log.v("dbFile", dbFile + "   "+ dbFile.exists());
		return dbFile.exists();
	}

	public void deleteDatabase() {

		File dbFile = new File(DB_PATH + DB_NAME);
		dbFile.delete();

	}

	// Copy the database from assets
	private void copyDataBase() throws IOException {
		InputStream mInput = mContext.getAssets().open(DB_NAME);
		String outFileName = DB_PATH + DB_NAME;
		OutputStream mOutput = new FileOutputStream(outFileName);
		byte[] mBuffer = new byte[1024];
		int mLength;
		while ((mLength = mInput.read(mBuffer)) > 0) {
			mOutput.write(mBuffer, 0, mLength);
		}
		mOutput.flush();
		mOutput.close();
		mInput.close();
	}

	// Open the database, so we can query it
	public boolean openDataBase() throws SQLException {
		String mPath = DB_PATH + DB_NAME;
		// Log.v("mPath", mPath);
		mDataBase = SQLiteDatabase.openDatabase(mPath, null,
				SQLiteDatabase.CREATE_IF_NECESSARY);
		// mDataBase = SQLiteDatabase.openDatabase(mPath, null,
		// SQLiteDatabase.NO_LOCALIZED_COLLATORS);
		return mDataBase != null;
	}

	@Override
	public synchronized void close() {
		if (mDataBase != null)
			mDataBase.close();
		super.close();
	}

	@Override
	public void onCreate(SQLiteDatabase arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub

	}

	void addContact(QuizPojo contact) {
		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();
		// values.put(KEY_ID, contact.get_id());
		values.put(KEY_QUESTION, contact.get_question());
		values.put(KEY_ISTRUE, contact.get_isTrue());
		values.put(KEY_DIFFICULTY, contact.get_difficulty());
		values.put(KEY_CATEGORYNAME, contact.get_category());
		values.put(KEY_COMMENT, contact.get_comment());

		// Inserting Row
		db.insert(TABLE_CONTACTS, null, values);
		db.close(); // Closing database connection
	}

	// Getting single contact
	/*
	 * QuizPojo getContact(int id) { SQLiteDatabase db =
	 * this.getReadableDatabase();
	 * 
	 * Cursor cursor = db.query(TABLE_CONTACTS, new String[] { KEY_ID,
	 * KEY_QUESTION, KEY_ISTRUE, KEY_DIFFICULTY, KEY_CATEGORYNAME }, KEY_ID +
	 * "=?", new String[] { String.valueOf(id) }, null, null, null, null); if
	 * (cursor != null) cursor.moveToFirst();
	 * 
	 * QuizPojo contact = new QuizPojo(cursor.getString(1), cursor.,
	 * cursor.getString(3), cursor.getString(4)); // return contact
	 * cursor.close(); db.close(); return contact; }
	 */

	// Getting All Contacts
	public void deletealldata() {

		// Select All Query

		SQLiteDatabase db = this.getWritableDatabase();
		db.delete(TABLE_CONTACTS, null, null);

		db.close();

	}

	// Getting All Contacts
	public List<QuizPojo> getAllContacts() {
		List<QuizPojo> contactList = new ArrayList<QuizPojo>();
		// Select All Query
		String selectQuery = "SELECT  * FROM " + TABLE_CONTACTS;

		SQLiteDatabase db = this.getWritableDatabase();
		Cursor cursor = db.rawQuery(selectQuery, null);

		// looping through all rows and adding to list
		if (cursor.moveToFirst()) {
			do {
				QuizPojo contact = new QuizPojo();
				contact.set_id(Integer.parseInt(cursor.getString(0)));
				contact.set_question(cursor.getString(1));
				contact.set_isTrue(cursor.getInt(2));
				contact.set_difficulty(cursor.getInt(3));
				contact.set_category(cursor.getString(4));
				contact.set_comment(cursor.getString(5));

				// Adding contact to list
				contactList.add(contact);
			} while (cursor.moveToNext());
		}
		cursor.close();
		db.close();

		// return contact list
		return contactList;
	}

	public List<QuizPojo> getcategory() {
		List<QuizPojo> categoryList = new ArrayList<QuizPojo>();
		// Select All Query
		String selectQuery = "SELECT DISTINCT category FROM " + TABLE_CONTACTS;

		SQLiteDatabase db = this.getWritableDatabase();
		Cursor cursor = db.rawQuery(selectQuery, null);

		// looping through all rows and adding to list
		if (cursor.moveToFirst()) {
			do {
				QuizPojo contact = new QuizPojo();

				contact.set_category(cursor.getString(0));

				categoryList.add(contact);
			} while (cursor.moveToNext());
		}
		cursor.close();
		db.close();

		// return contact list
		return categoryList;
	}

	public List<QuizPojo> getquestion(String categoryname) {
		List<QuizPojo> questionList = new ArrayList<QuizPojo>();
		int isAnswered = 0;

		String sql = "select count(*) from " + TABLE_CONTACTS
				+ " where isAnswered=1";
		String sql2 = "select count(*) from " + TABLE_CONTACTS;
		SQLiteDatabase db = this.getWritableDatabase();

		Cursor cursor1 = db.rawQuery(sql, null);
		Cursor cursor2 = db.rawQuery(sql2, null);
		int numberOfAnswered = 0, numberOfAnswers = 0;

		Log.w("column", "" + cursor1.getColumnCount());
		if (cursor1.moveToFirst())
			numberOfAnswered = cursor1.getInt(0);
		if (cursor2.moveToFirst())
			numberOfAnswers = cursor2.getInt(0);

		Log.w("nbr answers", "" + numberOfAnswers);
		Log.w("nbr answered", "" + numberOfAnswered);

		if (numberOfAnswered >= numberOfAnswers)
			isAnswered = 1;
		else
			isAnswered = 0;
		// Select All Query
		String selectQuery = "SELECT DISTINCT id,question, isTrue, difficulty, category , comment FROM "
				+ TABLE_CONTACTS
				+ " where "
				+ KEY_CATEGORYNAME
				+ "= ? and isAnswered=" + isAnswered + " ORDER BY RANDOM()";

		Cursor cursor = db.rawQuery(selectQuery, new String[] { categoryname });

		// looping through all rows and adding to list
		if (cursor.moveToFirst()) {
			do {
				QuizPojo contact = new QuizPojo();
				contact.set_id(cursor.getInt(0));
				contact.set_question(cursor.getString(1));
				contact.set_isTrue(cursor.getInt(2));
				contact.set_difficulty(cursor.getInt(3));
				contact.set_category(cursor.getString(4));
				contact.set_comment(cursor.getString(5));

				// Adding contact to list
				questionList.add(contact);
			} while (cursor.moveToNext());
		}

		// return contact list
		return questionList;

	}

	public int getContactsCount() {

		String countQuery = "SELECT * FROM " + TABLE_CONTACTS;
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery(countQuery, null);

		return cursor.getCount();
	}

	public int getcategorycount(String categoryname) {

		String selectQuery = "SELECT DISTINCT question, isTrue, difficulty, category,comment FROM"
				+ TABLE_CONTACTS + " where " + KEY_CATEGORYNAME + "= ?";

		SQLiteDatabase db = this.getWritableDatabase();

		Cursor cursor = db.rawQuery(selectQuery, new String[] { categoryname });

		// return count

		return cursor.getCount();
	}

	public static String getKeyId() {
		return KEY_ID;
	}

	public void insertIsanswered(int id) {

		String sql = "update " + TABLE_CONTACTS
				+ " set isAnswered=1 where id = " + id;
		SQLiteDatabase db = this.getWritableDatabase();
		db.execSQL(sql);

	}

}