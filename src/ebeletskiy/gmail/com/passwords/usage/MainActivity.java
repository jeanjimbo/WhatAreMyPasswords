package ebeletskiy.gmail.com.passwords.usage;

import android.app.ActionBar;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import ebeletskiy.gmail.com.passwords.usage.interfaces.AddNewItemListener;
import ebeletskiy.gmail.com.passwords.usage.interfaces.DeleteItemListener;
import ebeletskiy.gmail.com.passwords.usage.interfaces.EditItemListener;
import ebeletskiy.gmail.com.passwords.usage.interfaces.ListItemClickListener;
import ebeletskiy.gmail.com.passwords.usage.interfaces.SaveItemListener;
import ebeletskiy.gmail.com.passwords.usage.models.Ticket;
import ebeletskiy.gmail.com.passwords.usage.prefs.ApplicationPreferences;
import ebeletskiy.gmail.com.passwords.usage.utils.MyConfigs;
import ebeletskiy.gmail.com.passwords.usage.prefs.ApplicationPreferences;

public class MainActivity extends Activity implements ListItemClickListener, AddNewItemListener,
        SaveItemListener, DeleteItemListener, EditItemListener {
    private static final String TAG = "MainActivity";

    protected boolean fromOrientation = false;
    protected SharedPreferences mSharedPreferences;
    protected Editor mPrefsEditor;

    protected Handler mHandler;
    protected Runnable finishRunnable = new Runnable() {

        @Override
        public void run() {
            mPrefsEditor = getApplicationContext().getSharedPreferences(MyConfigs.PREFS_NAME, 0)
                    .edit();
            mPrefsEditor.putBoolean("finishThread", true).commit();

            finish();
        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        initActionBar();
        if (savedInstanceState == null) {
            addEmptyFragment();
        }

        mSharedPreferences = getSharedPreferences(MyConfigs.PREFS_NAME, 0);
        mPrefsEditor = mSharedPreferences.edit();

        ((ItemsList) getFragmentManager().findFragmentById(R.id.left_frag))
                .enablePersistentSelection();

    }

    @Override
    public void onStart() {
        super.onStart();

        if (mHandler != null) {
            mHandler.removeCallbacks(finishRunnable);
            mPrefsEditor.putBoolean("finishThread", false).commit();
        }

        if (mSharedPreferences.getBoolean(MyConfigs.FIRST_RUN_MAIN, true)) {
            updateSharedPreferences();
        } else {
            fromOrientation = mSharedPreferences.getBoolean("fromOrient", false);

            if (fromOrientation) {
                // do not check for password
                mPrefsEditor.putBoolean("fromOrient", false).commit();
            } else {
                startActivity(new Intent(this, CheckPassword.class));
                finish();
            }
        }
    }

    @Override
    public Object onRetainNonConfigurationInstance() {
        mPrefsEditor.putBoolean("fromOrient", true);
        mPrefsEditor.commit();
        return null;
    }

    @Override
    protected void onStop() {
        super.onStop();
        mHandler = new Handler();
        mHandler.postDelayed(finishRunnable, MyConfigs.DESTROY_APP_AFTER);
        mPrefsEditor.putBoolean("finishThread", true).commit();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mHandler != null) {
            mHandler.removeCallbacks(finishRunnable);
            mPrefsEditor.putBoolean("finishThread", false).commit();
        }
    }

    private void updateSharedPreferences() {
        SharedPreferences sharedPreferences = getSharedPreferences(MyConfigs.PREFS_NAME, 0);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(MyConfigs.FIRST_RUN_MAIN, false);
        editor.commit();
    }

    private void addEmptyFragment() {
        Fragment fg = new EmptyRightFrag();
        getFragmentManager().beginTransaction().add(R.id.right_frag, fg).commit();
    }

    private void initActionBar() {
        ActionBar actionBar = getActionBar();
        actionBar.setBackgroundDrawable(getResources()
                .getDrawable(R.drawable.action_bar_background));
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
        ((ItemsList) getFragmentManager().findFragmentById(R.id.left_frag)).refresh();
    }

    private void loadFragment(Fragment fragment, boolean animation) {
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        if (animation) {
            transaction.setCustomAnimations(R.anim.slide_in_left, R.anim.slide_out_right);
        }
        transaction.replace(R.id.right_frag, fragment);
        transaction.commit();
    }

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.menu_main_activity, menu);
		return true;
	}

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
        case android.R.id.home:
            Intent intent = new Intent(this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
			return true;
		case R.id.show_preferences:
			startActivity(new Intent(this, ApplicationPreferences.class));
            return true;
        default:
            return super.onOptionsItemSelected(item);
        }
    }
}