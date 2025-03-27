package ru.sayron.server.commands;

import ru.sayron.common.exceptions.*;
import ru.sayron.common.interaction.User;
import ru.sayron.common.utility.Outputer;
import ru.sayron.server.utility.CollectionManager;

public class EmployeesCountCommand extends AbstractCommand {
    private CollectionManager collectionManager;

    public EmployeesCountCommand(CollectionManager collectionManager) {
        super("count_by_employees_count","<employeesCount>",
                "вывести количество элементов, значение поля employeesCount которых равно заданному");
        this.collectionManager = collectionManager;
    }

    /**
     * Executes the command.
     * @return Command exit status.
     */
    @Override
    public boolean execute(String argument, Object objectArgument, User user) {
        try {
            if (argument.isEmpty() || objectArgument != null) throw new WrongAmountOfElementsException();
            if (collectionManager.collectionSize() == 0) throw new CollectionIsEmptyException();
            Long employeesCount = Long.parseLong(argument);
            String filteredInfo = String.valueOf(collectionManager.countByEmployeesCount(employeesCount));
            if (!filteredInfo.isEmpty()) {
                Outputer.println(filteredInfo);
                return true;
            } else Outputer.println("There are no organizations in the collection with an equal number of employees!");
        } catch (WrongAmountOfElementsException exception) {
            Outputer.println("Usage: '" + getName() + "'");
        } catch (CollectionIsEmptyException exception) {
            Outputer.printerror("The collection is empty!");
        } catch (IllegalArgumentException exception) {
            Outputer.printerror("Organization not listed!");
        }
        return false;
    }
}