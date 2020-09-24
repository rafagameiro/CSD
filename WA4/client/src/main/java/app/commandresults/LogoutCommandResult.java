package app.commandresults;

public class LogoutCommandResult implements CommandResult {
    private static final String MESSAGE = "User logged out successfully!";

    @Override
    public void show() {
        System.out.println(MESSAGE);
    }
}
