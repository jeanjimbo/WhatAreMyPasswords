package ebeletskiy.gmail.com.passwords.activities;

import android.app.Activity;
import android.app.DialogFragment;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
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
    private IncorrectPasswordManager passwordManager;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.check_password);
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        prefsEditor = sharedPreferences.edit();
        mUserPassword = sharedPreferences.getString(MyConfigs.USER_PASSWORD, "");
        passwordManager = new IncorrectPasswordManager(this);

        mEdtPassword = (EditText) findViewById(R.id.edt_checkpassword_password);
        mEdtPassword.setOnKeyListener(onSoftKeyboardDonePress);
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
            passwordManager.start();
        }
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

}
