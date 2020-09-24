package app.commandresults;

import app.commands.*;

public class HelpResultCommand implements CommandResult {
    private static final String SEPARATOR = "==========================================================";

    @Override
    public void show() {
        System.out.println("BASIC OPERATIONS:");
        System.out.println(HelpCommand.HELP);
        System.out.println(ExitCommand.HELP);
        System.out.println(TestCommand.HELP);
        System.out.println(RegisterUserCommand.HELP);
        System.out.println(LoginCommand.HELP);
        System.out.println(SEPARATOR);
        System.out.println("OPERATIONS REQUIRING LOGIN:");
        System.out.println(DepositCommand.HELP);
        System.out.println(TransferCommand.HELP);
        System.out.println(BalanceCommand.HELP);
        System.out.println(LedgerGlobalCommand.HELP);
        System.out.println(LedgerClientCommand.HELP);
        System.out.println(LogoutCommand.HELP);
        System.out.println(SEPARATOR);
    }
}
