package ebeletskiy.gmail.com.passwords;

import android.app.Activity;
import android.app.ListFragment;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.CursorAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import ebeletskiy.gmail.com.passwords.R.style;
import ebeletskiy.gmail.com.passwords.interfaces.AddNewItemListener;
import ebeletskiy.gmail.com.passwords.interfaces.DeleteItemListener;
import ebeletskiy.gmail.com.passwords.interfaces.EditItemListener;
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
	private AddNewItemListener newItemBtnListener;
	private DeleteItemListener deleteListener;
	private EditItemListener editItemListener;
	private Ticket ticket;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		dbHelper = new DBHelper(getActivity());
		
		if (savedInstanceState == null) {
			setHasOptionsMenu(true);
		}
		
//		fillDB(); 
		
		cursor = dbHelper.getAll();
		mAdapter = new MAdapter(getActivity(), cursor, true);

		setListAdapter(mAdapter);
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);		
		
		getListView().setOnItemLongClickListener(new OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View view,
					int position, long id) {
				ticket = createView(view);
				getListView().setItemChecked(position, true);
				getActivity().startActionMode(mContentSelectionActionModeCallback);
				return true;
			}
		});
	}
	
	
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		
		itemClickListener = (ListItemClickListener)activity;
		newItemBtnListener = (AddNewItemListener)activity;
		deleteListener = (DeleteItemListener)activity;
		editItemListener = (EditItemListener)activity;
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
			itemClickListener.itemClicked(ticket);
		}
	}
	
	public void enablePersistentSelection() {
		getListView().setChoiceMode(ListView.CHOICE_MODE_SINGLE);
	}
	
	private Ticket createView(View v) {
		ViewHolder viewHolder = (ViewHolder)v.getTag(); 
		Cursor cursor = dbHelper.getItem(viewHolder.getId());
		return ticket = DataConverter.convertToTicket(cursor);
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
			View row = null;
			LayoutInflater inflater=((Activity)ctxt).getLayoutInflater();
			if (c.getInt(0) % 2 == 0) {
				row=inflater.inflate(R.layout.row, parent, false);
			} else {
				row=inflater.inflate(R.layout.row_gray, parent, false);
			}
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
		getListView().setItemChecked(-1, true);
		Log.i(TAG, "refresh()");
	}
	
	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
	    inflater.inflate(R.menu.menu_items_list, menu);
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		
		switch (item.getItemId()) {
			case R.id.add_item:
				if (newItemBtnListener != null) {
					newItemBtnListener.onAddNewItem();
				}
			break;
		}
		return super.onOptionsItemSelected(item);
	}


	private ActionMode.Callback mContentSelectionActionModeCallback = new ActionMode.Callback() {
        public boolean onCreateActionMode(ActionMode actionMode, Menu menu) {
            actionMode.setTitle("What you want to do with the item?");

            MenuInflater inflater = getActivity().getMenuInflater();
            inflater.inflate(R.menu.menu_contex_item_long_click, menu);
            return true;
        }

        public boolean onPrepareActionMode(ActionMode actionMode, Menu menu) {
            return false;
        }

        public boolean onActionItemClicked(ActionMode actionMode, MenuItem menuItem) {
            switch (menuItem.getItemId()) {
                case R.id.edit_item:
                	editItemListener.loadEditItem( ticket );
                    actionMode.finish();
                    return true;
                case R.id.delete_item:
                	dbHelper.deleteRow(ticket.getId());
    				deleteListener.onDeleteItem();
                    actionMode.finish();
                    return true;
            }
            return false;
        }

        public void onDestroyActionMode(ActionMode actionMode) {
        }
    };
}
