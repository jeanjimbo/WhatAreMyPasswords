package ebeletskiy.gmail.com.passwords;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import ebeletskiy.gmail.com.passwords.interfaces.DeleteItemListener;
import ebeletskiy.gmail.com.passwords.interfaces.EditItemListener;
import ebeletskiy.gmail.com.passwords.models.Ticket;
import ebeletskiy.gmail.com.passwords.utils.DBHelper;

public class ItemsDescription extends Fragment {
	private static final String TAG = "ItemsDescription";
	
	Ticket ticket; 
	TextView title, login, password, notes;
	DBHelper dbHelper;
	DeleteItemListener deleteListener;
	EditItemListener editItemListener;
	
	public ItemsDescription() {
		
	}
	
	public ItemsDescription(Ticket ticket) {
		if (ticket == null) {		
			throw new IllegalArgumentException();
		}
		
		this.ticket = ticket;		
	}
	
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		deleteListener = (DeleteItemListener)activity;
		editItemListener = (EditItemListener)activity;
	}
	
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		dbHelper = new DBHelper(getActivity());
		
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
	    inflater.inflate(R.menu.menu_items_description, menu);
	}
	
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		
			case R.id.show_pass: 
				password.setTransformationMethod(null);
				break;
				
			case R.id.delete_item: 
				dbHelper.deleteRow(ticket.getId());
				deleteListener.onDeleteItem();
				break;
			
			case R.id.edit_item:
				editItemListener.loadEditItem( createTicket() );
			default: break;
		}
		return super.onOptionsItemSelected(item);
		
	}

	private Ticket createTicket() {
		Ticket ticket = new Ticket();
		
		ticket.setTitle( (title.getText()).toString() );
		ticket.setLogin( (login.getText()).toString() );
		ticket.setPassword( (password.getText()).toString() );
		ticket.setNotes( (notes.getText()).toString() );
		
		return ticket;
	}
}
