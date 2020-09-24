package app.commandresults;

public class LoginCommandResult implements CommandResult {
    private static final String SUCCESS_MESSAGE = "User logged in successfully!";
    private static final String ERROR_MESSAGE = "Username or password invalid!";
    private boolean logged;

    public LoginCommandResult(boolean logged) {
        this.logged = logged;
    }

    @Override
    public void show() {
        String result = logged ? SUCCESS_MESSAGE : ERROR_MESSAGE;
        System.out.println(result);
    }

    public boolean isLogged() {
        return logged;
    }
}
