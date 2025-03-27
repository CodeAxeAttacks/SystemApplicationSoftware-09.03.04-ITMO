package ru.sayron.server.commands;

import ru.sayron.common.exceptions.DatabaseHandlingException;
import ru.sayron.common.exceptions.UserAlreadyExists;
import ru.sayron.common.exceptions.WrongAmountOfElementsException;
import ru.sayron.common.interaction.User;
import ru.sayron.server.utility.DatabaseUserManager;
import ru.sayron.server.utility.ResponseOutputer;

/**
 * Command 'register'. Allows the user to register.
 */
public class RegisterCommand extends AbstractCommand {
    private DatabaseUserManager databaseUserManager;

    public RegisterCommand(DatabaseUserManager databaseUserManager) {
        super("register", "", "внутренняя команда");
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
            if (!databaseUserManager.insertUser(user)) throw new UserAlreadyExists();
            ResponseOutputer.appendln("UserRegistered");
            ResponseOutputer.appendargs(user.getUsername());
            return true;
        } catch (WrongAmountOfElementsException exception) {
            ResponseOutputer.appendln("Using");
            ResponseOutputer.appendargs(getName() + " " + getUsage() + "'");
        } catch (ClassCastException exception) {
            ResponseOutputer.appenderror("ClientObjectException");
        } catch (DatabaseHandlingException exception) {
            ResponseOutputer.appenderror("DatabaseHandlingException");
        } catch (UserAlreadyExists exception) {
            ResponseOutputer.appendln("UserExistsException");
            ResponseOutputer.appendargs(user.getUsername());
        }
        return false;
    }
}
