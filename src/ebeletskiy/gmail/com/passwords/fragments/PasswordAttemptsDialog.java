package ebeletskiy.gmail.com.passwords.fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import ebeletskiy.gmail.com.passwords.R;

public class PasswordAttemptsDialog extends DialogFragment {
    private int attemptsLeft;

    public PasswordAttemptsDialog(int attemptsLeft) {
        this.attemptsLeft = attemptsLeft;
    }

    public PasswordAttemptsDialog() {
    };

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            attemptsLeft = savedInstanceState.getInt("attemptsLeft");
        }

        return new AlertDialog.Builder(getActivity())
                .setMessage(getString(R.string.you_have)
                        + attemptsLeft
                        + getString(R.string._attempts_left_afterwards_for_security_reasons_all_user_data_will_be_deleted))
                .setCancelable(false).create();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("attemptsLeft", attemptsLeft);
    }

}
