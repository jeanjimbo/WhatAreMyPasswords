package ebeletskiy.gmail.com.passwords;

import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import ebeletskiy.gmail.com.passwords.models.Ticket;
import ebeletskiy.gmail.com.passwords.utils.ShowToast;

public class EditItem extends NewItem {
    private static final String TAG = "EditItem";

    private boolean mTitleChanged = false;
    private Ticket mTicket;

    private String mBeforeTextChanged;
    private String mAfterTextChanged;

    public EditItem() {
    };

    public EditItem(Ticket ticket) {
        this.mTicket = ticket;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initUIelements();
        mBeforeTextChanged = title.getText().toString();

        title.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                mAfterTextChanged = title.getText().toString();

                if (mBeforeTextChanged.equals(mAfterTextChanged)) {
                    mTitleChanged = false;
                } else {
                    mTitleChanged = true;
                }

            }
        });
    }

    private void initUIelements() {
        title = (EditText) getView().findViewById(R.id.et_title);
        login = (EditText) getView().findViewById(R.id.et_login_data);
        password = (EditText) getView().findViewById(R.id.et_password_data);
        notes = (EditText) getView().findViewById(R.id.et_notes_data);

        if (mTicket != null) {
            title.setText(mTicket.getTitle());
            login.setText(mTicket.getLogin());
            password.setText(mTicket.getPassword());
            notes.setText(mTicket.getNotes());
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
        case R.id.save_item:

            if (mTitleChanged) {

                if (isDuplicate(title.getText().toString())) {
                    ShowToast.showToast(getActivity(), "The item wich such name already exists.");
                } else {
                    updateData();
                }

            } else {
                if (saveItemListener != null && checkFields()) {
                    updateData();
                } else {
                    ShowToast.showToast(getActivity(), "Please fill Title.");
                }
            }

            break;
        }
        return true;
    }

    private void updateData() {
        dbHelper.updateRow(createTicket());
        hideKeyboard();
        saveItemListener.saveItem();
        ShowToast.showToast(getActivity(), "Ticket has been updated.");
    }

    private void hideKeyboard() {
        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(
                Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(title.getWindowToken(), 0);
    }

    public Ticket createTicket() {

        mTicket.setTitle((title.getText()).toString().trim());
        mTicket.setLogin((login.getText()).toString().trim());
        mTicket.setPassword((password.getText()).toString().trim());
        mTicket.setNotes((notes.getText()).toString().trim());

        return mTicket;
    }
}
