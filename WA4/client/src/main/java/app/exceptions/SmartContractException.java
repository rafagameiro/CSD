package app.exceptions;

public class SmartContractException extends Exception {
    public SmartContractException(String name) {
        super("The smart contract located at " + name + " does not exist.");
    }
}
