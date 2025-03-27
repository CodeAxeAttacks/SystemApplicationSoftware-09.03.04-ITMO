package ru.sayron.common.data;

public enum OrganizationType {
    PUBLIC,
    GOVERNMENT,
    OPEN_JOINT_STOCK_COMPANY;

    public static String nameList() {
        String nameList = "";
        for (OrganizationType organizationType : values()) {
            nameList += organizationType.name() + ", ";
        }
        return nameList.substring(0, nameList.length()-2);
    }
}