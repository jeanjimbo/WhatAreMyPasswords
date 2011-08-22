package ebeletskiy.gmail.com.passwords;

import ebeletskiy.gmail.com.passwords.R;
import ebeletskiy.gmail.com.passwords.R.layout;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import ebeletskiy.gmail.com.passwords.models.Ticket;

public class ItemsDescription extends Fragment {
	private static final String TAG = "ItemsDescription";
	
	Ticket ticket; 
	TextView title, login, password, notes;
	
	public ItemsDescription() {
		
	}
	
	public ItemsDescription(Ticket ticket) {
		if (ticket != null) {
			this.ticket = ticket;
		} else {
			throw new IllegalArgumentException();
		}
	}
	
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		if (savedInstanceState == null) {
			setHasOptionsMenu(true);
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return inflater.inflate(R.layout.items_description, container, false);
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		
		if (savedInstanceState == null) {
			title = (TextView)getView().findViewById(R.id.tv_title);
			title.setText(ticket.getTitle());
			
			login = (TextView)getView().findViewById(R.id.tv_login_data);
			login.setText(ticket.getLogin());
			
			password = (TextView)getView().findViewById(R.id.tv_password_data);
			password.setText(ticket.getPassword());
			
			notes = (TextView)getView().findViewById(R.id.tv_notes_data);
			notes.setText(ticket.getNotes());
		}		
	}
	
	
	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		 menu.add("Show password").setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
         menu.add("Delete").setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
	}
}
