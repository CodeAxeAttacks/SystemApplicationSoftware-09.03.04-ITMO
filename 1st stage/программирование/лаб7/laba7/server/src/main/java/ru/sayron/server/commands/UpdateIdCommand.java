package ru.sayron.server.commands;

import ru.sayron.common.data.*;
import ru.sayron.common.exceptions.*;
import ru.sayron.common.interaction.OrganizationRaw;
import ru.sayron.common.interaction.User;
import ru.sayron.server.utility.CollectionManager;
import ru.sayron.server.utility.DatabaseCollectionManager;
import ru.sayron.server.utility.ResponseOutputer;

import java.time.LocalDateTime;

/**
 * Command 'update'. Updates the information about selected marine.
 */
public class UpdateIdCommand extends AbstractCommand {
    private CollectionManager collectionManager;
    private DatabaseCollectionManager databaseCollectionManager;

    public UpdateIdCommand(CollectionManager collectionManager, DatabaseCollectionManager databaseCollectionManager) {
        super("update","<ID> {element}", "update collection element value by ID");
        this.collectionManager = collectionManager;
        this.databaseCollectionManager = databaseCollectionManager;
    }

    /**
     * Executes the command.
     * @return Command exit status.
     */
    @Override
    public boolean execute(String argument, Object objectArgument, User user) {
        try {
            if (argument.isEmpty() || objectArgument == null) throw new WrongAmountOfElementsException();
            if (collectionManager.collectionSize() == 0) throw new CollectionIsEmptyException();

            long id = Long.parseLong(argument);
            if (id <= 0) throw new NumberFormatException();
            Organization organization = collectionManager.getById(id);
            if (organization == null) throw new OrganizationNotFoundException();
            if (!organization.getOwner().equals(user)) throw new PermissionDeniedException();
            if (!databaseCollectionManager.checkOrganizationUserId(organization.getId(), user)) throw new ManualDatabaseEditException();
            OrganizationRaw organizationRaw = (OrganizationRaw) objectArgument;

            databaseCollectionManager.updateOrganizationById(id, organizationRaw);

            String name = organizationRaw.getName() == null ? organization.getName() : organizationRaw.getName();
            Coordinates coordinates = organizationRaw.getCoordinates() == null ? organization.getCoordinates() : organizationRaw.getCoordinates();
            LocalDateTime creationDate = organization.getCreationDate();
            int turnover = organizationRaw.getAnnualTurnover() == -1 ? organization.getAnnualTurnover() : organizationRaw.getAnnualTurnover();
            String fullName = organizationRaw.getFullName() == null ? organization.getFullName() : organizationRaw.getFullName();
            Long employeesCount = organizationRaw.getEmployeesCount() == null ? organization.getEmployeesCount() : organizationRaw.getEmployeesCount();
            OrganizationType type = organizationRaw.getType() == null ? organization.getType() : organizationRaw.getType();
            Address officialAddress = organizationRaw.getOfficialAddress() == null ? organization.getOfficialAddress() : organizationRaw.getOfficialAddress();

            collectionManager.removeFromCollection(organization);
            collectionManager.addToCollection(new Organization(
                    id,
                    name,
                    coordinates,
                    creationDate,
                    turnover,
                    fullName,
                    employeesCount,
                    type,
                    officialAddress,
                    user
            ));
            ResponseOutputer.appendln("The organization has been successfully changed!");
            return true;
        } catch (WrongAmountOfElementsException exception) {
            ResponseOutputer.appendln("Usage: '" + getName() + "'");
        } catch (CollectionIsEmptyException exception) {
            ResponseOutputer.appenderror("The collection is empty!");
        } catch (NumberFormatException exception) {
            ResponseOutputer.appenderror("ID must be represented by a number!");
        } catch (OrganizationNotFoundException exception) {
            ResponseOutputer.appenderror("There is no organization with this ID in the collection!");
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