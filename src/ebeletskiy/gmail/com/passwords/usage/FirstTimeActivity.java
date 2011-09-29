package ebeletskiy.gmail.com.passwords.usage;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import ebeletskiy.gmail.com.passwords.usage.utils.MyConfigs;
import ebeletskiy.gmail.com.passwords.usage.utils.ShowToast;

public class FirstTimeActivity extends Activity {

    private String mPassword;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.first_time_screen);
    }

    public void onButtonClick(View v) {
        if (checkFields()) {
            savePassword();
            updateSharedPreferences();
            launchMainActivity();
        } else {
            ShowToast.showToast(this, getString(R.string.fill_both_fields_passwords_match));
        }
    }

    private void updateSharedPreferences() {
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
}
