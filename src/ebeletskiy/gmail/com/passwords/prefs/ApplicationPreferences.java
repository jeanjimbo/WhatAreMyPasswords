package ebeletskiy.gmail.com.passwords.prefs;

import java.util.List;

import ebeletskiy.gmail.com.passwords.R;
import ebeletskiy.gmail.com.passwords.R.xml;

import android.preference.PreferenceActivity;

public class ApplicationPreferences extends PreferenceActivity {

	@Override
	public void onBuildHeaders(List<Header> target) {
		loadHeadersFromResource(R.xml.preference_headers, target);
	}
}
