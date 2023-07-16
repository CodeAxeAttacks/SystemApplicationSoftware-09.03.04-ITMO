package ru.sayron.common.interaction;

import ru.sayron.common.data.*;

import java.io.Serializable;

public class OrganizationRaw implements Serializable {
    private String name;
    private Coordinates coordinates;
    private int annualTurnover;
    private String fullName;
    private Long employeesCount;
    private OrganizationType type;
    private Address officialAddress;

    public OrganizationRaw(String name, Coordinates coordinates, int annualTurnover, String fullName,
                           Long employeesCount, OrganizationType type, Address officialAddress) {
        this.name = name;
        this.coordinates = coordinates;
        this.annualTurnover = annualTurnover;
        this.fullName = fullName;
        this.employeesCount = employeesCount;
        this.type = type;
        this.officialAddress = officialAddress;
    }

    /**
     * @return Name of the organization.
     */
    public String getName() {
        return name;
    }

    /**
     * @return Coordinates of the organization.
     */
    public Coordinates getCoordinates() {
        return coordinates;
    }

    /**
     * @return Annual turnover of the organization.
     */
    public int getAnnualTurnover() {
        return annualTurnover;
    }

    /**
     * @return Full Name of the organization.
     */
    public String getFullName() {
        return fullName;
    }

    /**
     * @return Employees Count of the organization.
     */
    public Long getEmployeesCount() {
        return employeesCount;
    }

    /**
     * @return Type of the organization.
     */
    public OrganizationType getType() {
        return type;
    }

    /**
     * @return Official Address of the organization.
     */
    public Address getOfficialAddress() {
        return officialAddress;
    }

    @Override
    public String toString() {
        String info = "";
        info += "'Raw' organization";
        info += "\n Name: " + name;
        info += "\n Location: " + coordinates;
        info += "\n Annual turnover: " + annualTurnover;
        info += "\n Full name: " + fullName;
        info += "\n The number of employees: " + employeesCount;
        info += "\n Organization type: " + type;
        info += "\n Address: " + officialAddress;
        return info;
    }

    @Override
    public int hashCode() {
        return name.hashCode() + coordinates.hashCode() + (int) annualTurnover + fullName.hashCode() + employeesCount.hashCode() +
                type.hashCode() + officialAddress.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj instanceof Organization) {
            Organization organizationObj = (Organization) obj;
            return name.equals(organizationObj.getName()) && coordinates.equals(organizationObj.getCoordinates()) &&
                    (annualTurnover == organizationObj.getAnnualTurnover()) && (fullName.equals(organizationObj.getFullName())) &&
                    (employeesCount == organizationObj.getEmployeesCount()) && (type == organizationObj.getType()) &&
                    officialAddress.equals(organizationObj.getOfficialAddress());
        }
        return false;
    }
}
