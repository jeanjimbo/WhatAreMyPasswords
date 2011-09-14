package ebeletskiy.gmail.com.passwords.usage.prefs;

import java.util.List;

import android.preference.PreferenceActivity;
import ebeletskiy.gmail.com.passwords.usage.R;

public class ApplicationPreferences extends PreferenceActivity {

	@Override
	public void onBuildHeaders(List<Header> target) {
		loadHeadersFromResource(R.xml.preference_headers, target);
	}
}
