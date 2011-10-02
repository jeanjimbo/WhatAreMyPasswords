package ebeletskiy.gmail.com.passwords.usage;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import ebeletskiy.gmail.com.passwords.interfaces.StartNewActivity;
import ebeletskiy.gmail.com.passwords.interfaces.StartNewActivityForResult;
import ebeletskiy.gmail.com.passwords.usage.R;
import ebeletskiy.gmail.com.passwords.usage.utils.MyConfigs;

public class ParentActivity extends Activity implements StartNewActivity, StartNewActivityForResult {
    private static final String TAG = "Parent Activity";

    public int mLayout = -1;
    public boolean checkPassword = false;
    public SharedPreferences mSharedPreferences;
    public Editor mPrefsEditor;
    public Handler mHandler;
    public static boolean invokedByNewActivityRun = false;

    public Runnable finishRunnable = new Runnable() {

        @Override
        public void run() {
            if (MyConfigs.DEBUG)
                Log.i(TAG, "Runnable executed.");
            finish();
        }
    };

    public ParentActivity(int mLayout) {
        this.mLayout = mLayout;
    }

    public ParentActivity() {
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (mLayout != -1) {
            setContentView(mLayout);
        } else {
            throw new NullPointerException(
                    "onCreate(): setContentView(mLayout) where mLayout is -1");
        }

        mSharedPreferences = getSharedPreferences(MyConfigs.PREFS_NAME, 0);
        mPrefsEditor = mSharedPreferences.edit();
    }

    @Override
    public void onStart() {
        super.onStart();

        myOnStart();
    }

    public void myOnStart() {
        if (MyConfigs.DEBUG)
            Log.i(TAG, "myOnStart()");
        if (mHandler != null) {
            if (MyConfigs.DEBUG)
                Log.i(TAG, "onStart(), handler != null");
            mHandler.removeCallbacks(finishRunnable);
        }

        checkPassword = mSharedPreferences.getBoolean(MyConfigs.ORIENTATION_CHANGE, false);

        if (checkPassword) {
            // do not check for password
            if (MyConfigs.DEBUG)
                Log.i(TAG, "onStart(), do not check password");
            mPrefsEditor.putBoolean(MyConfigs.ORIENTATION_CHANGE, false).commit();
        } else {
            if (MyConfigs.DEBUG) {
                Log.i(TAG, "onStart(): check password.");
            }

            if (!invokedByNewActivityRun) {
                if (MyConfigs.DEBUG) {
                    Log.i(TAG, "onStart(): invokeByNewActivityRun = " + invokedByNewActivityRun);
                }

                startActivity(new Intent(this, CheckPassword.class));
                setInvokedByNewActivityRun(false);
                finish();
            }
        }
    }

    @Override
    public Object onRetainNonConfigurationInstance() {
        if (MyConfigs.DEBUG)
            Log.i(TAG, "onRetainCOnfigurationInstance");
        mPrefsEditor.putBoolean(MyConfigs.ORIENTATION_CHANGE, true);
        mPrefsEditor.commit();
        return null;
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (MyConfigs.DEBUG)
            Log.i(TAG, "onStop()");
        if (!invokedByNewActivityRun) {
            if (MyConfigs.DEBUG)
                Log.i(TAG, "onStop(): invokedByNewActitivyRun is false -> starting finishRunnable");
            mHandler = new Handler();
            mHandler.postDelayed(finishRunnable, MyConfigs.DESTROY_APP_AFTER);
        } else {
            if (MyConfigs.DEBUG)
                Log.i(TAG,
                        "onStop(): invokedByNewActitivyRun is true -> do NOT starting finishRunnable");
        }
    }

    @Override
    protected void onDestroy() {
        if (MyConfigs.DEBUG)
            Log.i(TAG, "onDestroy()");
        super.onDestroy();
        if (mHandler != null) {
            mHandler.removeCallbacks(finishRunnable);
        }
    }

    public void setInvokedByNewActivityRun(boolean value) {
        invokedByNewActivityRun = value;
    }

    @Override
    public void startNewActivity(Intent i) {
        setInvokedByNewActivityRun(true);
        // TODO implement this
    }

    @Override
    public void startNewActivityForResult(Intent intent, int requestCode) {
        setInvokedByNewActivityRun(true);
        startActivityForResult(intent, requestCode);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.i(TAG, "onActivityResult()");
        if (requestCode == MyConfigs.NEW_ITEM_PASSWORD_REQUEST_CODE) {
            ((NewItem) getFragmentManager().findFragmentById(R.id.right_frag)).onActivityResult(
                    requestCode, resultCode, data);
        }
    }

}
