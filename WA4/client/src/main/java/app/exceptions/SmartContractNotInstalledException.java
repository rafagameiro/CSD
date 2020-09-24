package app.exceptions;

public class SmartContractNotInstalledException extends InvalidCommandException {
    public SmartContractNotInstalledException() {
        super("There are not SmartContracts installed!");
    }
}
