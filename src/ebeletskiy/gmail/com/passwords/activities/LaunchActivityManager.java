package ebeletskiy.gmail.com.passwords.activities;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import ebeletskiy.gmail.com.passwords.utils.MyConfigs;

public class LaunchActivityManager extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPreferences sharedPreferences = PreferenceManager
                .getDefaultSharedPreferences(this);
        if (sharedPreferences.getBoolean(MyConfigs.FIRST_RUN, true)) {
            runFirstTimeActivity();
            finish();
        } else {
            runMainActivity();
            finish();
        }
    }

    public void runMainActivity() {
        startActivity(new Intent(this, MainActivity.class));
    }

    public void runFirstTimeActivity() {
        startActivity(new Intent(this, FirstTimeActivity.class));
    }

}
