package ebeletskiy.gmail.com.passwords.ui;

import android.app.ActionBar;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.widget.TextView;
import ebeletskiy.gmail.com.passwords.R;
import ebeletskiy.gmail.com.passwords.R.id;
import ebeletskiy.gmail.com.passwords.R.layout;
import ebeletskiy.gmail.com.passwords.R.menu;
import ebeletskiy.gmail.com.passwords.interfaces.AddNewItemBtnListener;
import ebeletskiy.gmail.com.passwords.interfaces.ListItemClickListener;
import ebeletskiy.gmail.com.passwords.interfaces.SaveItemListener;
import ebeletskiy.gmail.com.passwords.models.Ticket;
import ebeletskiy.gmail.com.passwords.utils.DBHelper;

public class WhatAreMyPasswordsActivity extends Activity implements 
												ListItemClickListener, 
												AddNewItemBtnListener,
												SaveItemListener{
	private static final String TAG = "WhatAreMyPasswordsActivity";
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        initUI();
        addDynamicFragment();
//        DBHelper db = new DBHelper(this);
//        db.deleteAll();
        
        
        ((ItemsList)getFragmentManager().findFragmentById(R.id.left_frag))
        	.enablePersistentSelection();
    }

    private void addDynamicFragment() {
        Fragment fg = ItemsDescription.newInstance();
        getFragmentManager().beginTransaction().add(R.id.right_frag, fg)
        	.commit();
    }
    
	private void initUI() {
        ActionBar actionBar = getActionBar();
        actionBar.setTitle("What are my passwords?");
	}

	@Override
	public void showTicket(Ticket ticket) {
		ItemsDescription itemsDescription = ((ItemsDescription)getFragmentManager().findFragmentById(R.id.right_frag));
		TextView title = (TextView)itemsDescription.getView().findViewById(R.id.tv_title);
	    title.setText(ticket.getTitle());
	    
	    TextView login = (TextView)itemsDescription.getView().findViewById(R.id.tv_login_data);
	    login.setText(ticket.getLogin());
		
	    TextView password = (TextView)itemsDescription.getView().findViewById(R.id.tv_password_data);
	    password.setText(ticket.getPassword());
	
	    TextView notes = (TextView)itemsDescription.getView().findViewById(R.id.tv_notes_data);
	    notes.setText(ticket.getNotes());
//		itemsDescription.update(ticket);
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
	    inflater.inflate(R.menu.menu, menu);
	    return true;
	}

	@Override
	public void onButtonClick() {
		Fragment newFragment = new EditItem();
		FragmentTransaction transaction = getFragmentManager().beginTransaction();
		transaction.replace(R.id.right_frag, newFragment);
		transaction.addToBackStack(null);
		transaction.commit();
	}

	@Override
	public void refreshItemsList() {
		((ItemsList)getFragmentManager().findFragmentById(R.id.left_frag)).
    		refresh();
	}
}