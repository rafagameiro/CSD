package app.commands;

import app.exceptions.InvalidCommandException;
import app.exceptions.UserAlreadyLoggedInException;
import app.exceptions.UserNotLoggedInException;
import app.models.UserModel;

public class CommandValidator {

    public static void validateCommand(Command cmd, UserModel user) throws InvalidCommandException {
        if (cmd instanceof LoginCommand || cmd instanceof RegisterUserCommand) {
            if (!user.isEmpty()) {
                throw new UserAlreadyLoggedInException();
            }
        } else if (!(cmd instanceof TestCommand || cmd instanceof ExitCommand || cmd instanceof HelpCommand)
                && user.isEmpty())
            throw new UserNotLoggedInException();
    }

}
