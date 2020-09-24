package app.commands;

import app.exceptions.InvalidCommandException;

import java.util.function.Function;

public class CommandFactory {

    private static final String SPACE = " ";
    private static final String ERROR_MSG = "Inserted command was used incorrectly, type 'man %s' for more info!";
    private static final String UNKNOWN_COMMAND_MESSAGE = "Command does not exist, type help!";

    public static Command fromStart(String line) throws InvalidCommandException {
        String[] parts = line.split(SPACE);
        if (parts.length < 1) {
            throw new InvalidCommandException(UNKNOWN_COMMAND_MESSAGE);
        }
        switch (parts[0]) {
            case LoginCommand.COMMAND:
                return processCommand(parts, 4, a -> new LoginCommand(a[1], a[2], a[3]),
                        LoginCommand.COMMAND);
            case RegisterUserCommand.COMMAND:
                return processCommand(parts, 4, a -> new RegisterUserCommand(a[1], a[2], a[3]),
                        RegisterUserCommand.COMMAND);
            case TestCommand.COMMAND:
                return processCommand(parts, 1, a -> new TestCommand(), TestCommand.COMMAND);
            case ManualCommand.COMMAND:
                return processCommand(parts, 2, a -> new ManualCommand(a[1]), ManualCommand.COMMAND);
            case HelpCommand.COMMAND:
                return processCommand(parts, 1, a -> new HelpCommand(), HelpCommand.COMMAND);
            case ExitCommand.COMMAND:
                return processCommand(parts, 1, a -> new ExitCommand(), ExitCommand.COMMAND);
            default:
                throw new InvalidCommandException(UNKNOWN_COMMAND_MESSAGE);
        }
    }

    public static Command from(String line) throws InvalidCommandException {
        String[] parts = line.split(SPACE);
        if (parts.length < 1) {
            throw new InvalidCommandException(UNKNOWN_COMMAND_MESSAGE);
        }
        switch (parts[0]) {
            case BalanceCommand.COMMAND:
                return processCommand(parts, 1, a -> new BalanceCommand(), BalanceCommand.COMMAND);
            case DepositCommand.COMMAND:
                return processCommand(parts, 2, a -> new DepositCommand(a[1]),
                        DepositCommand.COMMAND);
            case TransferCommand.COMMAND:
                return processCommand(parts, 3, a -> new TransferCommand(a[1], a[2]),
                        TransferCommand.COMMAND);
            case LedgerClientCommand.COMMAND:
                return processCommand(parts, 2, a -> new LedgerClientCommand(a[1]),
                        LedgerClientCommand.COMMAND);
            case LedgerGlobalCommand.COMMAND:
                return processCommand(parts, 1, a -> new LedgerGlobalCommand(), LedgerGlobalCommand.COMMAND);
            case InstallSmartContractCommand.COMMAND:
                return processCommand(parts, 2, a -> new InstallSmartContractCommand(a[1]), InstallSmartContractCommand.COMMAND);
            case RemoveSmartContractCommand.COMMAND:
                return processCommand(parts, 1, a -> new RemoveSmartContractCommand(), RemoveSmartContractCommand.COMMAND);
            case ManualCommand.COMMAND:
                return processCommand(parts, 2, a -> new ManualCommand(a[1]), ManualCommand.COMMAND);
            case HelpCommand.COMMAND:
                return processCommand(parts, 1, a -> new HelpCommand(), HelpCommand.COMMAND);
            case LogoutCommand.COMMAND:
                return processCommand(parts, 1, a -> new LogoutCommand(), LogoutCommand.COMMAND);
            case ExitCommand.COMMAND:
                return processCommand(parts, 1, a -> new ExitCommand(), ExitCommand.COMMAND);
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
