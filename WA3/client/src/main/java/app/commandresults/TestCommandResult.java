package app.commandresults;

public class TestCommandResult implements CommandResult {
    private String result;

    public TestCommandResult(String result) {
        this.result = result;
    }

    @Override
    public void show() {
        System.out.println(result);
    }
}
