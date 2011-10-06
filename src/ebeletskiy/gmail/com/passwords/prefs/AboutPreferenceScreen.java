package ebeletskiy.gmail.com.passwords.prefs;

import android.content.Context;
import android.preference.Preference;
import android.util.AttributeSet;
import ebeletskiy.gmail.com.passwords.R;

public class AboutPreferenceScreen extends Preference {

    public AboutPreferenceScreen(Context context, AttributeSet attrs) {
        super(context, attrs);
        setLayoutResource(R.layout.about_preference_screen);
    }

}
