package ebeletskiy.gmail.com.passwords.activities;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import ebeletskiy.gmail.com.passwords.fragments.PasswordAttemptsDialog;
import ebeletskiy.gmail.com.passwords.fragments.UserDataDeletedDialog;
import ebeletskiy.gmail.com.passwords.utils.DBHelper;
import ebeletskiy.gmail.com.passwords.utils.MyConfigs;

public class IncorrectPasswordManager {
    private Activity mActivity;
    private DBHelper dbHelper;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor prefsEditor;

    public IncorrectPasswordManager(Activity activity) {
        mActivity = activity;
        dbHelper = new DBHelper(activity);
        sharedPreferences = activity.getSharedPreferences(MyConfigs.PREFS_NAME, 0);
        prefsEditor = sharedPreferences.edit();
    }

    public void start() {
        incrementIncorrectPasswordAttempts();
        if (getIncorrectPasswordAttempts() == getAllowedPasswordAttempts()) {
            clearUserDatabase();
            resetUserSettings();
            notifyUserDataDeleted();
        } else {
            showAttemptsLeftAlert();
        }
    }

    private void incrementIncorrectPasswordAttempts() {
        int tmpPasswordAttempts = sharedPreferences
                .getInt(MyConfigs.INCORRECT_PASSWORD_ATTEMPTS, 0);
        tmpPasswordAttempts++;
        prefsEditor.putInt(MyConfigs.INCORRECT_PASSWORD_ATTEMPTS, tmpPasswordAttempts).commit();
    }

    private int getIncorrectPasswordAttempts() {
        int tmp = sharedPreferences.getInt(MyConfigs.INCORRECT_PASSWORD_ATTEMPTS, 0);
        return tmp;
    }

    private void notifyUserDataDeleted() {
        new UserDataDeletedDialog().show(mActivity.getFragmentManager(), "tag");
    }

    private void showAttemptsLeftAlert() {
        int attemptsLeft = getAllowedPasswordAttempts() - getIncorrectPasswordAttempts();
        new PasswordAttemptsDialog(attemptsLeft).show(mActivity.getFragmentManager(), "tag");
    }

    private void clearUserDatabase() {
        dbHelper.deleteAll();
        dbHelper.close();
    }

    private void resetUserSettings() {
        prefsEditor.clear().commit();
    }

    private int getAllowedPasswordAttempts() {
        return sharedPreferences.getInt(MyConfigs.INCORRECT_PASSWORD_ALLOWED_ATTEMPTS,
                MyConfigs.INCORRECT_PASSWORD_ALLOWED_ATTEMPTS_DEFAULT);
    }

}
