package ebeletskiy.gmail.com.passwords;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;
import ebeletskiy.gmail.com.passwords.interfaces.SaveItemListener;
import ebeletskiy.gmail.com.passwords.models.Ticket;
import ebeletskiy.gmail.com.passwords.utils.DBHelper;

public class NewItem extends Fragment {
	private static final String TAG = "EditItem";
	
	private boolean menuWasCreated = false;
	
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
		
		if (!menuWasCreated) {
			Log.i(TAG, "creating menu");
			setHasOptionsMenu(true);
			menuWasCreated = true;
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
	
	
	public Ticket createTicket() {
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

	
	public boolean checkFields() {
		if (title.getText().toString().equals("")) {
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
					InputMethodManager imm =  (InputMethodManager) getActivity().
						getSystemService(Context.INPUT_METHOD_SERVICE);
					imm.hideSoftInputFromWindow(title.getWindowToken(), 0);
					
					saveItemListener.saveItem();
					showToast("New ticket has been created");
				} else {
					showToast("Please fill the title");
				}
				
			break;
		}
		return super.onOptionsItemSelected(item);
	}
	
	private void showToast(String str) {
		Toast t = Toast.makeText(getActivity(), str, Toast.LENGTH_SHORT);
		t.setGravity(Gravity.CENTER_HORIZONTAL|Gravity.CENTER, 0, 0);
		t.show();
	}
}
