package app.models;

public class AppUser {
    private User user;
    private String contractFilePath;
    private int keyId;
    private Configuration config;

    public AppUser() {
        this.user = new User();
        this.contractFilePath = null;
        this.keyId = -1;
    }

    public AppUser(User user, String contractFilePath, int keyId) {
        this.user = user;
        this.contractFilePath = contractFilePath;
        this.keyId = keyId;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getContractFilePath() {
        return contractFilePath;
    }

    public void setContractFilePath(String contractFilePath) {
        this.contractFilePath = contractFilePath;
    }

    public int getKeyId() {
        return keyId;
    }

    public void setKeyId(int keyId) {
        this.keyId = keyId;
    }

    public void setConfig(Configuration config) {
        this.config = config;
    }

    public Configuration getConfig() {
        return config;
    }
}
