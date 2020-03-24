package commandresults;

public class ExitCommandResult implements CommandResult {
    @Override
    public void show() {
        System.out.println("Terminating...");
    }
}
