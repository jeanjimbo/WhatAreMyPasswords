package ebeletskiy.gmail.com.passwords.controller;

import android.database.Cursor;
import ebeletskiy.gmail.com.passwords.models.DataModel;

public interface ControllerInterface {
	/**
	 * Retrieve all columns from user table
	 * @return Cursor
	 */
	Cursor getAllData();
	
	
	/**
	 * Inserts provided DataModel into database
	 * @param DataModel data
	 */
	void insertItem(DataModel data);
	
	
	/**
	 * Deletes provided DataModel item from database
	 * @param DataModel
	 */
	void deleteItem(DataModel data);
	
	
	/** 
	 * Delets all data from user database
	 */
	void deleteAllItems();
}
