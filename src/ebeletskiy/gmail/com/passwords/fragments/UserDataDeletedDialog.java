package ebeletskiy.gmail.com.passwords.fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import ebeletskiy.gmail.com.passwords.R;
import ebeletskiy.gmail.com.passwords.activities.LaunchActivityManager;

public class UserDataDeletedDialog extends DialogFragment {

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        return new AlertDialog.Builder(getActivity())
                .setMessage(getString(R.string.all_data_has_been_deleted)).setCancelable(false)
                .setPositiveButton(getString(R.string.ok), new OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        startActivity(new Intent(getActivity(), LaunchActivityManager.class));
                    }
                }).create();
    }

}
