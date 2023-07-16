package ru.sayron.common.data;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

import java.io.Serializable;
import java.util.Objects;

public class Address implements Serializable {
    @JacksonXmlProperty(localName = "street")
    private String street; //Строка не может быть пустой, Поле не может быть null
    @JacksonXmlProperty(localName = "town")
    private Location town; //Поле может быть null

    public Address() {
    }

    public Address(String street, Location town) {
        this.street = street;
        this.town = town;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public Location getTown() {
        return town;
    }

    public void setTown(Location town) {
        this.town = town;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj instanceof Address) {
            Address addressObj = (Address) obj;
            return street.equals(addressObj.getStreet()) && (town.equals(addressObj.getTown()));
        }
        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hash(street, town);
    }

    @Override
    public String toString() {
        return street + " by coordinates " + town;
    }
}
