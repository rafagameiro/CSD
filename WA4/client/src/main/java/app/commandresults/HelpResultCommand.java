package app.commandresults;

import app.commands.*;

public class HelpResultCommand implements CommandResult {

    private static final String DELIMITER = "\n";
    private static final String SEPARATOR = "===========================================================================";
    private static final String HEADER = "Commands";

    private boolean option;
    public HelpResultCommand(boolean init) {
        option = init;
    }

    public HelpResultCommand() {
        option = false;
    }

    @Override
    public void show() {
        if(option) {
            showInitialHelp();
        } else {
            showLoggedHelp();
        }
    }

    private void showLoggedHelp() {
        System.out.println(DELIMITER+HEADER);
        System.out.println(SEPARATOR);
        System.out.println(DepositCommand.HELP);
        System.out.println(TransferCommand.HELP);
        System.out.println(BalanceCommand.HELP);
        System.out.println(LedgerGlobalCommand.HELP);
        System.out.println(LedgerClientCommand.HELP);
        System.out.println(InstallSmartContractCommand.HELP);
        System.out.println(RemoveSmartContractCommand.HELP);
        System.out.println(ManualCommand.HELP);
        System.out.println(HelpCommand.HELP);
        System.out.println(LogoutCommand.HELP);
        System.out.println(ExitCommand.HELP);
        System.out.println(SEPARATOR+DELIMITER);
    }

    private void showInitialHelp() {
        System.out.println(DELIMITER+HEADER);
        System.out.println(SEPARATOR);
        System.out.println(LoginCommand.HELP);
        System.out.println(RegisterUserCommand.HELP);
        System.out.println(TestCommand.HELP);
        System.out.println(ManualCommand.HELP);
        System.out.println(HelpCommand.HELP);
        System.out.println(ExitCommand.HELP);
        System.out.println(SEPARATOR+DELIMITER);
    }

}
