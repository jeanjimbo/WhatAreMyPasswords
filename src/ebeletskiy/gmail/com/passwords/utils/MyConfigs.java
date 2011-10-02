package ebeletskiy.gmail.com.passwords.utils;

public final class MyConfigs {
    private MyConfigs() {
    }

    public static final boolean DEBUG = true;
    public static final int DESTROY_APP_AFTER = 10000; // milliseconds

    public static final String PREFS_NAME = "MyPrefsFile";
    public static final String FIRST_RUN = "FirstRun";
    public static final String USER_PASSWORD = "UserPassword";
    public static final String FIRST_RUN_MAIN = "FirstRunMain";
    public static final String FIRST_PASSWORD_GENERATED = "firstPasswordRun";
    public static final String FIRST_ITEM_DESCRIPTION_OPENED = "firstItemDesriptionRun";
    public static final String ORIENTATION_CHANGE = "orientation_change";
    
    public static final int NEW_ITEM_PASSWORD_REQUEST_CODE = 15;
}
