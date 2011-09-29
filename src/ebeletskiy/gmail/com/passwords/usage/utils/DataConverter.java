package ebeletskiy.gmail.com.passwords.usage.utils;

import android.database.Cursor;
import ebeletskiy.gmail.com.passwords.usage.models.Ticket;

public class DataConverter {
    private static final String TAG = "DataConverter";

    public static Ticket convertToTicket(Cursor result) {
        Ticket ticket = new Ticket();

        // String str = " ";
        // String [] stringarray = result.getColumnNames();
        // for (int i = 0; i < stringarray.length; i++) {
        // str = str + stringarray[i];
        // }
        // Log.i(TAG, str);

        result.moveToFirst();
        ticket.setId(result.getInt(0));
        ticket.setTitle(result.getString(1));
        ticket.setLogin(result.getString(2));
        ticket.setPassword(result.getString(3));
        ticket.setNotes(result.getString(4));

        result.close();
        return ticket;
    }
}
