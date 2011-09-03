package ebeletskiy.gmail.com.passwords;

import android.app.ActionBar;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import ebeletskiy.gmail.com.passwords.interfaces.AddNewItemListener;
import ebeletskiy.gmail.com.passwords.interfaces.DeleteItemListener;
import ebeletskiy.gmail.com.passwords.interfaces.EditItemListener;
import ebeletskiy.gmail.com.passwords.interfaces.ListItemClickListener;
import ebeletskiy.gmail.com.passwords.interfaces.SaveItemListener;
import ebeletskiy.gmail.com.passwords.models.Ticket;
import ebeletskiy.gmail.com.passwords.utils.MyConfigs;

public class MainActivity extends Activity implements 
												ListItemClickListener, 
												AddNewItemListener,
												SaveItemListener,
												DeleteItemListener,
												EditItemListener {
	private static final String TAG = "MainActivity";
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        initActionBar();
        if (savedInstanceState == null) {
            addEmptyFragment();
        }
        
        ((ItemsList)getFragmentManager().findFragmentById(R.id.left_frag))
        	.enablePersistentSelection();
    }

    
    @Override
	public void onStart() {
    	super.onStart();
    	
    	SharedPreferences sharedPreferences = getSharedPreferences(MyConfigs.PREFS_NAME, 0);
    	
    	if( sharedPreferences.getBoolean(MyConfigs.FIRST_RUN_MAIN, true) ) {
    		updateSharedPreferences();
		} else {
			Intent i = new Intent(this, CheckPassword.class);
			startActivity(i);
			finish();
		}
	}
   
	private void updateSharedPreferences() {
		SharedPreferences sharedPreferences = 
			getSharedPreferences(MyConfigs.PREFS_NAME, 0);
		SharedPreferences.Editor editor = sharedPreferences.edit();
		editor.putBoolean(MyConfigs.FIRST_RUN_MAIN, false);
		editor.commit();
	}
    
    @Override
    protected void onStop() {
    	super.onStop();
    	//TODO: implement me
    }


	private void addEmptyFragment() {
        Fragment fg = new EmptyRightFrag();
        getFragmentManager().beginTransaction().add(R.id.right_frag, fg)
        	.commit();
    }
    
    
	private void initActionBar() {
        ActionBar actionBar = getActionBar();
        actionBar.setTitle("What are my passwords?");
		actionBar.setBackgroundDrawable(getResources().getDrawable(R.drawable.action_bar_background));
	}

	
	@Override
	public void itemClicked(Ticket ticket) {
		Fragment newFragment = new ItemsDescription(ticket);
		loadFragment(newFragment, false);
	}
	
	
	@Override
	public void onAddNewItem() {
		Fragment newFragment = new NewItem();
		loadFragment(newFragment, true);
	}

	
	@Override
	public void saveItem() {
		refreshList();
		
		Fragment newFragment = new EmptyRightFrag();
		loadFragment(newFragment, true);
	}

	
	@Override
	public void onDeleteItem() {
		refreshList();
		
		Fragment newFragment = new EmptyRightFrag();
		loadFragment(newFragment, false);
	}
	
	
	@Override
	public void loadEditItem(Ticket ticket) {
		Fragment newFragment = new EditItem(ticket);
		loadFragment(newFragment, true);
	}

	
	private void refreshList() {
		((ItemsList)getFragmentManager().findFragmentById(R.id.left_frag)).
		refresh();
	}
	
	
	private void loadFragment(Fragment fragment, boolean animation) {
		FragmentTransaction transaction = getFragmentManager().beginTransaction();
		if (animation) {
			transaction.setCustomAnimations(R.anim.slide_in_left, R.anim.slide_out_right);
		}
		transaction.replace(R.id.right_frag, fragment);
		transaction.addToBackStack(null);
		transaction.commit();
	}
	
}