package ru.sayron.server.commands;

import ru.sayron.common.data.*;
import ru.sayron.common.exceptions.*;
import ru.sayron.common.interaction.OrganizationRaw;
import ru.sayron.server.utility.CollectionManager;
import ru.sayron.server.utility.ResponseOutputer;

import java.time.LocalDateTime;

/**
 * Command 'update'. Updates the information about selected marine.
 */
public class UpdateIdCommand extends AbstractCommand {
    private CollectionManager collectionManager;

    public UpdateIdCommand(CollectionManager collectionManager) {
        super("update","<ID> {element}", "update collection element value by ID");
        this.collectionManager = collectionManager;
    }

    /**
     * Executes the command.
     * @return Command exit status.
     */
    @Override
    public boolean execute(String argument, Object objectArgument) {
        try {
            if (argument.isEmpty() || objectArgument == null) throw new WrongAmountOfElementsException();
            if (collectionManager.collectionSize() == 0) throw new CollectionIsEmptyException();

            Long id = Long.parseLong(argument);
            if (id <= 0) throw new NumberFormatException();
            Organization organization = collectionManager.getById(id);
            if (organization == null) throw new OrganizationNotFoundException();

            OrganizationRaw organizationRaw = (OrganizationRaw) objectArgument;
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
                    officialAddress
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
        }
        return false;
    }
}