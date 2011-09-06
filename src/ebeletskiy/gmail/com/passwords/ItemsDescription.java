package ebeletskiy.gmail.com.passwords;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import ebeletskiy.gmail.com.passwords.interfaces.DeleteItemListener;
import ebeletskiy.gmail.com.passwords.interfaces.EditItemListener;
import ebeletskiy.gmail.com.passwords.models.Ticket;
import ebeletskiy.gmail.com.passwords.utils.DBHelper;

public class ItemsDescription extends Fragment {
	private static final String TAG = "ItemsDescription";
	private boolean menuWasCreated = false;
	
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
		
		if (!menuWasCreated) {
			setHasOptionsMenu(true);
			menuWasCreated = true;
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
		
		initUI();
		
		if (savedInstanceState != null) {
			title.setText( (String)savedInstanceState.get("title") );
			login.setText( (String)savedInstanceState.get("login") );
			password.setText( (String)savedInstanceState.get("password") );
			notes.setText( (String)savedInstanceState.get("notes") );
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
				showAlertDialog();
				break;
			case R.id.edit_item:
				editItemListener.loadEditItem( ticket );
			default: break;
		}
		return super.onOptionsItemSelected(item);
		
	}
	
	private void showAlertDialog() {
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		builder.setMessage("Are you sure you want to delete the item?")
		       .setCancelable(false)
		       .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
		           public void onClick(DialogInterface dialog, int id) {
		        	   	dbHelper.deleteRow(ticket.getId());
						deleteListener.onDeleteItem();
						showToast("Item has been deleted");
		           }
		       })
		       .setNegativeButton("No", new DialogInterface.OnClickListener() {
		           public void onClick(DialogInterface dialog, int id) {
		                dialog.cancel();
		           }
		       });
		
		AlertDialog alert = builder.create();
		builder.show();
	}
	
	private void showToast(String string) {
		Toast t = Toast.makeText(getActivity(), string,
				Toast.LENGTH_SHORT);
		t.setGravity(Gravity.CENTER_HORIZONTAL|Gravity.CENTER, 0, 0);
		t.show();
	}
	
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putString("title", title.getText().toString());
		outState.putString("login", login.getText().toString());
		outState.putString("password", password.getText().toString());
		outState.putString("notes", notes.getText().toString());
	}
	
	private void initUI() {
		title = (TextView)getView().findViewById(R.id.tv_title);
		login = (TextView)getView().findViewById(R.id.tv_login_data);
		password = (TextView)getView().findViewById(R.id.tv_password_data);
		notes = (TextView)getView().findViewById(R.id.tv_notes_data);

		if (ticket != null) {
			title.setText(ticket.getTitle());
			login.setText(ticket.getLogin());
			password.setText(ticket.getPassword());
			notes.setText(ticket.getNotes());
		}
	}

    public void onDestroy() {
    	super.onDestroy();
    	if (dbHelper != null) {
    		dbHelper.close();
    	}
    }
}
