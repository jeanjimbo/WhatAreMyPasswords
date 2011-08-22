package ebeletskiy.gmail.com.passwords.ui;

import android.app.Activity;
import android.app.ListFragment;
import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CursorAdapter;
import android.widget.ListView;
import android.widget.TextView;
import ebeletskiy.gmail.com.passwords.R;
import ebeletskiy.gmail.com.passwords.R.id;
import ebeletskiy.gmail.com.passwords.R.layout;
import ebeletskiy.gmail.com.passwords.interfaces.AddNewItemBtnListener;
import ebeletskiy.gmail.com.passwords.interfaces.ListItemClickListener;
import ebeletskiy.gmail.com.passwords.models.Ticket;
import ebeletskiy.gmail.com.passwords.utils.DBHelper;
import ebeletskiy.gmail.com.passwords.utils.DataConverter;

public class ItemsList extends ListFragment {
	private static final String TAG = "ItemsList";
	
	private DBHelper dbHelper;
	private MAdapter mAdapter;
	private Cursor cursor;
	private ListItemClickListener itemClickListener;
	private AddNewItemBtnListener newItemBtnListener;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		dbHelper = new DBHelper(getActivity());
		
//		fillDB(); 
		
		cursor = dbHelper.getAll();
		mAdapter = new MAdapter(getActivity(), cursor, true);

		setListAdapter(mAdapter);
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		Button newItemBtn = (Button)getView().findViewById(R.id.bt_add_item);
		newItemBtn.setOnClickListener(onClickListener);
	};
	
	View.OnClickListener onClickListener = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			if (newItemBtnListener != null) {
				newItemBtnListener.onButtonClick();
			}
		}
	};
	
	
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		
		itemClickListener = (ListItemClickListener)activity;
		newItemBtnListener = (AddNewItemBtnListener)activity;
	}
	
	// test method
//	private void fillDB() {
//		for (int i=0; i < 5; i++) {
//			dbHelper.insert("Ebay and PayPal " + i, 
//							"Login " + i, 
//							"Password " + i,
//							"Notes " + i);
//		}
//	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return inflater.inflate(R.layout.items_list, container, false);
	}
	
	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {
		l.setItemChecked(position, true);
		
		ViewHolder viewHolder = (ViewHolder)v.getTag(); 
		Cursor cursor = dbHelper.getItem(viewHolder.getId());
		Ticket ticket = DataConverter.convertToTicket(cursor);
		
		if (itemClickListener != null) {
			itemClickListener.showTicket(ticket);
		}
	}
	
	public void enablePersistentSelection() {
		getListView().setChoiceMode(ListView.CHOICE_MODE_SINGLE);
	}
	
	private class MAdapter extends CursorAdapter {
		
		public MAdapter(Context context, Cursor c, boolean autoRequery) {
			super(context, c, autoRequery);
		}

		@Override
		public void bindView(View row, Context ctxt, Cursor c) {
			ViewHolder holder=(ViewHolder)row.getTag();
			
			holder.populateFrom(c, dbHelper);
		}
		
		@Override
		public View newView(Context ctxt, Cursor c, ViewGroup parent) {
			LayoutInflater inflater=((Activity)ctxt).getLayoutInflater();
			View row=inflater.inflate(R.layout.row, parent, false);
			ViewHolder holder=new ViewHolder(row);
			
			row.setTag(holder);
			
			return(row);
		}
	}

	static class ViewHolder {
		private TextView name;
		private int id;
		
		
		ViewHolder(View row) {
			name = (TextView)row.findViewById(R.id.title);
		}
		
		void populateFrom(Cursor c, DBHelper helper) {
			
			name.setText(helper.getTitle(c));
			id = helper.getId(c);
		}
		
		public int getId() {
			return id;
		}
	}
	
	public void refresh() {
		mAdapter.changeCursor(dbHelper.getAll());
	}
}
