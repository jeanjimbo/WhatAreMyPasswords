package ebeletskiy.gmail.com.passwords.activities;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import ebeletskiy.gmail.com.passwords.R;
import ebeletskiy.gmail.com.passwords.R.id;
import ebeletskiy.gmail.com.passwords.R.layout;
import ebeletskiy.gmail.com.passwords.R.string;
import ebeletskiy.gmail.com.passwords.utils.MyConfigs;
import ebeletskiy.gmail.com.passwords.utils.ShowToast;

public class CheckPassword extends Activity {

    public static final String TAG = "CheckPassword.java";
    private EditText edtPassword;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (MyConfigs.DEBUG)
            Log.i(TAG, "onCreate()");
        setContentView(R.layout.check_password);
        edtPassword = (EditText) findViewById(R.id.edt_checkpassword_password);
        edtPassword.setOnKeyListener(onSoftKeyboardDonePress);
    }

    public void onButtonClick(View v) {
        login();
    }

    public void login() {
        SharedPreferences sharedPreferences = getSharedPreferences(MyConfigs.PREFS_NAME, 0);
        String userPassword = sharedPreferences.getString(MyConfigs.USER_PASSWORD, "");

        String providedPassword = ((EditText) findViewById(R.id.edt_checkpassword_password))
                .getText().toString();

        if (providedPassword.equals(userPassword)) {
            updateSharedPreferences();
            launchMainActivity();
        } else {
            ShowToast.showToast(this, getString(R.string.incorrect_password));
        }
    }

    private void updateSharedPreferences() {
        SharedPreferences sharedPreferences = getSharedPreferences(MyConfigs.PREFS_NAME, 0);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(MyConfigs.FIRST_RUN_MAIN, true);
        editor.commit();
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
