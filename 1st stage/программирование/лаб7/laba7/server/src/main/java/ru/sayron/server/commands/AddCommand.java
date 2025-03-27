package ru.sayron.server.commands;

import ru.sayron.common.data.Organization;
import ru.sayron.common.exceptions.DatabaseHandlingException;
import ru.sayron.common.exceptions.WrongAmountOfElementsException;
import ru.sayron.common.interaction.OrganizationRaw;
import ru.sayron.common.interaction.User;
import ru.sayron.common.utility.Outputer;
import ru.sayron.server.utility.CollectionManager;
import ru.sayron.server.utility.DatabaseCollectionManager;
import ru.sayron.server.utility.ResponseOutputer;

import java.time.LocalDateTime;

/**
 * Command 'add'. Adds a new element to collection.
 */
public class AddCommand extends AbstractCommand {
    private CollectionManager collectionManager;
    private DatabaseCollectionManager databaseCollectionManager;

    public AddCommand(CollectionManager collectionManager, DatabaseCollectionManager databaseCollectionManager) {
        super("add", "{element}", "добавить новый элемент в коллекцию");
        this.collectionManager = collectionManager;
        this.databaseCollectionManager = databaseCollectionManager;
    }

    /**
     * Executes the command.
     * @return Command exit status.
     */
    @Override
    public boolean execute(String stringArgument, Object objectArgument, User user) {
        try {
            if (!stringArgument.isEmpty() || objectArgument == null) throw new WrongAmountOfElementsException();
            OrganizationRaw organizationRaw = (OrganizationRaw) objectArgument;
            collectionManager.addToCollection(databaseCollectionManager.insertOrganization(organizationRaw, user));
            Outputer.println("Organization added successfully!");
            return true;
        } catch (WrongAmountOfElementsException exception) {
            ResponseOutputer.appendln("Usage: '" + getName() + " " + getUsage() + "'");
        } catch (ClassCastException exception) {
            ResponseOutputer.appenderror("The object passed by the client is invalid!");
        } catch (DatabaseHandlingException exception) {
            exception.printStackTrace();
            ResponseOutputer.appenderror("Произошла ошибка при обращении к базе данных!");
        }
        return false;
    }
}