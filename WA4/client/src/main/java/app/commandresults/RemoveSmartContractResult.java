package app.commandresults;

public class RemoveSmartContractResult implements CommandResult {
    private static final String SUCCESS_MSG = "Smart contract located at %s removed with success!";
    String filePath;

    public RemoveSmartContractResult(String filePath) {
        this.filePath = filePath;
    }

    @Override
    public void show() {
        System.out.println(String.format(SUCCESS_MSG, filePath));
    }
}
