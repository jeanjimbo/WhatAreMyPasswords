package ebeletskiy.gmail.com.passwords;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import ebeletskiy.gmail.com.passwords.R;
import ebeletskiy.gmail.com.passwords.R.id;
import ebeletskiy.gmail.com.passwords.R.layout;
import ebeletskiy.gmail.com.passwords.interfaces.SaveItemListener;
import ebeletskiy.gmail.com.passwords.models.Ticket;
import ebeletskiy.gmail.com.passwords.utils.DBHelper;

public class EditItem extends Fragment {
	private static final String TAG = "EditItem";
	
	DBHelper dbHelper;
	SaveItemListener saveItemListener;

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		saveItemListener = (SaveItemListener)activity;
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
		Button saveItemButton = (Button)getView().findViewById(R.id.bt_save_item);
		saveItemButton.setOnClickListener(onSaveClickListener);
	}
	
	
	View.OnClickListener onSaveClickListener = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			dbHelper.insert( createTicket() );
			
			if (saveItemListener != null) {
				saveItemListener.saveItemList();
			}
		}
	};
	
	
	private Ticket createTicket() {
		Ticket ticket = new Ticket();
		
		EditText title = (EditText)getView().findViewById(R.id.et_title);
		EditText login = (EditText)getView().findViewById(R.id.et_login_data);
		EditText password = (EditText)getView().findViewById(R.id.et_password_data);
		EditText notes = (EditText)getView().findViewById(R.id.et_notes_data);
		
		ticket.setTitle( (title.getText()).toString() );
		ticket.setLogin( (login.getText()).toString() );
		ticket.setPassword( (password.getText()).toString() );
		ticket.setNotes( (notes.getText()).toString() );
		
		return ticket;
	}
}
