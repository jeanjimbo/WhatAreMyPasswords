package ebeletskiy.gmail.com.passwords.utils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import ebeletskiy.gmail.com.passwords.models.Ticket;

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
	
	public void insert(Ticket ticket) {

		ContentValues cv=new ContentValues();
		
		cv.put(COLUMN_NAME_TITLE, ticket.getTitle());
		cv.put(COLUMN_NAME_LOGIN, ticket.getLogin());
		cv.put(COLUMN_NAME_PASSWORD, ticket.getPassword());
		cv.put(COLUMN_NAME_NOTES, ticket.getNotes());
		
		getWritableDatabase().insert(TABLE_NAME, COLUMN_NAME_TITLE, cv);
	}
	
	public Cursor getAll() {
		return(getReadableDatabase()
				.rawQuery("SELECT _id, " + 
						COLUMN_NAME_TITLE + ", " +
						COLUMN_NAME_LOGIN + ", " +
						COLUMN_NAME_PASSWORD + ", " +
						COLUMN_NAME_NOTES + " FROM " + TABLE_NAME, null)); // TODO: updated 'title'
	}
	
	public void deleteRow(int id) {
		getWritableDatabase().delete(TABLE_NAME, "_id=" + id , null);
	}
	
	public void updateRow(Ticket ticket) {
		ContentValues cv=new ContentValues();
		
		cv.put(COLUMN_NAME_TITLE, ticket.getTitle());
		cv.put(COLUMN_NAME_LOGIN, ticket.getLogin());
		cv.put(COLUMN_NAME_PASSWORD, ticket.getPassword());
		cv.put(COLUMN_NAME_NOTES, ticket.getNotes());
		
		getWritableDatabase().update(TABLE_NAME, cv, "_id=?", new String[] {Integer.toString(ticket.getId())});
		
	}

	public String getTitle(Cursor c) {
		return(c.getString(1));
	}
	
	public Cursor getItem(int id) {
		return (getReadableDatabase()
					.rawQuery("SELECT _id, " + 
					COLUMN_NAME_TITLE + ", " + 
					COLUMN_NAME_LOGIN + ", " + 
					COLUMN_NAME_PASSWORD + ", " + 
					COLUMN_NAME_NOTES + " FROM " + TABLE_NAME + 
					" WHERE _id = " + id, null)); // TODO: updated 'title'
	}

	public int getId(Cursor c) {
		return(c.getInt(0));
	}

	public String getLogin(Cursor c) {
		return(c.getString(1));
	}
	
	public String getPassword(Cursor c) {
		return(c.getString(2));
	}
	
	public String getNotes(Cursor c) {
		return(c.getString(3));
	}
	
	public Cursor getItemByTitle(String str) {
		return (getReadableDatabase()
				.rawQuery("SELECT " + 
				COLUMN_NAME_TITLE + 
				" FROM " + TABLE_NAME + 
				" WHERE " + COLUMN_NAME_TITLE + " = " + str, null));
	}
	
	public void deleteAll() {
		getReadableDatabase().execSQL("DELETE FROM " + TABLE_NAME);
	}
}
