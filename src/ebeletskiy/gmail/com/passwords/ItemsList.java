package ebeletskiy.gmail.com.passwords;

import android.app.Activity;
import android.app.ListFragment;
import android.content.Context;
import android.database.Cursor;
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
import android.widget.ListView;
import android.widget.TextView;
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
	private View mView;
	private ActionMode mCurrentActionMode;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		dbHelper = new DBHelper(getActivity());
		
		if (savedInstanceState == null) {
			setHasOptionsMenu(true);
		}
		
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
				mView = view; 
				
				if (mCurrentActionMode != null) {
					return false;
				}
				
				mCurrentActionMode = getActivity().startActionMode(mContentSelectionActionModeCallback);
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
	
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return inflater.inflate(R.layout.items_list, container, false);
	}
	
	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {
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
		public int getViewTypeCount() {
			return 2;
		}
		
		@Override
		public int getItemViewType(int position) {
			if (position % 2 == 0) {
				return 0;
			} else {
				return 1;
			}
		}

		@Override
		public void bindView(View row, Context ctxt, Cursor c) {
			ViewHolder holder=(ViewHolder)row.getTag();
			
			if (c.getPosition() % 2 == 0) {
				row.setBackgroundDrawable(getResources().getDrawable(R.drawable.dark_item_background));
			} 
			
			holder.populateFrom(c, dbHelper);
		}
		
		@Override
		public View newView(Context ctxt, Cursor c, ViewGroup parent) {
			Log.i(TAG, "newView()" + c.getPosition());
			View row = null;
			LayoutInflater inflater=((Activity)ctxt).getLayoutInflater();
			
//			if (getItemViewType(c.getPosition()) == 0) {
				Log.i(TAG, "inflating 1");
				row=inflater.inflate(R.layout.row, parent, false);
//			} else {
//				Log.i(TAG, "inflating 2");
//				row=inflater.inflate(R.layout.row_gray, parent, false);
//			}
			ViewHolder holder=new ViewHolder(row);
			
			row.setTag(holder);
			
			return(row);
		}
	}

	static class ViewHolder {
		private TextView title;
		private int id;
		
		
		ViewHolder(View row) {
			title = (TextView)row.findViewById(R.id.title);
		}
		
		void populateFrom(Cursor c, DBHelper helper) {
			title.setText(helper.getTitle(c));
			id = helper.getId(c);
		}
		
		public int getId() {
			return id;
		}
	}
	
	public void refresh() {
		mAdapter.changeCursor(dbHelper.getAll());
		getListView().setItemChecked(-1, true);
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
            actionMode.setTitle(ticket.getTitle());
            mView.setBackgroundDrawable(getResources().getDrawable(R.drawable.long_press_item_highlight));

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
            mView.setBackgroundDrawable(null);
            mCurrentActionMode = null;
        }
    };
}
