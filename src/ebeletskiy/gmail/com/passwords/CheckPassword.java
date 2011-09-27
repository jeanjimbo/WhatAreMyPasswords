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

public class CheckPassword extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.check_password);
    }

    public void onButtonClick(View v) {
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
}
