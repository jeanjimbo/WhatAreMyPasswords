package ebeletskiy.gmail.com.passwords.utils;

import android.database.Cursor;
import ebeletskiy.gmail.com.passwords.models.Ticket;

public class DataConverter {

	public static Ticket convertToTicket(Cursor result) {
		Ticket ticket = new Ticket();
		
		while (!result.moveToNext()) {
			ticket.setId(result.getInt(0));
			ticket.setTitle(result.getString(1));
			ticket.setLogin(result.getString(2));
			ticket.setPassword(result.getString(3));
			ticket.setNotes(result.getString(4));
		}
	
		result.close();
		return ticket;
	}
}
