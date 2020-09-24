package app.commandresults;

public class InstallSmartContractResult implements CommandResult {

    private String filePath;

    public InstallSmartContractResult(String filePath) {
        this.filePath = filePath;
    }

    @Override
    public void show() {
        System.out.println("SmartContract located at " + filePath + " was intalled with success, and will be used in future requests!");
    }

    public String getFilePath() {
        return filePath;
    }
}
