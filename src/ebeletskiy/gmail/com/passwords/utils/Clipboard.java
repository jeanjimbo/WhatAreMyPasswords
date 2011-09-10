package ebeletskiy.gmail.com.passwords.utils;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;

public class Clipboard {
	ClipboardManager clipboard;
	Context ctxt;
	ClipData clip;

	public Clipboard(Context ctxt) {
		this.ctxt = ctxt;
		clipboard = (ClipboardManager) ctxt
				.getSystemService(Context.CLIPBOARD_SERVICE);
	}

	public void copyText(String text) {
		clip = ClipData.newPlainText("pass", text);
		clipboard.setPrimaryClip(clip);
	}
}
