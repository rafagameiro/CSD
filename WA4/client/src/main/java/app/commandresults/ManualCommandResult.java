package app.commandresults;

import app.commands.*;

public class ManualCommandResult implements CommandResult {

    private static final String DELIMITER = "\n";
    private static final String NAME_HEADER = "\nNAME:\n";
    private static final String USAGE_HEADER = "USAGE:\n";
    private static final String OPTIONS_HEADER = "OPTIONS:\n";
    private static final String INVALID_COMMAND = "No manual entry for %s.";

    private String option;
    private boolean init;

    public ManualCommandResult(String option, boolean init) {
        this.option = option;
        this.init = init;
    }

    @Override
    public void show() {
        if (init) {
            showStartEntries();
        } else {
            showLoggedEntries();
        }
    }

    private void showStartEntries() {
        switch (option) {
            case LoginCommand.COMMAND:
                listEntry(LoginCommand.COMMAND, LoginCommand.DESCRIPTION, LoginCommand.USAGE, LoginCommand.OPT_DESCRIPTION);
                break;
            case RegisterUserCommand.COMMAND:
                listEntry(RegisterUserCommand.COMMAND, RegisterUserCommand.DESCRIPTION, RegisterUserCommand.USAGE, RegisterUserCommand.OPT_DESCRIPTION);
                break;
            case HelpCommand.COMMAND:
                listEntry(HelpCommand.COMMAND, HelpCommand.DESCRIPTION, HelpCommand.USAGE, HelpCommand.OPT_DESCRIPTION);
                break;
            default:
                System.out.println(String.format(INVALID_COMMAND, option));
        }
    }

    private void showLoggedEntries() {
        switch (option) {
            case BalanceCommand.COMMAND:
                listEntry(BalanceCommand.COMMAND, BalanceCommand.DESCRIPTION, BalanceCommand.USAGE, BalanceCommand.OPT_DESCRIPTION);
                break;
            case DepositCommand.COMMAND:
                listEntry(DepositCommand.COMMAND, DepositCommand.DESCRIPTION, DepositCommand.USAGE, DepositCommand.OPT_DESCRIPTION);
                break;
            case TransferCommand.COMMAND:
                listEntry(TransferCommand.COMMAND, TransferCommand.DESCRIPTION, TransferCommand.USAGE, TransferCommand.OPT_DESCRIPTION);
                break;
            case LedgerGlobalCommand.COMMAND:
                listEntry(LedgerGlobalCommand.COMMAND, LedgerGlobalCommand.DESCRIPTION, LedgerGlobalCommand.USAGE, LedgerGlobalCommand.OPT_DESCRIPTION);
                break;
            case LedgerClientCommand.COMMAND:
                listEntry(LedgerClientCommand.COMMAND, LedgerClientCommand.DESCRIPTION, LedgerClientCommand.USAGE, LedgerClientCommand.OPT_DESCRIPTION);
                break;
            case InstallSmartContractCommand.COMMAND:
                listEntry(InstallSmartContractCommand.COMMAND, InstallSmartContractCommand.DESCRIPTION, InstallSmartContractCommand.USAGE, InstallSmartContractCommand.OPT_DESCRIPTION);
                break;
            case RemoveSmartContractCommand.COMMAND:
                listEntry(RemoveSmartContractCommand.COMMAND, RemoveSmartContractCommand.DESCRIPTION, RemoveSmartContractCommand.USAGE, RemoveSmartContractCommand.OPT_DESCRIPTION);
                break;
            case HelpCommand.COMMAND:
                listEntry(HelpCommand.COMMAND, HelpCommand.DESCRIPTION, HelpCommand.USAGE, HelpCommand.OPT_DESCRIPTION);
                break;
            default:
                System.out.println(String.format(INVALID_COMMAND, option));
        }
    }

    private void listEntry(String command, String synopsis, String usage, String[] descriptions) {
        System.out.println(NAME_HEADER + "\t\t\t" + command + " - " + synopsis + DELIMITER);
        System.out.println(USAGE_HEADER + "\t\t\t" + usage + DELIMITER);
        System.out.println(OPTIONS_HEADER);
        String[] options = usage.split(" ");
        for (int i = 1; i < options.length; i++)
            System.out.println("\t\t\t" + options[i] + "\t" + descriptions[i - 1] + DELIMITER);

    }

}
