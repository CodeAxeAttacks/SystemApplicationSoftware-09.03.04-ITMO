package ru.sayron.server.commands;

import ru.sayron.common.exceptions.WrongAmountOfElementsException;
import ru.sayron.common.interaction.User;
import ru.sayron.server.utility.ResponseOutputer;

/**
 * Command 'server_exit'. Checks for wrong arguments then do nothing.
 */
public class ServerExitCommand extends AbstractCommand {

    public ServerExitCommand() {
        super("server_exit", "", "завершить работу сервера");
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
            ResponseOutputer.appendln("Работа сервера успешно завершена!");
            return true;
        } catch (WrongAmountOfElementsException exception) {
            ResponseOutputer.appendln("Использование: '" + getName() + " " + getUsage() + "'");
        }
        return false;
    }
}
