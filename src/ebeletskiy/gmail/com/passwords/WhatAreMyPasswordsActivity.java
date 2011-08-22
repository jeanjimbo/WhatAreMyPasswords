package ebeletskiy.gmail.com.passwords;

import android.app.ActionBar;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.widget.TextView;
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
        addEmptyFragment();
//        DBHelper db = new DBHelper(this);
//        db.deleteAll();
        
        
        ((ItemsList)getFragmentManager().findFragmentById(R.id.left_frag))
        	.enablePersistentSelection();
    }

    private void addEmptyFragment() {
        Fragment fg = EmptyRightFrag.newInstance();
        getFragmentManager().beginTransaction().add(R.id.right_frag, fg)
        	.commit();
    }
    
    private void addItemsDescrItem(Ticket ticket) {
    	Fragment fg = new ItemsDescription(ticket);
        getFragmentManager().beginTransaction().replace(R.id.right_frag, fg, "ttag")
        	.commit();
    }
    
	private void initUI() {
        ActionBar actionBar = getActionBar();
        actionBar.setTitle("What are my passwords?");
	}

	@Override
	public void showTicket(Ticket ticket) {
		addItemsDescrItem(ticket);
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
	public void saveItemList() {
		((ItemsList)getFragmentManager().findFragmentById(R.id.left_frag)).
    		refresh();
		
		Fragment newFragment = new EmptyRightFrag();
		FragmentTransaction transaction = getFragmentManager().beginTransaction();
		transaction.replace(R.id.right_frag, newFragment);
		transaction.addToBackStack(null);
		transaction.commit();
	}
}