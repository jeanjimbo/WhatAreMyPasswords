package ebeletskiy.gmail.com.passwords;

import android.app.ActionBar;
import android.app.Activity;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;

public class WhatAreMyPasswordsActivity extends Activity implements 
												ListItemClickListener{
	private static final String TAG = "WhatAreMyPasswordsActivity";
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        initUI();
        
        ((ItemsList)getFragmentManager().findFragmentById(R.id.left_frag)).
        	enablePersistentSelection();
    }

	private void initUI() {
        ActionBar actionBar = getActionBar();
		BitmapDrawable background = new BitmapDrawable(
				BitmapFactory.decodeResource(getResources(),
						R.drawable.actionbar_back));
		background.setTileModeX(android.graphics.Shader.TileMode.REPEAT);
		actionBar.setBackgroundDrawable(background);
	}

	@Override
	public void itemClicked() {
//		ItemsDescription rf = ((ItemsDescription)getFragmentManager().findFragmentById(R.id.right_frag));
//	    TextView tv = (TextView)rf.getView().findViewById(R.id.textView1);
//	    tv.setText("Updated text");
//		((ItemsDescription)getFragmentManager().findFragmentById(R.id.right_frag)).callMe();
		
	}
}