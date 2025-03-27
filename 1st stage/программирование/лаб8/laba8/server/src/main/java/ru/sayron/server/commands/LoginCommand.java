package ru.sayron.server.commands;

import ru.sayron.common.exceptions.DatabaseHandlingException;
import ru.sayron.common.exceptions.UserIsNotFoundException;
import ru.sayron.common.exceptions.WrongAmountOfElementsException;
import ru.sayron.common.interaction.User;
import ru.sayron.server.utility.DatabaseUserManager;
import ru.sayron.server.utility.ResponseOutputer;

/**
 * Command 'login'. Allows the user to login.
 */
public class LoginCommand extends AbstractCommand {
    private DatabaseUserManager databaseUserManager;

    public LoginCommand(DatabaseUserManager databaseUserManager) {
        super("login", "", "внутренняя команда");
        this.databaseUserManager = databaseUserManager;
    }

    /**
     * Executes the command.
     *
     * @return Command exit status.
     */
    @Override
    public boolean execute(String stringArgument, Object objectArgument, User user) {
        try {
            if (!stringArgument.isEmpty() || objectArgument != null) throw new WrongAmountOfElementsException();
            if (!databaseUserManager.checkUserByUsernameAndPassword(user)) throw new UserIsNotFoundException();
            ResponseOutputer.appendln("UserAuthorized");
            ResponseOutputer.appendargs(user.getUsername());
            return true;
        } catch (WrongAmountOfElementsException exception) {
            ResponseOutputer.appendln("Using");
            ResponseOutputer.appendargs(getName() + " " + getUsage() + "'");
        } catch (ClassCastException exception) {
            ResponseOutputer.appenderror("ClientObjectException");
        } catch (DatabaseHandlingException exception) {
            ResponseOutputer.appenderror("DatabaseHandlingException");
        } catch (UserIsNotFoundException exception) {
            ResponseOutputer.appenderror("InvalidUserException");
        }
        return false;
    }
}
