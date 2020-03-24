package models;

public class UserModel {
    private String username;
    private String password;

    public UserModel(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public UserModel() {
        this(null, null);
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isEmpty() {
        return username == null && password == null;
    }

    public void clone(UserModel user) {
        this.username = user.getUsername();
        this.password = user.getPassword();
    }

    public void reset() {
        this.username = null;
        this.password = null;
    }
}
