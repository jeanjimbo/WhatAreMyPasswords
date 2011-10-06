package ebeletskiy.gmail.com.passwords.prefs;

import android.os.Bundle;
import android.preference.PreferenceFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import ebeletskiy.gmail.com.passwords.R;

public class AboutPreferenceFragment extends PreferenceFragment {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.about_preferences);
	}
	
}
