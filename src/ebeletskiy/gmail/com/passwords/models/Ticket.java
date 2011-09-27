package ebeletskiy.gmail.com.passwords.models;

public class Ticket {
    private int id;
    private String mTitle;
    private String mLogin;
    private String mPassword;
    private String mNotes;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        this.mTitle = title;
    }

    public String getLogin() {
        return mLogin;
    }

    public void setLogin(String login) {
        this.mLogin = login;
    }

    public String getPassword() {
        return mPassword;
    }

    public void setPassword(String password) {
        this.mPassword = password;
    }

    public String getNotes() {
        return mNotes;
    }

    public void setNotes(String notes) {
        this.mNotes = notes;
    }

}
