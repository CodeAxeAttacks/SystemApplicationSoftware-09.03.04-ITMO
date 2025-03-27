package ru.sayron.common.data;

import ru.sayron.common.interaction.User;

import java.time.LocalDateTime;
import java.util.Objects;

public class Organization implements Comparable<Organization> {
    private Long id; //Поле не может быть null, Значение поля должно быть больше 0, Значение этого поля должно быть уникальным, Значение этого поля должно генерироваться автоматически
    private String name; //Поле не может быть null, Строка не может быть пустой
    private Coordinates coordinates; //Поле не может быть null
    private java.time.LocalDateTime creationDate; //Поле не может быть null, Значение этого поля должно генерироваться автоматически
    private int annualTurnover; //Значение поля должно быть больше 0
    private String fullName; //Значение этого поля должно быть уникальным, Строка не может быть пустой, Поле может быть null
    private Long employeesCount; //Поле не может быть null, Значение поля должно быть больше 0
    private OrganizationType type; //Поле может быть null
    private Address officialAddress; //Поле не может быть null
    private User owner;

    public Organization() {}

    public Organization(Long id, String name, Coordinates coordinates, LocalDateTime creationDate, int annualTurnover,
                        String fullName, Long employeesCount, OrganizationType type, Address officialAddress, User user) {
        this.id = id;
        this.name = name;
        this.coordinates = coordinates;
        this.creationDate = creationDate;
        this.annualTurnover = annualTurnover;
        this.fullName = fullName;
        this.employeesCount = employeesCount;
        this.type = type;
        this.officialAddress = officialAddress;
        this.owner = user;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Coordinates getCoordinates() {
        return coordinates;
    }

    public LocalDateTime getCreationDate() {
        return creationDate;
    }

    public int getAnnualTurnover() {
        return annualTurnover;
    }

    public String getFullName() {
        return fullName;
    }

    public Long getEmployeesCount() {
        return employeesCount;
    }

    public OrganizationType getType() {
        return type;
    }

    public Address getOfficialAddress() {
        return officialAddress;
    }

    /**
     * @return Owner of the organization.
     */
    public User getOwner() {
        return owner;
    }

    @Override
    public int compareTo(Organization organizationObj) {
        return id.compareTo(organizationObj.getId());
    }

    @Override
    public String toString() {
        String info = "";
        info += "Organization №" + id;
        info += " (added " + owner.getUsername() + creationDate.toLocalDate() + " " + creationDate.toLocalTime() + ")";
        info += "\n Name: " + name;
        info += "\n Location: " + coordinates;
        info += "\n Annual turnover: " + annualTurnover;
        info += "\n Full name: " + fullName;
        info += "\n Amount of employees: " + employeesCount;
        info += "\n Organization type: " + type;
        info += "\n Address: " + officialAddress;
        return info;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj instanceof Organization) {
            Organization organizationObj = (Organization) obj;
            return name.equals(organizationObj.getName()) && coordinates.equals(organizationObj.getCoordinates()) && (annualTurnover == organizationObj.getAnnualTurnover()) && (fullName.equals(organizationObj.getFullName())) && (employeesCount.equals(organizationObj.getEmployeesCount())) && (type == organizationObj.getType()) && officialAddress.equals(organizationObj.getOfficialAddress());
        }
        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, coordinates, creationDate, annualTurnover, fullName, employeesCount, type, officialAddress);
    }
}
