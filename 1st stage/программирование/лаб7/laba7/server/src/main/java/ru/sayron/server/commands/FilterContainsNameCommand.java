package ru.sayron.server.commands;

import ru.sayron.common.data.Organization;
import ru.sayron.common.exceptions.*;
import ru.sayron.common.interaction.User;
import ru.sayron.common.utility.Outputer;
import ru.sayron.server.utility.CollectionManager;

public class FilterContainsNameCommand extends AbstractCommand {
    private Organization organization;
    private CollectionManager collectionManager;

    public FilterContainsNameCommand(CollectionManager collectionManager) {
        super("filter_contains_name","<name>",
                "display elements whose name field value contains the given substring");
        this.collectionManager = collectionManager;
    }

    @Override
    public boolean execute(String argument, Object objectArgument, User user) {
        try {
            if (argument.isEmpty() || objectArgument != null) throw new WrongAmountOfElementsException();
            if (collectionManager.collectionSize() == 0) throw new CollectionIsEmptyException();
            String containsName = argument;
            String filteredInfo = collectionManager.containsNameFilteredInfo(containsName);
            if (!filteredInfo.isEmpty()) {
                Outputer.println(filteredInfo);
                return true;
            } else Outputer.println("There are no organizations in the collection with more employees than the specified number!");
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