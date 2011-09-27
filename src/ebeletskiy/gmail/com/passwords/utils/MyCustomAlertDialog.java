package ebeletskiy.gmail.com.passwords.utils;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import ebeletskiy.gmail.com.passwords.interfaces.DeleteItemListener;

public class MyCustomAlertDialog extends DialogFragment {

    private int mTicketId;
    private DBHelper mDbHelper;
    private DeleteItemListener mDeleteListener;

    public MyCustomAlertDialog(int ticketId) {
        mTicketId = ticketId;
    }

    public MyCustomAlertDialog() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (savedInstanceState != null) {
            mTicketId = savedInstanceState.getInt("ticketid");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putInt("ticketid", mTicketId);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        mDbHelper = new DBHelper(getActivity());
        mDeleteListener = (DeleteItemListener) getActivity();

        return new AlertDialog.Builder(getActivity())
                .setMessage("Are you sure you want to delete the item?").setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        mDbHelper.deleteRow(mTicketId);
                        mDeleteListener.onDeleteItem();
                        ShowToast.showToast(getActivity(), "Item has been deleted.");
                    }
                }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                }).create();
    }
}
