package ebeletskiy.gmail.com.passwords;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import ebeletskiy.gmail.com.passwords.utils.MyConfigs;
import ebeletskiy.gmail.com.passwords.utils.ShowToast;

public class FirstTimeActivity extends Activity {

    private String password;

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
            ShowToast.showToast(this, "Fields both fields and make sure passwords match.");
        }
    }

    private void updateSharedPreferences() {
        SharedPreferences sharedPreferences = getSharedPreferences(MyConfigs.PREFS_NAME, 0);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(MyConfigs.FIRST_RUN, false);
        editor.commit();
    }

    private void savePassword() {
        if (password.equals("")) {
            throw new IllegalArgumentException("Password field is empty");
        }

        SharedPreferences sharedPreferences = getSharedPreferences(MyConfigs.PREFS_NAME, 0);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(MyConfigs.USER_PASSWORD, password);
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
            password = firstField;
            return true;
        } else {
            return false;
        }
    }
}
