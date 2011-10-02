package ebeletskiy.gmail.com.passwords;

import ebeletskiy.gmail.com.passwords.utils.MyConfigs;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

public class ParentActivity extends Activity {
    private static final String TAG = "Parent Activity";

    public int mLayout = -1;
    public boolean checkPassword = false;
    public SharedPreferences mSharedPreferences;
    public Editor mPrefsEditor;
    public Handler mHandler;

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
            mPrefsEditor.putBoolean(MyConfigs.ORIENTATION_CHANGE, false).commit();
        } else {
            if (MyConfigs.DEBUG)
                Log.i(TAG, "onStart(): like from OrientationChange");
            startActivity(new Intent(this, CheckPassword.class));
            finish();
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
        mHandler = new Handler();
        mHandler.postDelayed(finishRunnable, MyConfigs.DESTROY_APP_AFTER);
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

}
