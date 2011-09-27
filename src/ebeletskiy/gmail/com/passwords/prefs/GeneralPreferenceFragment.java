package ebeletskiy.gmail.com.passwords.prefs;

import ebeletskiy.gmail.com.passwords.R;
import ebeletskiy.gmail.com.passwords.R.xml;
import android.os.Bundle;
import android.preference.PreferenceFragment;

public class GeneralPreferenceFragment extends PreferenceFragment {
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.general_preferences);
	}
}
