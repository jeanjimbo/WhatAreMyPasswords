package ebeletskiy.gmail.com.passwords;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
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
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		if (savedInstanceState == null) {
			setHasOptionsMenu(true);
		}
	}
	
	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		 menu.add("Show password").setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
         menu.add("Delete").setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
	}
}
