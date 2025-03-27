package ru.sayron.client.controllers;

import ru.sayron.client.controllers.tools.ObservableResourceFactory;
import ru.sayron.client.utility.OutputerUI;
import ru.sayron.common.data.*;
import ru.sayron.common.exceptions.MustBeNotEmptyException;
import ru.sayron.common.exceptions.NotInDeclaredLimitsException;
import ru.sayron.common.interaction.OrganizationRaw;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

/**
 * Sets the controller window.
 */
public class AskWindowController {
    @FXML
    private Label nameLabel;
    @FXML
    private Label coordinatesXLabel;
    @FXML
    private Label coordinatesYLabel;
    @FXML
    private Label annualTurnoverLabel;
    @FXML
    private Label fullNameLabel;
    @FXML
    private Label employeesCountLabel;
    @FXML
    private Label organizationTypeLabel;
    @FXML
    private Label addressStreetLabel;
    @FXML
    private Label addressTownLabel;
    @FXML
    private TextField nameField;
    @FXML
    private TextField coordinatesXField;
    @FXML
    private TextField coordinatesYField;
    @FXML
    private TextField annualTurnoverField;
    @FXML
    private TextField addressStreetField;
    @FXML
    private TextField addressTownField;
    @FXML
    private TextField fullNameField;
    @FXML
    private TextField employeesCountField;
    @FXML
    private TextField townXField;
    @FXML
    private TextField townYField;
    @FXML
    private TextField townZField;
    @FXML
    private ComboBox<OrganizationType> organizationTypeBox;
    @FXML
    private Button enterButton;

    private Stage askStage;
    private OrganizationRaw resultOrganization;
    private ObservableResourceFactory resourceFactory;

    /**
     * Initialize ask window.
     */
    public void initialize() {
        organizationTypeBox.setItems(FXCollections.observableArrayList(OrganizationType.values()));
    }

    /**
     * Enter button on action.
     */
    @FXML
    private void enterButtonOnAction() {
        try {
            resultOrganization = new OrganizationRaw(
                    convertName(),
                    new Coordinates(
                            (long) convertCoordinatesxX(),
                            convertCoordinatesxY()
                    ),
                    convertAnnualTurnover(),
                    convertFullName(),
                    convertEmployeesCount(),
                    organizationTypeBox.getValue(),
                    new Address(
                            convertAddressStreet(),
                            new Location(
                                    convertTownX(),
                                    convertTownY(),
                                    convertTownZ()
                            )
                    )
            );
            askStage.close();
        } catch (IllegalArgumentException exception) { /* ? */ }
    }

    /**
     * Binds interface language.
     */
    private void bindGuiLanguage() {
        nameLabel.textProperty().bind(resourceFactory.getStringBinding("NameColumn"));
        coordinatesXLabel.textProperty().bind(resourceFactory.getStringBinding("CoordinatesXColumn"));
        coordinatesYLabel.textProperty().bind(resourceFactory.getStringBinding("CoordinatesYColumn"));
        annualTurnoverLabel.textProperty().bind(resourceFactory.getStringBinding("AnnualTurnoverColumn"));
        fullNameLabel.textProperty().bind(resourceFactory.getStringBinding("FullNameColumn"));
        employeesCountLabel.textProperty().bind(resourceFactory.getStringBinding("EmployeesCountColumn"));
        organizationTypeLabel.textProperty().bind(resourceFactory.getStringBinding("OrganizationTypeColumn"));
        addressTownLabel.textProperty().bind(resourceFactory.getStringBinding("AddressTownColumn"));
        addressStreetLabel.textProperty().bind(resourceFactory.getStringBinding("AddressStreetColumn"));

        enterButton.textProperty().bind(resourceFactory.getStringBinding("EnterButton"));
    }

    /**
     * Convert name.
     *
     * @return Name.
     */
    private String convertName() throws IllegalArgumentException {
        String name;
        try {
            name = nameField.getText();
            if (name.equals("")) throw new MustBeNotEmptyException();
        } catch (MustBeNotEmptyException exception) {
            OutputerUI.error("NameEmptyException");
            throw new IllegalArgumentException();
        }
        return name;
    }

    private String convertAddressTown() throws IllegalArgumentException {
        String name;
        try {
            name = nameField.getText();
            if (name.equals("")) throw new MustBeNotEmptyException();
        } catch (MustBeNotEmptyException exception) {
            OutputerUI.error("NameEmptyException");
            throw new IllegalArgumentException();
        }
        return name;
    }

    /**
     * Convert Coordinates X.
     *
     * @return X.
     */
    private double convertCoordinatesxX() throws IllegalArgumentException {
        String strX;
        long x;
        try {
            strX = coordinatesXField.getText();
            x = Long.parseLong(strX);
        } catch (NumberFormatException exception) {
            OutputerUI.error("CoordinatesXFormatException");
            throw new IllegalArgumentException();
        }
        return x;
    }

    /**
     * Convert Coordinates Y.
     *
     * @return Y.
     */
    private Integer convertCoordinatesxY() throws IllegalArgumentException {
        String strY;
        Integer y;
        try {
            strY = coordinatesYField.getText();
            y = Integer.parseInt(strY);
            if (y > Organization.MAX_Y) throw new NotInDeclaredLimitsException();
        } catch (NumberFormatException exception) {
            OutputerUI.error("CoordinatesYFormatException");
            throw new IllegalArgumentException();
        } catch (NotInDeclaredLimitsException exception) {
            OutputerUI.error("CoordinatesYLimitsException", new String[]{String.valueOf(Organization.MAX_Y)});
            throw new IllegalArgumentException();
        }
        return y;
    }

    /**
     * Convert annualTurnover.
     *
     * @return AnnualTurnover.
     */
    private int convertAnnualTurnover() throws IllegalArgumentException {
        String strAnnualTurnover;
        int annualTurnover;
        try {
            strAnnualTurnover = annualTurnoverField.getText();
            annualTurnover = Integer.parseInt(strAnnualTurnover);
            if (annualTurnover <= Organization.MIN_ANNUALTURNOVER) throw new NotInDeclaredLimitsException();
        } catch (NumberFormatException exception) {
            OutputerUI.error("AnnualTurnoverFormatException");
            throw new IllegalArgumentException();
        } catch (NotInDeclaredLimitsException exception) {
            OutputerUI.error("AnnualTurnoverException");
            throw new IllegalArgumentException();
        }
        return annualTurnover;
    }


    /**
     * Convert fullName.
     *
     * @return FullName.
     */
    private String convertFullName() throws IllegalArgumentException {
        String fullName;
        try {
            fullName = fullNameField.getText();
            if (fullName.equals("")) throw new MustBeNotEmptyException();
        } catch (MustBeNotEmptyException exception) {
            OutputerUI.error("NameEmptyException");
            throw new IllegalArgumentException();
        }
        return fullName;
    }

    /**
     * Convert address street.
     *
     * @return Address street.
     */
    private String convertAddressStreet() throws IllegalArgumentException {
        String addressStreet;
        try {
            addressStreet = addressStreetField.getText();
            if (addressStreet.equals("")) throw new MustBeNotEmptyException();
        } catch (MustBeNotEmptyException exception) {
            OutputerUI.error("AddressStreetEmptyException");
            throw new IllegalArgumentException();
        }
        return addressStreet;
    }

    /**
     * Convert town X.
     *
     * @return X.
     */
    private int convertTownX() throws IllegalArgumentException {
        String strX;
        int x;
        try {
            strX = townXField.getText();
            x = Integer.parseInt(strX);
        } catch (NumberFormatException exception) {
            OutputerUI.error("CoordinatesXFormatException");
            throw new IllegalArgumentException();
        }
        return (int) x;
    }


    /**
     * Convert town Y.
     *
     * @return Y.
     */
    private Float convertTownY() throws IllegalArgumentException {
        String strY;
        Float y;
        try {
            strY = townYField.getText();
            y = Float.parseFloat(strY);
        } catch (NumberFormatException exception) {
            OutputerUI.error("CoordinatesYFormatException");
            throw new IllegalArgumentException();
        }
        return y;
    }


    /**
     * Convert town Z.
     *
     * @return Z.
     */
    private long convertTownZ() throws IllegalArgumentException {
        String strZ;
        long z;
        try {
            strZ = townZField.getText();
            z = Long.parseLong(strZ);
        } catch (NumberFormatException exception) {
            OutputerUI.error("CoordinatesZFormatException");
            throw new IllegalArgumentException();
        }
        return z;
    }


    /**
     * Convert employeesCount.
     *
     * @return EmployeesCount.
     */
    private Long convertEmployeesCount() throws IllegalArgumentException {
        String strEmployeesCount;
        Long emloyeesCount;
        try {
            strEmployeesCount = employeesCountField.getText();
            emloyeesCount = Long.parseLong(strEmployeesCount);
            if (emloyeesCount < Organization.MIN_EMPLOYEESCOUNT) throw new NotInDeclaredLimitsException();
        } catch (NumberFormatException exception) {
            OutputerUI.error("EmployeesCountFormatException");
            throw new IllegalArgumentException();
        } catch (NotInDeclaredLimitsException exception) {
            OutputerUI.error("EmployeesCountLimitsException", new String[]{String.valueOf(Organization.MIN_EMPLOYEESCOUNT)});
            throw new IllegalArgumentException();
        }
        return emloyeesCount;
    }

    /**
     * Set Organization.
     *
     * @param organization Organization to set.
     */
    public void setOrganization(Organization organization) {
        nameField.setText(organization.getName());
        coordinatesXField.setText(organization.getCoordinates().getX() + "");
        coordinatesYField.setText(organization.getCoordinates().getY() + "");
        annualTurnoverField.setText(organization.getAnnualTurnover() + "");
        addressStreetField.setText(organization.getOfficialAddress().getStreet());
        townXField.setText(organization.getOfficialAddress().getTown().getX() + "");
        townYField.setText(organization.getOfficialAddress().getTown().getY() + "");
        townZField.setText(organization.getOfficialAddress().getTown().getZ() + "");
        fullNameField.setText(organization.getFullName() + "");
        employeesCountField.setText(organization.getEmployeesCount() + "");
        organizationTypeBox.setValue(organization.getType());
    }

    /**
     * Clear Marine.
     */
    public void clearOrganization() {
        nameField.clear();
        coordinatesXField.clear();
        coordinatesYField.clear();
        annualTurnoverField.clear();
        fullNameField.clear();
        employeesCountField.clear();
        addressStreetField.clear();
        //addressTownField.clear();
        townXField.clear();
        townYField.clear();
        townZField.clear();
        organizationTypeBox.setValue(OrganizationType.PUBLIC);
    }

    /**
     * Get and clear Marine.
     *
     * @return Marine to return.
     */
    public OrganizationRaw getAndClear() {
        OrganizationRaw organizationToReturn = resultOrganization;
        resultOrganization = null;
        return organizationToReturn;
    }

    public void setAskStage(Stage askStage) {
        this.askStage = askStage;
    }

    /**
     * Init langs.
     *
     * @param resourceFactory Resource factory to set.
     */
    public void initLangs(ObservableResourceFactory resourceFactory) {
        this.resourceFactory = resourceFactory;
        bindGuiLanguage();
    }
}

