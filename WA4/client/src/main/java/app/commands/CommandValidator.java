package app.commands;

import app.exceptions.InvalidCommandException;
import app.exceptions.SmartContractNotInstalledException;
import app.exceptions.UserAlreadyLoggedInException;
import app.exceptions.UserNotLoggedInException;
import app.models.AppUser;
import app.models.User;

public class CommandValidator {

    public static void validateCommand(Command cmd, AppUser user) throws InvalidCommandException {
        if (cmd instanceof RemoveSmartContractCommand && user.getContractFilePath() == null ) {
            System.out.println("WHAT YPP CUZZZZZZZZZZZ");;
        }
    }

}
