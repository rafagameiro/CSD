package app.exceptions;

public class ContractExecutionException extends Exception {
    public ContractExecutionException() {
        super("Contract could not execute. ");
    }
}
