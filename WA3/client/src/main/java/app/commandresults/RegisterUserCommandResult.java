package app.commandresults;

public class RegisterUserCommandResult implements CommandResult {
    private static final String SUCCESS_MESSAGE = "User registered successfully!";
    private static final String ERROR_MESSAGE = "User already exists!";
    private boolean registered;

    public RegisterUserCommandResult(boolean registered) {
        this.registered = registered;
    }

    @Override
    public void show() {
        String result = registered ? SUCCESS_MESSAGE : ERROR_MESSAGE;
        System.out.println(result);
    }
}
