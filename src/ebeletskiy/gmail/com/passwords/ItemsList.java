package ebeletskiy.gmail.com.passwords;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ListFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
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
import ebeletskiy.gmail.com.passwords.utils.FontManager;
import ebeletskiy.gmail.com.passwords.utils.ShowToast;

public class ItemsList extends ListFragment
{
  private static final String TAG = "ItemsList";
  private boolean menuWasCreated = false;

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
    cursor = dbHelper.getAll();

    if (!menuWasCreated) {
      Log.i(TAG, "creating menu");
      setHasOptionsMenu(true);
      menuWasCreated = true;
    }

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

        mCurrentActionMode = getActivity().startActionMode(
            mContentSelectionActionModeCallback);
        return true;
      }
    });

    getListView().setEmptyView(getView().findViewById(R.id.ll_empty_left));
  }

  @Override
  public void onAttach(Activity activity) {
    super.onAttach(activity);

    itemClickListener = (ListItemClickListener) activity;
    newItemBtnListener = (AddNewItemListener) activity;
    deleteListener = (DeleteItemListener) activity;
    editItemListener = (EditItemListener) activity;
  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    return inflater.inflate(R.layout.items_list, container, false);
  }

  @Override
  public void onListItemClick(ListView l, View v, int position, long id) {
    ViewHolder viewHolder = (ViewHolder) v.getTag();
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
    ViewHolder viewHolder = (ViewHolder) v.getTag();
    Cursor cursor = dbHelper.getItem(viewHolder.getId());
    return ticket = DataConverter.convertToTicket(cursor);
  }

  private class MAdapter extends CursorAdapter
  {

    private Drawable defaultDrawable;

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
      ViewHolder holder = (ViewHolder) row.getTag();
      if (c.getPosition() % 2 == 0 && row.getBackground() == defaultDrawable) {
        row.setBackgroundDrawable(getResources().getDrawable(
            R.drawable.dark_item_background));
      }

      holder.populateFrom(c, dbHelper);
    }

    @Override
    public View newView(Context ctxt, Cursor c, ViewGroup parent) {
      View row = null;
      LayoutInflater inflater = ((Activity) ctxt).getLayoutInflater();
      row = inflater.inflate(R.layout.row, parent, false);
      this.defaultDrawable = row.getBackground();
      ViewHolder holder = new ViewHolder(row);

      row.setTag(holder);

      return (row);
    }
  }

  static class ViewHolder
  {
    private TextView title;
    private int id;

    ViewHolder(View row) {
      title = (TextView) row.findViewById(R.id.title);
//      FontManager.applyTypewriter(title);
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

  private void showAlertDialog() {
    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
    builder.setMessage("Are you sure you want to delete the item?")
        .setCancelable(false)
        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
          public void onClick(DialogInterface dialog, int id) {
            dbHelper.deleteRow(ticket.getId());
            deleteListener.onDeleteItem();
            ShowToast.showToast(getActivity(), "Item has been deleted.");
          }
        }).setNegativeButton("No", new DialogInterface.OnClickListener() {
          public void onClick(DialogInterface dialog, int id) {
            dialog.cancel();
          }
        });

    AlertDialog alert = builder.create();
    builder.show();
  }

  private ActionMode.Callback mContentSelectionActionModeCallback = new ActionMode.Callback() {
    Drawable viewBackground; 
    public boolean onCreateActionMode(ActionMode actionMode, Menu menu) {
      actionMode.setTitle(ticket.getTitle());
      viewBackground = mView.getBackground();
      mView.setBackgroundDrawable(getResources().getDrawable(
          R.drawable.long_press_item_highlight));

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
        editItemListener.loadEditItem(ticket);
        actionMode.finish();
        return true;
      case R.id.delete_item:
        showAlertDialog();
        actionMode.finish();
        return true;
      }
      return false;
    }

    public void onDestroyActionMode(ActionMode actionMode) {
      mView.setBackgroundDrawable(null);
      mCurrentActionMode = null;
      mAdapter.notifyDataSetChanged();
      mView.setBackgroundDrawable(viewBackground);
    }
  };

  public void onDestroy() {
    super.onDestroy();
    if (dbHelper != null) {
      dbHelper.close();
    }
  }
}
