package ebeletskiy.gmail.com.passwords.usage.utils;

import android.graphics.Typeface;
import android.widget.TextView;

public class FontManager {
    private static Typeface sTypeface;

    public static void applyHandmadeTypeface(TextView view) {
        applyTypeface(view, "fonts/HandmadeTypewriter.ttf");
    }

    public static void applyTypewriter(TextView view) {
        applyTypeface(view, "fonts/typewriter.ttf");
    }

    private static void applyTypeface(TextView view, String font) {
        if (view != null && !font.equals("")) {
            sTypeface = Typeface.createFromAsset(view.getContext().getAssets(), font);
            view.setTypeface(sTypeface);
        }
    }

}
