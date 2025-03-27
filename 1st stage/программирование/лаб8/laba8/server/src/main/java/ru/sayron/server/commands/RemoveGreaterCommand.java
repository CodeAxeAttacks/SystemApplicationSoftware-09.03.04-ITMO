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
 * Command 'remove_greater'. Removes elements greater than user entered.
 */
public class RemoveGreaterCommand extends AbstractCommand {
    private CollectionManager collectionManager;
    private DatabaseCollectionManager databaseCollectionManager;

    public RemoveGreaterCommand(CollectionManager collectionManager, DatabaseCollectionManager databaseCollectionManager) {
        super("remove_greater","{element}", "remove from the collection all elements greater than the given");
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
            for (Organization organization : collectionManager.getGreater(organizationFromCollection)) {
                if (!organization.getOwner().equals(user)) throw new PermissionDeniedException();
                if (!databaseCollectionManager.checkOrganizationUserId(organization.getId(), user)) throw new ManualDatabaseEditException();
            }
            for (Organization organization : collectionManager.getGreater(organizationFromCollection)) {
                databaseCollectionManager.deleteOrganizationById(organization.getId());
                collectionManager.removeFromCollection(organization);
            }
            ResponseOutputer.appendln("OrganizationsWasDeleted");
            return true;
        } catch (WrongAmountOfElementsException exception) {
            ResponseOutputer.appendln("Using");
            ResponseOutputer.appendargs(getName() + " " + getUsage() + "'");
        } catch (CollectionIsEmptyException exception) {
            ResponseOutputer.appenderror("CollectionIsEmptyException");
        } catch (OrganizationNotFoundException exception) {
            ResponseOutputer.appenderror("OrganizationException");
        } catch (ClassCastException exception) {
            ResponseOutputer.appenderror("ClientObjectException");
        } catch (DatabaseHandlingException exception) {
            ResponseOutputer.appenderror("DatabaseHandlingException");
        } catch (PermissionDeniedException exception) {
            ResponseOutputer.appenderror("NoughRightsException");
        } catch (ManualDatabaseEditException exception) {
            ResponseOutputer.appenderror("ManualDatabaseException");
        }
        return false;
    }
}
