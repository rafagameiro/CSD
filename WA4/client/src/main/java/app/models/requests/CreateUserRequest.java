package app.models.requests;

import app.models.User;

public class CreateUserRequest {
    User user;

    public CreateUserRequest() {

    }

    public CreateUserRequest(User user) {
        this.user = user;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
