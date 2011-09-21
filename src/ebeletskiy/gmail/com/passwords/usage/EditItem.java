package ebeletskiy.gmail.com.passwords.usage;

import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import ebeletskiy.gmail.com.passwords.usage.models.Ticket;
import ebeletskiy.gmail.com.passwords.usage.utils.ShowToast;

public class EditItem extends NewItem
{
  private static final String TAG = "EditItem";

  private boolean titleChanged = false;
  private Ticket ticket;

  private String beforeTextChanged;
  private String afterTextChanged;

  public EditItem() {
  };

  public EditItem(Ticket ticket) {
    this.ticket = ticket;
  }

  @Override
  public void onActivityCreated(Bundle savedInstanceState) {
    super.onActivityCreated(savedInstanceState);
    initUIelements();
    beforeTextChanged = title.getText().toString();

    title.addTextChangedListener(new TextWatcher() {

      @Override
      public void onTextChanged(CharSequence s, int start, int before, int count) {
      }

      @Override
      public void beforeTextChanged(CharSequence s, int start, int count,
          int after) {
      }

      @Override
      public void afterTextChanged(Editable s) {
        afterTextChanged = title.getText().toString();

        if (beforeTextChanged.equals(afterTextChanged)) {
          titleChanged = false;
        } else {
          titleChanged = true;
        }

      }
    });
  }

  private void initUIelements() {
    title = (EditText) getView().findViewById(R.id.et_title);
    login = (EditText) getView().findViewById(R.id.et_login_data);
    password = (EditText) getView().findViewById(R.id.et_password_data);
    notes = (EditText) getView().findViewById(R.id.et_notes_data);

    if (ticket != null) {
      title.setText(ticket.getTitle());
      login.setText(ticket.getLogin());
      password.setText(ticket.getPassword());
      notes.setText(ticket.getNotes());
    }
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {

    switch (item.getItemId()) {
    case R.id.save_item:

      Log.i("Dev", "Save menu cliked");

      if (titleChanged) {
        Log.i("Dev", "menu: title changed");

        if (isDuplicate(title.getText().toString())) {
          Log.i("Dev", "menu: title is duplicate");
          ShowToast.showToast(getActivity(),
              "The item wich such name already exists.");
        } else {
          Log.i("Dev", "menu: title is not duplicate -> updateData()");
          updateData();
        }

      } else {
        Log.i("Dev", "menu: title not changed && is not duplicate");
        if (saveItemListener != null && checkFields()) {
          Log.i("Dev", "menu: fields are ok");
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
    InputMethodManager imm = (InputMethodManager) getActivity()
        .getSystemService(Context.INPUT_METHOD_SERVICE);
    imm.hideSoftInputFromWindow(title.getWindowToken(), 0);
  }

  public Ticket createTicket() {

    ticket.setTitle((title.getText()).toString().trim());
    ticket.setLogin((login.getText()).toString().trim());
    ticket.setPassword((password.getText()).toString().trim());
    ticket.setNotes((notes.getText()).toString().trim());

    return ticket;
  }
}
