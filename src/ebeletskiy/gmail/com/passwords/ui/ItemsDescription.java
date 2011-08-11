package ebeletskiy.gmail.com.passwords.ui;

import ebeletskiy.gmail.com.passwords.R;
import ebeletskiy.gmail.com.passwords.R.layout;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class ItemsDescription extends Fragment {
	private static final String TAG = "ItemsDescription";
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return inflater.inflate(R.layout.items_description, container, false);
	}

	public static Fragment newInstance() {
		return new ItemsDescription();
	}
}
