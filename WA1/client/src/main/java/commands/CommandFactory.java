package commands;

import exceptions.InvalidCommandException;

import java.util.function.Function;

public class CommandFactory {

    private static final String SPACE = " ";
    private static final String REGISTER = "register";
    private static final String LOGIN = "login";
    private static final String LOGOUT = "logout";
    private static final String BALANCE = "balance";
    private static final String DEPOSIT = "deposit";
    private static final String TRANSFER = "transfer";
    private static final String LEDGER_CLIENT = "ledgerclient";
    private static final String LEDGER_GLOBAL = "ledgerglobal";
    private static final String EXIT = "exit";
    private static final String TEST = "test";
    private static final String HELP = "help";
    private static final String ERROR_MSG = "Inserted command was used incorrectly, usage: %s";
    private static final String UNKNOWN_COMMAND_MESSAGE = "Command does not exist, type help!";

    public static Command from(String line) throws InvalidCommandException {
        String[] parts = line.split(SPACE);
        if (parts.length < 1) {
            throw new InvalidCommandException(UNKNOWN_COMMAND_MESSAGE);
        }
        switch (parts[0]) {
            case TEST:
                return processCommand(parts, 1, a -> new TestCommand(), TestCommand.HELP);
            case EXIT:
                return processCommand(parts, 1, a -> new ExitCommand(), ExitCommand.HELP);
            case HELP:
                return processCommand(parts, 1, a -> new HelpCommand(), HelpCommand.HELP);
            case REGISTER:
                return processCommand(parts, 3, a -> new RegisterUserCommand(a[1], a[2]),
                        RegisterUserCommand.HELP);
            case LOGIN:
                return processCommand(parts, 3, a -> new LoginCommand(a[1], a[2]),
                        LoginCommand.HELP);
            case LOGOUT:
                return processCommand(parts, 1, a -> new LogoutCommand(), LogoutCommand.HELP);
            case BALANCE:
                return processCommand(parts, 1, a -> new BalanceCommand(), BalanceCommand.HELP);
            case DEPOSIT:
                return processCommand(parts, 2, a -> new DepositCommand(a[1]),
                        DepositCommand.HELP);
            case TRANSFER:
                return processCommand(parts, 3, a -> new TransferCommand(a[1], a[2]),
                        TransferCommand.HELP);
            case LEDGER_CLIENT:
                return processCommand(parts, 2, a -> new LedgerClientCommand(a[1]),
                        LedgerClientCommand.HELP);
            case LEDGER_GLOBAL:
                return processCommand(parts, 1, a -> new LedgerGlobalCommand(), LedgerGlobalCommand.HELP);
            default:
                throw new InvalidCommandException(UNKNOWN_COMMAND_MESSAGE);
        }
    }

    private static Command processCommand(String[] parts, int nArgs, Function<String[], Command> success, String errorMsg)
            throws InvalidCommandException {
        if (parts.length == nArgs) {
            return success.apply(parts);
        }
        throw new InvalidCommandException(String.format(ERROR_MSG, errorMsg));
    }

}
