package ru.sayron.server.commands;

import ru.sayron.common.data.Organization;
import ru.sayron.common.exceptions.*;
import ru.sayron.common.interaction.OrganizationRaw;
import ru.sayron.common.interaction.User;
import ru.sayron.common.utility.Outputer;
import ru.sayron.server.utility.CollectionManager;
import ru.sayron.server.utility.DatabaseCollectionManager;
import ru.sayron.server.utility.ResponseOutputer;

import java.time.LocalDateTime;

/**
 * Command 'remove_lower'. Removes elements lower than user entered.
 */
public class RemoveLowerCommand extends AbstractCommand {
    private CollectionManager collectionManager;
    private DatabaseCollectionManager databaseCollectionManager;

    public RemoveLowerCommand(CollectionManager collectionManager, DatabaseCollectionManager databaseCollectionManager) {
        super("remove_lower","{element}", "remove from the collection all elements smaller than the given one");
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
            if (collectionManager.collectionSize() == 0) throw new CollectionIsEmptyException();
            OrganizationRaw organizationRaw = (OrganizationRaw) objectArgument;
            Organization organizationToFind = new Organization(
                    0L,
                    organizationRaw.getName(),
                    organizationRaw.getCoordinates(),
                    LocalDateTime.now(),
                    organizationRaw.getAnnualTurnover(),
                    organizationRaw.getFullName(),
                    organizationRaw.getEmployeesCount(),
                    organizationRaw.getType(),
                    organizationRaw.getOfficialAddress(),
                    user
            );
            Organization organizationFromCollection = collectionManager.getByValue(organizationToFind);
            if (organizationFromCollection == null) throw new OrganizationNotFoundException();
            for (Organization organization : collectionManager.getLower(organizationFromCollection)) {
                if (!organization.getOwner().equals(user)) throw new PermissionDeniedException();
                if (!databaseCollectionManager.checkOrganizationUserId(organization.getId(), user)) throw new ManualDatabaseEditException();
            }
            for (Organization organization : collectionManager.getGreater(organizationFromCollection)) {
                databaseCollectionManager.deleteOrganizationById(organization.getId());
                collectionManager.removeFromCollection(organization);
            }
            Outputer.println("Organizations deleted successfully!");
            return true;
        } catch (WrongAmountOfElementsException exception) {
            Outputer.println("Usage: '" + getName() + "'");
        } catch (CollectionIsEmptyException exception) {
            Outputer.printerror("The collection is empty!");
        } catch (OrganizationNotFoundException exception) {
            Outputer.printerror("There are no organizations with such characteristics in the collection!");
        } catch (ClassCastException exception) {
            ResponseOutputer.appenderror("The object passed by the client is invalid!");
        } catch (DatabaseHandlingException exception) {
            ResponseOutputer.appenderror("Произошла ошибка при обращении к базе данных!");
        } catch (PermissionDeniedException exception) {
            ResponseOutputer.appenderror("Недостаточно прав для выполнения данной команды!");
            ResponseOutputer.appendln("Принадлежащие другим пользователям объекты доступны только для чтения.");
        } catch (ManualDatabaseEditException exception) {
            ResponseOutputer.appenderror("Произошло прямое изменение базы данных!");
            ResponseOutputer.appendln("Перезапустите клиент для избежания возможных ошибок.");
        }
        return false;
    }
}