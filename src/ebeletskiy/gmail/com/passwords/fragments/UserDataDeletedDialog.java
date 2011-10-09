package ebeletskiy.gmail.com.passwords.fragments;

import ebeletskiy.gmail.com.passwords.R;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;

public class UserDataDeletedDialog extends DialogFragment {

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        return new AlertDialog.Builder(getActivity())
                .setMessage(getString(R.string.all_data_has_been_deleted)).setCancelable(false)
                .create();
    }

}
