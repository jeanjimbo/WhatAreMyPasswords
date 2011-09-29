package ebeletskiy.gmail.com.passwords.prefs;

import android.app.Fragment;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import ebeletskiy.gmail.com.passwords.R;
import ebeletskiy.gmail.com.passwords.utils.MyConfigs;
import ebeletskiy.gmail.com.passwords.utils.ShowToast;

public class ChangePasswordFragment extends Fragment {
    private EditText firstPassword;
    private EditText secondPassword;
    private Button okButton;
    private String newPassword;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.change_password_fragment, container);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        firstPassword = (EditText) getView().findViewById(R.id.changepassword_et_firstpassword);
        secondPassword = (EditText) getView().findViewById(R.id.changepassword_et_secondpassword);
        okButton = (Button) getView().findViewById(R.id.changepassword_bt_ok);
        okButton.setEnabled(false);
        okButton.setOnClickListener(okButtonOnClickListener);

        secondPassword.addTextChangedListener(textWatcher);
    }

    private TextWatcher textWatcher = new TextWatcher() {

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void afterTextChanged(Editable s) {
            if (firstPassword.getText().toString()
                    .compareToIgnoreCase(secondPassword.getText().toString()) == 0) {
                newPassword = firstPassword.getText().toString().trim();
                okButton.setEnabled(true);
            } else {
//                ShowToast.showToast(getActivity(),
//                        getResources().getString(R.string.fill_both_fields_passwords_match));
                okButton.setEnabled(false);
            }
        }
    };

    private OnClickListener okButtonOnClickListener = new OnClickListener() {

        @Override
        public void onClick(View v) {
            SharedPreferences sharedPreferences = getActivity().getSharedPreferences(
                    MyConfigs.PREFS_NAME, 0);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString(MyConfigs.USER_PASSWORD, newPassword);
            editor.commit();
            getActivity().onBackPressed();
        }
    };

}
