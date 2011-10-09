package ebeletskiy.gmail.com.passwords.activities;

import android.app.Activity;
import android.app.DialogFragment;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import ebeletskiy.gmail.com.passwords.R;
import ebeletskiy.gmail.com.passwords.fragments.PasswordAttemptsDialog;
import ebeletskiy.gmail.com.passwords.fragments.UserDataDeletedDialog;
import ebeletskiy.gmail.com.passwords.utils.DBHelper;
import ebeletskiy.gmail.com.passwords.utils.MyConfigs;

public class CheckPassword extends Activity {

    public static final String TAG = "CheckPassword.java";
    private EditText mEdtPassword;
    private String mUserPassword = null;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor prefsEditor;
    private DBHelper dbHelper;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.check_password);
        sharedPreferences = getSharedPreferences(MyConfigs.PREFS_NAME, 0);
        prefsEditor = sharedPreferences.edit();
        initUserPassword();
        mEdtPassword = (EditText) findViewById(R.id.edt_checkpassword_password);
        mEdtPassword.setOnKeyListener(onSoftKeyboardDonePress);
    }

    private void incrementIncorrectPasswordAttempts() {
        int tmpPasswordAttempts = sharedPreferences
                .getInt(MyConfigs.INCORRECT_PASSWORD_ATTEMPTS, 0);
        tmpPasswordAttempts++;
        prefsEditor.putInt(MyConfigs.INCORRECT_PASSWORD_ATTEMPTS, tmpPasswordAttempts).commit();
    }

    private int getIncorrectPasswordAttempts() {
        int tmp = sharedPreferences.getInt(MyConfigs.INCORRECT_PASSWORD_ATTEMPTS, 0);
        return tmp;
    }

    private void initUserPassword() {
        mUserPassword = sharedPreferences.getString(MyConfigs.USER_PASSWORD, "");
    }

    public void onButtonClick(View v) {
        login();
    }

    public void login() {
        String providedPassword = ((EditText) findViewById(R.id.edt_checkpassword_password))
                .getText().toString();

        if (providedPassword.equals(mUserPassword)) {
            setFlagAppStartsFirstTime();
            launchMainActivity();
        } else {
            incrementIncorrectPasswordAttempts();
            if (getIncorrectPasswordAttempts() == getAllowedPasswordAttempts()) {
                clearUserDatabase();
                resetUserSettings();
                notifyUserDataDeleted();
                startAppFromBegining();
            } else {
                showAttemptsLeftAlert();
            }
        }
    }

    private void notifyUserDataDeleted() {
        new UserDataDeletedDialog().show(getFragmentManager(), "tag");
    }

    private void showAttemptsLeftAlert() {
        int attemptsLeft = getAllowedPasswordAttempts() - getIncorrectPasswordAttempts();
        new PasswordAttemptsDialog(attemptsLeft).show(getFragmentManager(), "tag");
    }

    private void clearUserDatabase() {
        dbHelper = new DBHelper(getApplicationContext());
        dbHelper.deleteAll();
    }

    private void resetUserSettings() {
        prefsEditor.clear().commit();
    }

    private void startAppFromBegining() {
        startActivity(new Intent(this, LaunchActivityManager.class));
    }

    private int getAllowedPasswordAttempts() {
        return sharedPreferences.getInt(MyConfigs.INCORRECT_PASSWORD_ALLOWED_ATTEMPTS,
                MyConfigs.INCORRECT_PASSWORD_ALLOWED_ATTEMPTS_DEFAULT);
    }

    private void setFlagAppStartsFirstTime() {
        prefsEditor.putBoolean(MyConfigs.FIRST_RUN_MAIN, true);
        prefsEditor.commit();
    }

    private void launchMainActivity() {
        Intent i = new Intent(this, MainActivity.class);
        startActivity(i);
        finish();
    }

    public View.OnKeyListener onSoftKeyboardDonePress = new View.OnKeyListener() {
        public boolean onKey(View v, int keyCode, KeyEvent event) {
            if (event.getKeyCode() == KeyEvent.KEYCODE_ENTER) {
                if (KeyEvent.ACTION_UP == event.getAction()) {
                    login();
                    return true;
                }
            }
            return false;
        }
    };

    public void onDestroy() {
        super.onDestroy();
        Log.i(TAG, "onDestroy()");
        if (dbHelper != null) {
            dbHelper.close();
        }
    }

}
