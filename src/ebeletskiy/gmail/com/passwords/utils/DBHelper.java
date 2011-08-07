package ebeletskiy.gmail.com.passwords.utils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DBHelper extends SQLiteOpenHelper {
	
	private static final String TAG = "DBHelper";
	
	private static final String DATABASE_NAME = "wampdb1";
	private static final int DATABASE_VERSION = 1;
	private static final String TABLE_NAME = "userdata";
	private static final String COLUMN_NAME_TITLE = "title";
	private static final String COLUMN_NAME_LOGIN = "login";
	private static final String COLUMN_NAME_PASSWORD = "password";
	private static final String COLUMN_NAME_NOTES = "notes";

	private static final String CREATE_DATABASE = "CREATE TABLE " +  
		TABLE_NAME + "(_id INTEGER PRIMARY KEY AUTOINCREMENT," +
		COLUMN_NAME_TITLE + " TEXT, " +
		COLUMN_NAME_LOGIN + " TEXT, " +
		COLUMN_NAME_PASSWORD + " TEXT, " +
		COLUMN_NAME_NOTES + " TEXT);";
	
	public DBHelper(Context c) {
		super(c, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(CREATE_DATABASE);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		
	}
	
	public void insert(String title, String login, String password, 
			String notes) {

		Log.d("Dev", "insert()");
		ContentValues cv=new ContentValues();
		
		cv.put(COLUMN_NAME_TITLE, title);
		cv.put(COLUMN_NAME_LOGIN, login);
		cv.put(COLUMN_NAME_PASSWORD, password);
		cv.put(COLUMN_NAME_NOTES, notes);
		
		getWritableDatabase().insert(TABLE_NAME, COLUMN_NAME_TITLE, cv);
		
		getWritableDatabase().close();
	}
	
	public Cursor getAll() {
		return(getReadableDatabase()
				.rawQuery("SELECT _id, " + 
						COLUMN_NAME_TITLE + ", " +
						COLUMN_NAME_LOGIN + ", " +
						COLUMN_NAME_PASSWORD + ", " +
						COLUMN_NAME_NOTES + " FROM " + TABLE_NAME + 
						" ORDER BY title", null));
	}

	public String getTitle(Cursor c) {
		return(c.getString(1));
	}
	
	public String getLogin(Cursor c) {
		return(c.getString(2));
	}
	
	public String getPassword(Cursor c) {
		return(c.getString(3));
	}
	
	public String getNotes(Cursor c) {
		return(c.getString(4));
	}
}
