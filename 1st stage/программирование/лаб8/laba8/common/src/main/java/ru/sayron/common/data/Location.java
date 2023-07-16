package ru.sayron.common.data;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

import java.io.Serializable;
import java.util.Objects;

public class Location implements Serializable {
    @JacksonXmlProperty(localName = "x")
    private int x;
    @JacksonXmlProperty(localName = "y")
    private Float y; //Поле не может быть null
    @JacksonXmlProperty(localName = "z")
    private long z;

    public Location() {
    }

    public Location(int x, Float y, long z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public int getX() {
        return x;
    }

    public Float getY() {
        return y;
    }

    public long getZ() {
        return z;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj instanceof Location) {
            Location locationObj = (Location) obj;
            return (x == locationObj.getX()) && y.equals(locationObj.getY()) && (z == locationObj.getZ());
        }
        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y, z);
    }

    @Override
    public String toString() {
        return x + " " + y + " " + z;
    }
}