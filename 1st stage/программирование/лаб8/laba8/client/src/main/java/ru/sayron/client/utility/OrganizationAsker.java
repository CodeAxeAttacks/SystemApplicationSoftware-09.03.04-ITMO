package ru.sayron.client.utility;

import ru.sayron.client.Main;
import ru.sayron.common.exceptions.*;
import ru.sayron.common.data.*;

import java.util.NoSuchElementException;
import java.util.Scanner;

import static ru.sayron.common.data.Organization.MIN_ANNUALTURNOVER;
import static ru.sayron.common.data.Organization.MIN_EMPLOYEESCOUNT;


/**
 * Asks a user a organization's value.
 */
public class OrganizationAsker {
    private Scanner userScanner;

    public OrganizationAsker(Scanner userScanner) {
        this.userScanner = userScanner;
    }

    public String askName() throws IncorrectInputInScriptException {
        String name;
        try {
            Outputer.println("EnterName");
            Outputer.print(Main.PS2);
            name = userScanner.nextLine().trim();
            Outputer.println(name);
            if (name.equals("")) throw new MustBeNotEmptyException();
            return name;
        } catch (NoSuchElementException exception) {
            Outputer.printerror("NameNotIdentifiedException");
        } catch (MustBeNotEmptyException exception) {
            Outputer.printerror("NameEmptyException");
        } catch (IllegalStateException exception) {
            Outputer.printerror("UnexpectedException");
            OutputerUI.error("UnexpectedException");
            System.exit(0);
        }
        throw new IncorrectInputInScriptException();
    }

    public long askCoordinatesX() throws IncorrectInputInScriptException {
        String strX;
        long x;
        try {
            Outputer.println("EnterX");
            Outputer.print(Main.PS2);
            strX = userScanner.nextLine().trim();
            Outputer.println(strX);
            x = Long.parseLong(strX);
            return x;
        } catch (NoSuchElementException exception) {
            Outputer.printerror("XNotIdentifiedException");
        } catch (NumberFormatException exception) {
            Outputer.printerror("XMustBeNumberException");
        } catch (NullPointerException | IllegalStateException exception) {
            Outputer.printerror("UnexpectedException");
            OutputerUI.error("UnexpectedException");
            System.exit(0);
        }
        throw new IncorrectInputInScriptException();
    }

    public Integer askCoordinatesY() throws IncorrectInputInScriptException {
        String strY;
        Integer y;
        try {
            Outputer.println("EnterY");
            Outputer.print(Main.PS2);
            strY = userScanner.nextLine().trim();
            Outputer.println(strY);
            y = Integer.parseInt(strY);
            if (y > Organization.MAX_Y) throw new NotInDeclaredLimitsException();
            return y;
        } catch (NoSuchElementException exception) {
            Outputer.printerror("YNotIdentifiedException");
        } catch (NotInDeclaredLimitsException exception) {
            Outputer.printerror("YMustBeLessException", String.valueOf(Organization.MAX_Y));
        } catch (NumberFormatException exception) {
            Outputer.printerror("YMustBeNumberException");
        } catch (NullPointerException | IllegalStateException exception) {
            Outputer.printerror("UnexpectedException");
            OutputerUI.error("UnexpectedException");
            System.exit(0);
        }
        throw new IncorrectInputInScriptException();
    }

    public Coordinates askCoordinates() throws IncorrectInputInScriptException {
        long x;
        Integer y;
        x = askCoordinatesX();
        y = askCoordinatesY();
        return new Coordinates(x, y);
    }

    public int askTurnover() throws IncorrectInputInScriptException {
        String strTurnover;
        int turnover;
        try {
            Outputer.println("EnterAnnualTurnover");
            Outputer.print(Main.PS2);
            strTurnover = userScanner.nextLine().trim();
            Outputer.println(strTurnover);
            turnover = Integer.parseInt(strTurnover);
            if (turnover < MIN_ANNUALTURNOVER) throw new NotInDeclaredLimitsException();
            return turnover;
        } catch (NoSuchElementException exception) {
            Outputer.printerror("AnnualTurnoverNotIdentifiedException");
        } catch (NotInDeclaredLimitsException exception) {
            Outputer.printerror("AnnualTurnoverMustBeNumberException", String.valueOf(Organization.MAX_Y));
        } catch (NumberFormatException exception) {
            Outputer.printerror("AnnualTurnoverMustBeMoreZero");
        } catch (NullPointerException | IllegalStateException exception) {
            Outputer.printerror("UnexpectedException");
            OutputerUI.error("UnexpectedException");
            System.exit(0);
        }
        throw new IncorrectInputInScriptException();
    }

    public String askFullName() throws IncorrectInputInScriptException {
        String fullName;
        try {
            Outputer.println("EnterFullName");
            Outputer.print(Main.PS2);
            fullName = userScanner.nextLine().trim();
            Outputer.println(fullName);
            if (fullName.equals("")) throw new MustBeNotEmptyException();
            return fullName;
        } catch (NoSuchElementException exception) {
            Outputer.printerror("FullNameNotIndentifiedException");
        } catch (MustBeNotEmptyException exception) {
            Outputer.printerror("FullNameMustBeNumberException", String.valueOf(Organization.MAX_Y));
        } catch (NullPointerException | IllegalStateException exception) {
            Outputer.printerror("UnexpectedException");
            OutputerUI.error("UnexpectedException");
            System.exit(0);
        }
        throw new IncorrectInputInScriptException();
    }

    public Long askEmployeesCount() throws IncorrectInputInScriptException {
        String strEmployeesCount;
        Long employeesCount;
        try {
            Outputer.println("EnterEmployeesCount");
            Outputer.print(Main.PS2);
            strEmployeesCount = userScanner.nextLine().trim();
            Outputer.println(strEmployeesCount);
            employeesCount = Long.parseLong(strEmployeesCount);
            if (employeesCount < MIN_EMPLOYEESCOUNT) throw new NotInDeclaredLimitsException();
            return employeesCount;
        } catch (NoSuchElementException exception) {
            Outputer.printerror("EmployeesCountNotIdentifiedException");
        } catch (NotInDeclaredLimitsException exception) {
            Outputer.printerror("EmployeesCountMustBeMoreZero", String.valueOf(Organization.MAX_Y));
        } catch (NumberFormatException exception) {
            Outputer.printerror("EmployeesCountMustBeNumberException");
        } catch (NullPointerException | IllegalStateException exception) {
            Outputer.printerror("UnexpectedException");
            OutputerUI.error("UnexpectedException");
            System.exit(0);
        }
        throw new IncorrectInputInScriptException();
    }

    public OrganizationType askType() throws IncorrectInputInScriptException {
        String strType;
        OrganizationType type;
        try {
            Outputer.println("TypeList", OrganizationType.nameList());
            Outputer.println("EnterType");
            Outputer.print(Main.PS2);
            strType = userScanner.nextLine().trim();
            Outputer.println(strType);
            type = OrganizationType.valueOf(strType.toUpperCase());
            return type;
        } catch (NoSuchElementException exception) {
            Outputer.printerror("TypeNotIndentifiedException");
        } catch (IllegalArgumentException exception) {
            Outputer.printerror("NoSuchType");
        } catch (IllegalStateException exception) {
            Outputer.printerror("UnexpectedException");
            OutputerUI.error("UnexpectedException");
            System.exit(0);
        }
        throw new IncorrectInputInScriptException();
    }

    public String askStreet() throws IncorrectInputInScriptException {
        String streetName;
        try {
            Outputer.println("EnterStreet");
            Outputer.print(Main.PS2);
            streetName = userScanner.nextLine().trim();
            Outputer.println(streetName);
            if (streetName.equals("")) throw new MustBeNotEmptyException();
            return streetName;
        } catch (NoSuchElementException exception) {
            Outputer.printerror("StreetNotIdentifiedException");
        } catch (MustBeNotEmptyException exception) {
            Outputer.printerror("StreetEmptyExeption");
        } catch (IllegalStateException exception) {
            Outputer.printerror("UnexpectedException");
            OutputerUI.error("UnexpectedException");
            System.exit(0);
        }
        throw new IncorrectInputInScriptException();
    }

    public Address askAddress() throws IncorrectInputInScriptException {
        String street;
        Location town;
        street = askStreet();
        town = askLocation();
        return new Address(street, town);
    }

    public int askLocationX() throws IncorrectInputInScriptException {
        String strX;
        int x;
        try {
            Outputer.println("EnterX");
            Outputer.print(Main.PS2);
            strX = userScanner.nextLine().trim();
            Outputer.println(strX);
            x = Integer.parseInt(strX);
            return x;
        } catch (NoSuchElementException exception) {
            Outputer.printerror("XNotIdentifiedException");
        } catch (NumberFormatException exception) {
            Outputer.printerror("XMustBeNumberException");
        } catch (NullPointerException | IllegalStateException exception) {
            Outputer.printerror("UnexpectedException");
            OutputerUI.error("UnexpectedException");
            System.exit(0);
        }
        throw new IncorrectInputInScriptException();
    }

    public Float askLocationY() throws IncorrectInputInScriptException {
        String strY;
        Float y;
        try {
            Outputer.println("EnterY");
            Outputer.print(Main.PS2);
            strY = userScanner.nextLine().trim();
            Outputer.println(strY);
            y = Float.parseFloat(strY);
            return y;
        } catch (NoSuchElementException exception) {
            Outputer.printerror("YNotIdentifiedException");
        } catch (NumberFormatException exception) {
            Outputer.printerror("YMustBeNumberException");
        } catch (NullPointerException | IllegalStateException exception) {
            Outputer.printerror("UnexpectedException");
            OutputerUI.error("UnexpectedException");
            System.exit(0);
        }
        throw new IncorrectInputInScriptException();
    }


    public long askLocationZ() throws IncorrectInputInScriptException {
        String strZ;
        long z;
        try {
            Outputer.println("EnterZ");
            Outputer.print(Main.PS2);
            strZ = userScanner.nextLine().trim();
            Outputer.println(strZ);
            z = Long.parseLong(strZ);
            return z;
        } catch (NoSuchElementException exception) {
            Outputer.printerror("ZNotIdentifiedException");
        } catch (NumberFormatException exception) {
            Outputer.printerror("ZMustBeNumberException");
        } catch (NullPointerException | IllegalStateException exception) {
            Outputer.printerror("UnexpectedException");
            OutputerUI.error("UnexpectedException");
            System.exit(0);
        }
        throw new IncorrectInputInScriptException();
    }

    public Location askLocation() throws IncorrectInputInScriptException {
        int x;
        Float y; //Поле не может быть null
        long z;
        x = askLocationX();
        y = askLocationY();
        z = askLocationZ();
        return new Location(x, y, z);
    }

    /**
     * Asks a user a question.
     * @return Answer (true/false).
     * @param question A question.
     * @throws IncorrectInputInScriptException If script is running and something goes wrong.
     */
    public boolean askQuestion(String question) throws IncorrectInputInScriptException {
        String finalQuestion = question + " (+/-):";
        String answer;
        try {
            Outputer.println(finalQuestion);
            Outputer.print(Main.PS2);
            answer = userScanner.nextLine().trim();
            if (!answer.equals("+") && !answer.equals("-")) throw new NotInDeclaredLimitsException();
            return answer.equals("+");
        } catch (NoSuchElementException exception) {
            Outputer.printerror("AnswerNotIndentifiedException");
        } catch (NotInDeclaredLimitsException exception) {
            Outputer.printerror("AnswerLimitsException");
        } catch (IllegalStateException exception) {
            Outputer.printerror("UnexpectedException");
            OutputerUI.error("UnexpectedException");
            System.exit(0);
        }
        throw new IncorrectInputInScriptException();
    }

    @Override
    public String toString() {
        return "OrganizationAsker (helper class for queries to the user)";
    }
}