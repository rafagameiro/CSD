package app.commandresults;

public class RegisterUserCommandResult implements CommandResult {
    private static final String SUCCESS_MESSAGE = "User registered successfully!";
    private String errorMsg;
    private boolean success;

    public RegisterUserCommandResult() {
        this.success = true;
    }

    public RegisterUserCommandResult(String errorMsg) {
        this.success = false;
        this.errorMsg = errorMsg;
    }

    @Override
    public void show() {
        String result = success ? SUCCESS_MESSAGE : errorMsg;
        System.out.println(result);
    }

    public boolean isSuccess() {
        return success;
    }
}
