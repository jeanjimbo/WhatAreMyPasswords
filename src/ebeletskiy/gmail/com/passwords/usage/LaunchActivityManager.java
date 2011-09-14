package ebeletskiy.gmail.com.passwords.usage;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import ebeletskiy.gmail.com.passwords.usage.utils.MyConfigs;

public class LaunchActivityManager extends Activity {
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		SharedPreferences sharedPreferences = getSharedPreferences(MyConfigs.PREFS_NAME, 0);
		if( sharedPreferences.getBoolean(MyConfigs.FIRST_RUN, true) ) {
			runFirstTimeActivity();
			finish();
		} else {
			runMainActivity();
			finish();
		}
	}
	
	public void runMainActivity() {
		Intent i = new Intent(this, MainActivity.class);
		startActivity(i);
	}
	
	public void runFirstTimeActivity() {
		Intent i = new Intent(this, FirstTimeActivity.class);
		startActivity(i);
	}
	
}
