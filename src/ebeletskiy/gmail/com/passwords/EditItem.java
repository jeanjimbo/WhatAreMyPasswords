package ebeletskiy.gmail.com.passwords;

import android.os.Bundle;
import android.view.Gravity;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;
import ebeletskiy.gmail.com.passwords.models.Ticket;
import ebeletskiy.gmail.com.passwords.utils.DBHelper;

public class EditItem extends NewItem {
	
	private Ticket ticket;
	
	public EditItem() {};
	
	public EditItem(Ticket ticket) {
		this.ticket = ticket;
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		initUIelements();
	}
	
	private void initUIelements() {
		title = (EditText)getView().findViewById(R.id.et_title);
		title.setText(ticket.getTitle());
		
		login = (EditText)getView().findViewById(R.id.et_login_data);
		login.setText(ticket.getLogin());
		
		password = (EditText)getView().findViewById(R.id.et_password_data);
		password.setText(ticket.getTitle());
		
		notes = (EditText)getView().findViewById(R.id.et_notes_data);
		notes.setText(ticket.getTitle());
	}
	
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		
		switch (item.getItemId()) {
			case R.id.save_item: 
			
				dbHelper.insert( createTicket() );
				
				if (saveItemListener != null && checkFields()) {
					saveItemListener.saveItem();
					showToast("Ticket has been updated");
				} else {
					showToast("Please fill all mandatory fields");
				}
				
			break;
		}
		return true;
	}
	
	private void showToast(String string) {
		Toast t = Toast.makeText(getActivity(), string,
				Toast.LENGTH_SHORT);
		t.setGravity(Gravity.CENTER_HORIZONTAL|Gravity.CENTER, 0, 0);
		t.show();
	}
	
}
