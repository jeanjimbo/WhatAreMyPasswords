package ebeletskiy.gmail.com.passwords;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import ebeletskiy.gmail.com.passwords.R;
import ebeletskiy.gmail.com.passwords.R.id;
import ebeletskiy.gmail.com.passwords.R.layout;
import ebeletskiy.gmail.com.passwords.interfaces.SaveItemListener;
import ebeletskiy.gmail.com.passwords.models.Ticket;
import ebeletskiy.gmail.com.passwords.utils.DBHelper;

public class NewItem extends Fragment {
	private static final String TAG = "EditItem";
	
	DBHelper dbHelper;
	SaveItemListener saveItemListener;
	
	EditText title;
	EditText login;
	EditText password;
	EditText notes;

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		saveItemListener = (SaveItemListener)activity;
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
		return inflater.inflate(R.layout.new_item, container, false);
	}
	
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		
		dbHelper = new DBHelper(getActivity());
	}
	
	
	private Ticket createTicket() {
		Ticket ticket = new Ticket();
		
		title = (EditText)getView().findViewById(R.id.et_title);
		login = (EditText)getView().findViewById(R.id.et_login_data);
		password = (EditText)getView().findViewById(R.id.et_password_data);
		notes = (EditText)getView().findViewById(R.id.et_notes_data);
		
		ticket.setTitle( (title.getText()).toString() );
		ticket.setLogin( (login.getText()).toString() );
		ticket.setPassword( (password.getText()).toString() );
		ticket.setNotes( (notes.getText()).toString() );
		
		return ticket;
	}

	
	private boolean checkFields() {
		if (title.getText().toString().equals("") || login.getText().toString().equals("")
				|| password.getText().toString().equals("")) {
			return  false;
		} else {
			return true; 
		}
	}
	
	
	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
	    inflater.inflate(R.menu.menu_new_item, menu);
	}
	
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		
		switch (item.getItemId()) {
			case R.id.save_item: 
			
				dbHelper.insert( createTicket() );
				
				if (saveItemListener != null && checkFields()) {
					saveItemListener.saveItem();
					Toast t = Toast.makeText(getActivity(), "New ticket has been created",
							Toast.LENGTH_SHORT);
					t.setGravity(Gravity.CENTER_HORIZONTAL|Gravity.CENTER, 0, 0);
					t.show();
				} else {
					Toast t = Toast.makeText(getActivity(), "Please fill all mandatory fields",
							Toast.LENGTH_SHORT);
					t.setGravity(Gravity.CENTER_HORIZONTAL|Gravity.CENTER, 0, 0);
					t.show();
				}
				
			break;
		}
		return super.onOptionsItemSelected(item);
	}
}
