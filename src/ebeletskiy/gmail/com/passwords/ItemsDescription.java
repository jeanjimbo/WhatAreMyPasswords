package ebeletskiy.gmail.com.passwords;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.text.method.PasswordTransformationMethod;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.widget.TextView;
import ebeletskiy.gmail.com.passwords.interfaces.DeleteItemListener;
import ebeletskiy.gmail.com.passwords.interfaces.EditItemListener;
import ebeletskiy.gmail.com.passwords.models.Ticket;
import ebeletskiy.gmail.com.passwords.utils.Clipboard;
import ebeletskiy.gmail.com.passwords.utils.DBHelper;
import ebeletskiy.gmail.com.passwords.utils.FontManager;
import ebeletskiy.gmail.com.passwords.utils.MyCustomAlertDialog;
import ebeletskiy.gmail.com.passwords.utils.ShowToast;

public class ItemsDescription extends Fragment {
    private static final String TAG = "ItemsDescription";
    private boolean mMenuWasCreated = false;
    private boolean mPasswordShown = true;

    private Ticket mTicket;
    private TextView mTitle, mLogin, mPassword, mNotes;
    private DBHelper mDbHelper;
    private DeleteItemListener mDeleteListener;
    private EditItemListener mEditItemListener;
    private Clipboard mClipBoard;

    public ItemsDescription() {

    }

    public ItemsDescription(Ticket ticket) {
        if (ticket == null) {
            throw new IllegalArgumentException();
        }

        this.mTicket = ticket;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mDeleteListener = (DeleteItemListener) activity;
        mEditItemListener = (EditItemListener) activity;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mDbHelper = new DBHelper(getActivity());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.items_description, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initUI();
        applyFonts();

        if (!mMenuWasCreated) {
            setHasOptionsMenu(true);
            mMenuWasCreated = true;
        }

        if (savedInstanceState != null) {
            mTitle.setText((String) savedInstanceState.get("mTitle"));
            mLogin.setText((String) savedInstanceState.get("mLogin"));
            mPassword.setText((String) savedInstanceState.get("mPassword"));
            mNotes.setText((String) savedInstanceState.get("mNotes"));
        }

        mPassword.setOnLongClickListener(longClickListener);
        mPassword.setOnClickListener(shortClickListener);

        applyFonts();
    }

    private void applyFonts() {
        if (mTitle != null) {
            FontManager.applyTypewriter(mTitle);
        }
        if (mLogin != null) {
            FontManager.applyTypewriter(mLogin);
        }
        if (mPassword != null) {
            FontManager.applyTypewriter(mPassword);
        }
        if (mNotes != null) {
            FontManager.applyTypewriter(mNotes);
        }

        TextView login_t = (TextView) getView().findViewById(R.id.tv_login);
        if (login_t != null) {
            FontManager.applyTypewriter(login_t);
        }
        TextView password_t = (TextView) getView().findViewById(R.id.tv_password);
        if (password_t != null) {
            FontManager.applyTypewriter(password_t);
        }
        TextView notes_t = (TextView) getView().findViewById(R.id.tv_notes);
        if (notes_t != null) {
            FontManager.applyTypewriter(notes_t);
        }
    }

    OnClickListener shortClickListener = new OnClickListener() {

        @Override
        public void onClick(View v) {
            if (mPasswordShown) {
                mPassword.setTransformationMethod(null);
                mPasswordShown = false;
            } else {
                mPassword.setTransformationMethod(new PasswordTransformationMethod());
                mPasswordShown = true;
            }
        }
    };

    OnLongClickListener longClickListener = new OnLongClickListener() {

        @Override
        public boolean onLongClick(View v) {
            mClipBoard = new Clipboard(getActivity());
            mClipBoard.copyText(mPassword.getText().toString());
            ShowToast.showToast(getActivity(), "Password copied to clipboard");
            return true;
        }
    };

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_items_description, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

        case R.id.delete_item:
            showAlertDialog();
            break;
        case R.id.edit_item:
            mEditItemListener.loadEditItem(mTicket);
        default:
            break;
        }
        return super.onOptionsItemSelected(item);

    }

    private void showAlertDialog() {
        new MyCustomAlertDialog(mTicket.getId()).show(getFragmentManager(), "tag");
    }

    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("mTitle", mTitle.getText().toString());
        outState.putString("mLogin", mLogin.getText().toString());
        outState.putString("mPassword", mPassword.getText().toString());
        outState.putString("mNotes", mNotes.getText().toString());
    }

    private void initUI() {
        mTitle = (TextView) getView().findViewById(R.id.tv_title);
        mLogin = (TextView) getView().findViewById(R.id.tv_login_data);
        mPassword = (TextView) getView().findViewById(R.id.tv_password_data);
        mNotes = (TextView) getView().findViewById(R.id.tv_notes_data);

        if (mTicket != null) {
            mTitle.setText(mTicket.getTitle());
            mLogin.setText(mTicket.getLogin());
            mPassword.setText(mTicket.getPassword());
            mNotes.setText(mTicket.getNotes());
        }
    }

    public void onDestroy() {
        super.onDestroy();
        if (mDbHelper != null) {
            mDbHelper.close();
        }
    }
}
