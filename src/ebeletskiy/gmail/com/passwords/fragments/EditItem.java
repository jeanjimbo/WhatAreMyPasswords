package ebeletskiy.gmail.com.passwords.fragments;

import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.ViewGroup.MarginLayoutParams;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import ebeletskiy.gmail.com.passwords.R;
import ebeletskiy.gmail.com.passwords.models.Ticket;
import ebeletskiy.gmail.com.passwords.utils.MyConfigs;
import ebeletskiy.gmail.com.passwords.utils.ShowToast;

public class EditItem extends NewItem {
    private static final String TAG = "EditItem.java";

    private boolean mTitleChanged = false;
    private Ticket mTicket;
    private int id;

    private String mBeforeTextChanged;
    private String mAfterTextChanged;

    public EditItem() {
    };

    public EditItem(Ticket ticket) {
        this.mTicket = ticket;
        id = ticket.getId();
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if (savedInstanceState != null) {
            mBeforeTextChanged = savedInstanceState.getString("beforeTextChanged");
            id = savedInstanceState.getInt("id");
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        mBeforeTextChanged = title.getText().toString().trim();
        title.addTextChangedListener(textWatcherListener);
    }

    @Override
    public void initUI() {
        super.initUI();
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
            onSaveOperation();
            break;
        }
        return true;
    }

    @Override
    public void onSaveOperation() {
        if (mTitleChanged) {

            if (isDuplicate(title.getText().toString())) {
                ShowToast.showToast(getActivity(), getString(R.string.item_already_exists));
            } else {
                updateData();
            }

        } else {
            if (saveItemListener != null && checkFields()) {
                updateData();
            } else {
                ShowToast.showToast(getActivity(), getString(R.string.fill_the_title));
            }
        }
    }

    private void updateData() {
        dbHelper.updateRow(createTicket());
        hideKeyboard();
        saveItemListener.saveItem();
        ShowToast.showToast(getActivity(),
                getActivity().getString(R.string.ticket_has_been_updated_));
    }

    private void hideKeyboard() {
        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(
                Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(title.getWindowToken(), 0);
    }

    public Ticket createTicket() {
        Ticket mTicket = new Ticket();

        mTicket.setTitle((title.getText()).toString().trim());
        mTicket.setLogin((login.getText()).toString().trim());
        mTicket.setPassword((password.getText()).toString().trim());
        mTicket.setNotes((notes.getText()).toString().trim());
        mTicket.setId(id);

        return mTicket;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("beforeTextChanged", mBeforeTextChanged);
        outState.putInt("id", id);
    }

    TextWatcher textWatcherListener = new TextWatcher() {

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void afterTextChanged(Editable s) {
            mAfterTextChanged = title.getText().toString().trim();
            if (mBeforeTextChanged.equals(mAfterTextChanged)) {
                mTitleChanged = false;
            } else {
                mTitleChanged = true;
            }
        }
    };
}
