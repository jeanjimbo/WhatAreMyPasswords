package ebeletskiy.gmail.com.passwords;

import android.app.ActionBar;
import android.app.Activity;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.widget.TextView;
import ebeletskiy.gmail.com.passwords.models.Ticket;

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
//		BitmapDrawable background = new BitmapDrawable(
//				BitmapFactory.decodeResource(getResources(),
//						R.drawable.actionbar_back));
//		background.setTileModeX(android.graphics.Shader.TileMode.REPEAT);
//		actionBar.setBackgroundDrawable(background);
        actionBar.setTitle("What are my passwords?");
	}

	@Override
	public void showTicket(Ticket ticket) {
		ItemsDescription itemsDescription = ((ItemsDescription)getFragmentManager().findFragmentById(R.id.right_frag));
		TextView title = (TextView)itemsDescription.getView().findViewById(R.id.tv_title);
	    title.setText(ticket.getTitle());
	    
	    TextView login = (TextView)itemsDescription.getView().findViewById(R.id.tv_login_data);
	    login.setText(ticket.getLogin());
		
	    TextView password = (TextView)itemsDescription.getView().findViewById(R.id.tv_password_data);
	    password.setText(ticket.getPassword());
	
	    TextView notes = (TextView)itemsDescription.getView().findViewById(R.id.tv_notes_data);
	    notes.setText(ticket.getNotes());
//		itemsDescription.update(ticket);
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
	    inflater.inflate(R.menu.menu, menu);
	    return true;
	}
}