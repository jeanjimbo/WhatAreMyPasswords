package ebeletskiy.gmail.com.passwords.usage;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import ebeletskiy.gmail.com.passwords.usage.interfaces.SaveItemListener;
import ebeletskiy.gmail.com.passwords.usage.models.Ticket;
import ebeletskiy.gmail.com.passwords.usage.utils.DBHelper;
import ebeletskiy.gmail.com.passwords.usage.utils.ShowToast;

public class NewItem extends Fragment {
	private static final String TAG = "EditItem";

	private boolean menuWasCreated = false;

	DBHelper dbHelper;
	SaveItemListener saveItemListener;

	EditText title;
	EditText login;
	EditText password;
	EditText notes;
	Ticket ticket;

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		saveItemListener = (SaveItemListener) activity;
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
		ticket = new Ticket();
		initUI();
	}

	public void initUI() {
		title = (EditText) getView().findViewById(R.id.et_title);
		login = (EditText) getView().findViewById(R.id.et_login_data);
		password = (EditText) getView().findViewById(R.id.et_password_data);
		notes = (EditText) getView().findViewById(R.id.et_notes_data);
	}

	public Ticket createTicket() {
		if (ticket != null) {

			ticket.setTitle((title.getText()).toString().trim());
			ticket.setLogin((login.getText()).toString().trim());
			ticket.setPassword((password.getText()).toString().trim());
			ticket.setNotes((notes.getText()).toString().trim());

			return ticket;
		} else {
			throw new NullPointerException("ticket is null");
		}

	}

	public boolean checkFields() {
		if (title.getText().toString().equals("")) {
			return false;
		} else {
			return true;
		}
	}

	public boolean isDuplicate(String str) {

		Cursor c = dbHelper.getItemByTitle(str);
		if (c.getCount() == 0) {
			return false;
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

			if (checkFields()) {
				if (!isDuplicate((title.getText()).toString().trim())) {
					createNewItem();
					hideKeyboard();
					saveItemListener.saveItem();
				} else {
					ShowToast.showToast(getActivity(),
							"The item wich such name already exists.");
				}
			} else {
				ShowToast.showToast(getActivity(),
						"Please fill the title.");
			}
			break;
		}
		return super.onOptionsItemSelected(item);
	}

	private void hideKeyboard() {
		InputMethodManager imm = (InputMethodManager) getActivity()
				.getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.hideSoftInputFromWindow(title.getWindowToken(), 0);
	}

	private void createNewItem() {
		dbHelper.insert(createTicket());
	}

	public void onDestroy() {
		super.onDestroy();
		if (dbHelper != null) {
			dbHelper.close();
		}
	}
}