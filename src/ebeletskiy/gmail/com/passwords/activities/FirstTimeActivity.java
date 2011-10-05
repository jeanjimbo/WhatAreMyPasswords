package ebeletskiy.gmail.com.passwords.activities;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import ebeletskiy.gmail.com.passwords.R;
import ebeletskiy.gmail.com.passwords.R.id;
import ebeletskiy.gmail.com.passwords.R.layout;
import ebeletskiy.gmail.com.passwords.R.string;
import ebeletskiy.gmail.com.passwords.utils.MyConfigs;
import ebeletskiy.gmail.com.passwords.utils.ShowToast;

public class FirstTimeActivity extends Activity {

    private String mPassword;
    private EditText edtPassword;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.first_time_screen);
        edtPassword = (EditText) findViewById(R.id.edt_firsttimeactivity_second_field);
        edtPassword.setOnKeyListener(onSoftKeyboardDonePress);
    }
    
    public void onButtonClick(View v) {
       processNewAccountCreation();
    }
    
    private void processNewAccountCreation() {
        if (checkFields()) {
            savePassword();
            markAsAccountCreated();
            launchMainActivity();
        } else {
            ShowToast.showToast(this, getString(R.string.fill_both_fields_passwords_match));
        }
    }

    private void markAsAccountCreated() {
        SharedPreferences sharedPreferences = getSharedPreferences(MyConfigs.PREFS_NAME, 0);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(MyConfigs.FIRST_RUN, false);
        editor.commit();
    }

    private void savePassword() {
        if (mPassword.equals("")) {
            throw new IllegalArgumentException(getString(R.string.password_field_is_empty));
        }

        SharedPreferences sharedPreferences = getSharedPreferences(MyConfigs.PREFS_NAME, 0);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(MyConfigs.USER_PASSWORD, mPassword);
        editor.commit();
    }

    private void launchMainActivity() {
        Intent i = new Intent(this, MainActivity.class);
        startActivity(i);
        finish();
    }

    private boolean checkFields() {
        String firstField = ((EditText) findViewById(R.id.edt_firsttimeactivity_first_field))
                .getText().toString();

        String secondField = ((EditText) findViewById(R.id.edt_firsttimeactivity_second_field))
                .getText().toString();

        if (firstField.equals(secondField)) {
            mPassword = firstField;
            return true;
        } else {
            return false;
        }
    }
    
    public View.OnKeyListener onSoftKeyboardDonePress = new View.OnKeyListener() {
        public boolean onKey(View v, int keyCode, KeyEvent event) {
            if (event.getKeyCode() == KeyEvent.KEYCODE_ENTER) {
                if (KeyEvent.ACTION_UP == event.getAction()) {
                    processNewAccountCreation();
                    return true;
                }
            }
            return false;
        }
    };
}
