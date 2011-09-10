package ebeletskiy.gmail.com.passwords.utils;

import android.content.Context;
import android.view.Gravity;
import android.widget.Toast;

public class ShowToast {
	
	public static void showToast(Context ctxt, String text) {
		Toast t = Toast.makeText(ctxt, text, Toast.LENGTH_SHORT);
		t.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.CENTER, 0, 0);
		t.show();
	}
}
