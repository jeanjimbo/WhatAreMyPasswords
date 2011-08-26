package ebeletskiy.gmail.com.passwords;

import android.app.ActionBar;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.util.Log;
import ebeletskiy.gmail.com.passwords.interfaces.AddNewItemListener;
import ebeletskiy.gmail.com.passwords.interfaces.DeleteItemListener;
import ebeletskiy.gmail.com.passwords.interfaces.ListItemClickListener;
import ebeletskiy.gmail.com.passwords.interfaces.SaveItemListener;
import ebeletskiy.gmail.com.passwords.models.Ticket;

public class MainActivity extends Activity implements 
												ListItemClickListener, 
												AddNewItemListener,
												SaveItemListener,
												DeleteItemListener{
	private static final String TAG = "WhatAreMyPasswordsActivity";
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        initActionBar();
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
    
    
	private void initActionBar() {
        ActionBar actionBar = getActionBar();
        actionBar.setTitle("What are my passwords?");
        
        
		BitmapDrawable background = new BitmapDrawable(
		BitmapFactory.decodeResource(getResources(),
				R.drawable.action_bar_background));
		background.setTileModeX(android.graphics.Shader.TileMode.REPEAT);
		actionBar.setBackgroundDrawable(background);
	}

	@Override
	public void itemClicked(Ticket ticket) {
		Fragment newFragment = new ItemsDescription(ticket);
		loadFragment(newFragment);
	}
	
	
	@Override
	public void onAddNewItem() {
		Fragment newFragment = new NewItem();
		loadFragment(newFragment);
	}

	@Override
	public void saveItem() {
		refreshList();
		
		Fragment newFragment = new EmptyRightFrag();
		loadFragment(newFragment);
	}

	@Override
	public void onDeleteItem() {
		refreshList();
		
		Fragment newFragment = new EmptyRightFrag();
		loadFragment(newFragment);
	}
	
	private void refreshList() {
		((ItemsList)getFragmentManager().findFragmentById(R.id.left_frag)).
		refresh();
		
		Log.i(TAG, "refreshList()");
	}
	
	private void loadFragment(Fragment fragment) {
		FragmentTransaction transaction = getFragmentManager().beginTransaction();
		transaction.setCustomAnimations(R.anim.slide_in_left, R.anim.slide_out_right);
		transaction.replace(R.id.right_frag, fragment);
		transaction.addToBackStack(null);
		transaction.commit();
	}
}