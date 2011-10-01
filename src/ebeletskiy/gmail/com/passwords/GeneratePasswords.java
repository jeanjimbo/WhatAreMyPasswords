package ebeletskiy.gmail.com.passwords;

import java.util.Random;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;
import ebeletskiy.gmail.com.passwords.utils.MyConfigs;
import ebeletskiy.gmail.com.passwords.utils.ShowToast;

public class GeneratePasswords extends Activity {

    private static final int PASSWORD_MAX_LENGTH = 15;
    private static final int PASSWORD_DEFAULT_LENGTH = 6;
    private static final String UPPERCASE_LETTERS_ONLY = "ABCDEFGHIKLMNOPQRSTVXYZ";
    private static final String LOWERCASE_LETTERS_ONLY = "abcdefghijklmnopqrstuvwxyz";
    private static final String UPPERCASE_AND_LOWECASE_LETTERS = "ABCDEFGHIKLMNOPQRSTVXYZabcdefghijklmnopqrstuvwxyz";
    private static final String UPPERCASE_AND_LOWECASE_LETTERS_AND_DIGITS = "ABCDEFGHIKLMNOPQRSTVXYZabcdefghijklmnopqrstuvwxyz0123456789";
    private static final String UPPERCASE_AND_DIGITS = "ABCDEFGHIKLMNOPQRSTVXYZ0123456789";
    private static final String LOWERCASE_AND_DIGITS = "abcdefghijklmnopqrstuvwxyz0123456789";
    private static final String DIGITS_ONLY = "0123456789";

    private int mPasswordLength;
    private String mGeneratedPassword;
    private SeekBar mSeekBar;
    private RadioGroup mRgLeft, mRgRight;
    private LinearLayout mLlPassword;
    private TextView mTvPassword;
    private TextView mTvPasswordLength;
    private Button mBtnGeneratePassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.generate_password);
        initUI();

        if (savedInstanceState != null) {
            mGeneratedPassword = savedInstanceState.getString("generated_password");
            mTvPassword.setText(mGeneratedPassword);
            mLlPassword.setVisibility(savedInstanceState.getInt("password_visibility"));
            mRgLeft.check(savedInstanceState.getInt("left_radio_group"));
            mRgRight.check(savedInstanceState.getInt("right_radio_group"));
            mSeekBar.setProgress(savedInstanceState.getInt("seekbar_position"));
        } else {
            mSeekBar.setProgress(PASSWORD_DEFAULT_LENGTH);
            mLlPassword.setVisibility(View.INVISIBLE);
        }

        mSeekBar.setMax(PASSWORD_MAX_LENGTH);
        mTvPasswordLength.setText(" " + Integer.toString(mSeekBar.getProgress()));

        mBtnGeneratePassword.setOnClickListener(btnGenerateClickListener);
        mSeekBar.setOnSeekBarChangeListener(seekBarListener);
        mTvPassword.setOnClickListener(tvPasswordListener);
    }

    protected void onSaveInstanceState(Bundle outState) {
        if (mGeneratedPassword != null) {
            outState.putString("generated_password", mGeneratedPassword);
        }
        outState.putInt("password_visibility", mLlPassword.getVisibility());
        outState.putInt("left_radio_group", mRgLeft.getCheckedRadioButtonId());
        outState.putInt("right_radio_group", mRgRight.getCheckedRadioButtonId());
        outState.putInt("seekbar_position", mSeekBar.getProgress());
    }

    private void initUI() {
        mSeekBar = (SeekBar) findViewById(R.id.generate_password_sb_seekbar);
        mRgLeft = (RadioGroup) findViewById(R.id.generate_password_rg_left);
        mRgRight = (RadioGroup) findViewById(R.id.generate_password_rg_right);
        mLlPassword = (LinearLayout) findViewById(R.id.generate_password_ll_result);
        mTvPassword = (TextView) findViewById(R.id.generate_password_tv_result);
        mBtnGeneratePassword = (Button) findViewById(R.id.generate_password_bt_generate);
        mTvPasswordLength = (TextView) findViewById(R.id.generate_password_tv_password_length);
    }

    OnClickListener tvPasswordListener = new OnClickListener() {

        @Override
        public void onClick(View v) {
            Intent i = new Intent();
            i.putExtra("password", mTvPassword.getText().toString());
            setResult(15, i);
            onBackPressed();
        }
    };

    SeekBar.OnSeekBarChangeListener seekBarListener = new OnSeekBarChangeListener() {

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {
        }

        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            mTvPasswordLength.setText(" " + Integer.toString(progress));
        }
    };

    OnClickListener btnGenerateClickListener = new OnClickListener() {
        private String stringToGeneratePassword;

        @Override
        public void onClick(View v) {
            mGeneratedPassword = null;
            readSettings();
            generatePassword();
            showPassword();
            showTip();

            if (mGeneratedPassword != null) {
                mBtnGeneratePassword.setText(R.string.refresh);
            }
        }

        public void showTip() {
            SharedPreferences sharedPreferences = getSharedPreferences(MyConfigs.PREFS_NAME, 0);
            int result = sharedPreferences.getInt(MyConfigs.FIRST_PASSWORD_GENERATED, 0);
            if (result == 0) {
                ShowToast.showToast(getApplicationContext(),
                        getString(R.string.click_on_the_password_to_use_it_));
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putInt(MyConfigs.FIRST_PASSWORD_GENERATED, 1).commit();
            }
        }

        public void readSettings() {
            switch (mRgLeft.getCheckedRadioButtonId()) {
            case R.id.generate_password_rb_digits:
                stringToGeneratePassword = DIGITS_ONLY;
                break;
            case R.id.generate_password_rb_letters:
                if (mRgRight.getCheckedRadioButtonId() == R.id.generate_password_rb_upper_case) {
                    stringToGeneratePassword = UPPERCASE_LETTERS_ONLY;
                } else if (mRgRight.getCheckedRadioButtonId() == R.id.generate_password_rb_lower_case) {
                    stringToGeneratePassword = LOWERCASE_LETTERS_ONLY;
                } else {
                    stringToGeneratePassword = UPPERCASE_AND_LOWECASE_LETTERS;
                }
                break;
            case R.id.generate_password_rb_both:
                if (mRgRight.getCheckedRadioButtonId() == R.id.generate_password_rb_upper_case) {
                    stringToGeneratePassword = UPPERCASE_AND_DIGITS;
                } else if (mRgRight.getCheckedRadioButtonId() == R.id.generate_password_rb_lower_case) {
                    stringToGeneratePassword = LOWERCASE_AND_DIGITS;
                } else {
                    stringToGeneratePassword = UPPERCASE_AND_LOWECASE_LETTERS_AND_DIGITS;
                }
            }

        }

        public void generatePassword() {
            int length = mSeekBar.getProgress();
            if (length == 0) {
                ShowToast.showToast(getApplicationContext(),
                        getString(R.string.password_s_length_can_t_be_zero));
                return;
            }
            StringBuffer sb = new StringBuffer();
            while (length > 0) {
                Random random = new Random();
                sb.append(stringToGeneratePassword.charAt(random.nextInt(stringToGeneratePassword
                        .length())));
                length--;
            }
            mGeneratedPassword = sb.toString();
        }

        public void showPassword() {
            mLlPassword.setVisibility(View.VISIBLE);
            mTvPassword.setText(mGeneratedPassword);
        }
    };;
}
