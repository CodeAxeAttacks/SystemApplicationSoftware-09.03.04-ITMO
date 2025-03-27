package ru.sayron.server.utility;

import ru.sayron.common.data.Organization;
import ru.sayron.common.exceptions.DatabaseHandlingException;
import ru.sayron.server.Main;

import java.time.LocalDateTime;
import java.util.NavigableSet;
import java.util.TreeSet;

public class CollectionManager {
    private NavigableSet<Organization> organizationsCollection;
    private LocalDateTime lastInitTime;
    private DatabaseCollectionManager databaseCollectionManager;

    public CollectionManager(DatabaseCollectionManager databaseCollectionManager) {
        this.databaseCollectionManager = databaseCollectionManager;

        loadCollection();
    }

    public NavigableSet<Organization> getCollection() {
        return organizationsCollection;
    }

    /**
     * @return Last initialization time or null if there wasn't initialization.
     */
    public LocalDateTime getLastInitTime() {
        return lastInitTime;
    }

    /**
     * @return Name of the collection's type.
     */
    public String collectionType() {
        return organizationsCollection.getClass().getName();
    }

    /**
     * @return Size of the collection.
     */
    public int collectionSize() {
        return organizationsCollection.size();
    }

    /**
     * @param id ID of the organization.
     * @return A organization by his ID or null if organization isn't found.
     */
    public Organization getById(Long id) {
        for (Organization organization : organizationsCollection) {
            if (organization.getId() == id) return organization;
        }
        return null;
    }

    /**
     * @param organizationToFind A organization who's value will be found.
     * @return A organization by his value or null if organization isn't found.
     */
    public Organization getByValue(Organization organizationToFind) {
        for (Organization organization : organizationsCollection) {
            if (organization.equals(organizationToFind)) return organization;
        }
        return null;
    }

    public int countByEmployeesCount(Long countByEmployeesCount) {
        int count = 0;
        for (Organization organization : organizationsCollection) {
            if (organization.getEmployeesCount().equals(countByEmployeesCount)) {
                count += 1;
            }
        }
        return count;
    }

    public String containsNameFilteredInfo(String containsNameToFilter) {
        String info = "";
        for (Organization organization : organizationsCollection) {
            if (organization.getName().contains(containsNameToFilter)) {
                info += organization + "\n\n";
            }
        }
        return info.trim();
    }

    public String employeesCountFilteredInfo(Long employeesCountToFilter) {
        String info = "";
        for (Organization organization : organizationsCollection) {
            if (organization.getEmployeesCount() > employeesCountToFilter) {
                info += organization + "\n\n";
            }
        }
        return info.trim();
    }

    /**
     * @return Collection content or corresponding string if collection is empty.
     */
    public String showCollection() {
        if (organizationsCollection.isEmpty()) return "Коллекция пуста!";
        return organizationsCollection.stream().reduce("", (sum, m) -> sum += m + "\n\n", (sum1, sum2) -> sum1 + sum2).trim();
    }

    public void addToCollection(Organization organization) {
        organizationsCollection.add(organization);
    }

    /**
     * Removes a new organization to collection.
     * @param organization A organization to remove.
     */
    public void removeFromCollection(Organization organization) {
        organizationsCollection.remove(organization);
    }

    /**
     * Remove organization greater than the selected one.
     * @param organizationToCompare A organization to compare with.
     */
    public void removeGreater(Organization organizationToCompare) {
        organizationsCollection.removeIf(organization -> organization.compareTo(organizationToCompare) > 0);
    }

    /**
     * Remove organizations greater than the selected one.
     *
     * @param organizationToCompare A organization to compare with.
     * @return Greater organizations list.
     */
    public NavigableSet<Organization> getGreater(Organization organizationToCompare) {
        return organizationsCollection.stream().filter(marine -> marine.compareTo(organizationToCompare) > 0).collect(
                TreeSet::new,
                TreeSet::add,
                TreeSet::addAll
        );
    }

    /**
     * Remove organization lower than the selected one.
     * @param organizationToCompare A organization to compare with.
     */
    public void removeLower(Organization organizationToCompare) {
        organizationsCollection.removeIf(organization -> organization.compareTo(organizationToCompare) < 0);
    }

    /**
     * Remove organizations lower than the selected one.
     *
     * @param organizationToCompare A organization to compare with.
     * @return Lower organizations list.
     */
    public NavigableSet<Organization> getLower(Organization organizationToCompare) {
        return organizationsCollection.stream().filter(marine -> marine.compareTo(organizationToCompare) < 0).collect(
                TreeSet::new,
                TreeSet::add,
                TreeSet::addAll
        );
    }

    /**
     * Clears the collection.
     */
    public void clearCollection() {
        organizationsCollection.clear();
    }

    /**
     * Loads the collection from file.
     */
    private void loadCollection() {
        try {
            organizationsCollection = databaseCollectionManager.getCollection();
            lastInitTime = LocalDateTime.now();
            Outputer.println("Коллекция загружена.");
            Main.logger.info("Коллекция загружена.");
        } catch (DatabaseHandlingException exception) {
            organizationsCollection = new TreeSet<>();
            Outputer.printerror("Коллекция не может быть загружена!");
            Main.logger.error("Коллекция не может быть загружена!");
        }
    }

    @Override
    public String toString() {
        if (organizationsCollection.isEmpty()) return "The collection is empty!";

        String info = "";
        for (Organization organization : organizationsCollection) {
            info += organization;
            if (organization != organizationsCollection.last()) info += "\n\n";
        }
        return info;
    }


}
