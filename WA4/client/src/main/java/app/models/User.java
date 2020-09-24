package app.models;

public class User {
    private String username;
    private String password;
    private byte[] certificate;

    public User(String username, String password) {
        this.username = username;
        this.password = password;
        this.certificate= new byte[0];
    }

    public User(String username, String password, byte[] certificate) {
        this.username = username;
        this.password = password;
        this.certificate = certificate;
    }

    public User() {
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

    public byte[] getCertificate() {
        return certificate;
    }

    public void setCertificate(byte[] certificate) {
        this.certificate = certificate;
    }

    public void clone(User user) {
        this.username = user.getUsername();
        this.password = user.getPassword();
        this.certificate = user.certificate;
    }

    public void reset() {
        this.username = null;
        this.password = null;
        this.certificate = new byte[0];
    }
}
