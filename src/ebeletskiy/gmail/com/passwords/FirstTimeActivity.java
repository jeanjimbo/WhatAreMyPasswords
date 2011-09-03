package ebeletskiy.gmail.com.passwords;

import ebeletskiy.gmail.com.passwords.utils.MyConfigs;
import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;

public class FirstTimeActivity extends Activity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.first_time_screen);
	}
	
	@Override
	public void onStart() {
		super.onStart();
		SharedPreferences sharedPreferences = getSharedPreferences(MyConfigs.PREFS_NAME, 0);
		SharedPreferences.Editor editor = sharedPreferences.edit();
		editor.putBoolean(MyConfigs.FIRST_RUN, true);
		editor.commit();
	}
}
