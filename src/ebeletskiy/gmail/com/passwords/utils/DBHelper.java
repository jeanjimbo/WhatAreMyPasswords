package ebeletskiy.gmail.com.passwords.utils;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {
	
	private static final String TAG = "DBHelper";
	
	private static final String DATABASE_NAME = "wampdb";
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
	
	public Cursor getAll() {
		return(getReadableDatabase()
				.rawQuery("SELECT _id, COLUMN_NAME_TITLE, COLUMN_NAME_LOGIN," +
						"COLUMN_NAME_PASSWORD, COLUMN_NAME_NOTES " +
						"url FROM fragtestdb ORDER BY name", null));
	}
}
