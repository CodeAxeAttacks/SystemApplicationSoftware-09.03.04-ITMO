package ru.sayron.client.controllers;

import ru.sayron.client.Main;
import ru.sayron.client.Client;
import ru.sayron.client.controllers.tools.ObservableResourceFactory;
import ru.sayron.client.utility.OutputerUI;
import ru.sayron.common.data.*;
import ru.sayron.common.interaction.OrganizationRaw;
import javafx.animation.ScaleTransition;
import javafx.application.Platform;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Shape;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.controlsfx.control.table.TableFilter;

import java.io.File;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.*;

/**
 * Main window controller.
 */
public class MainWindowController {
    public static final String LOGIN_COMMAND_NAME = "login";
    public static final String REGISTER_COMMAND_NAME = "register";
    public static final String REFRESH_COMMAND_NAME = "refresh";
    public static final String INFO_COMMAND_NAME = "info";
    public static final String ADD_COMMAND_NAME = "add";
    public static final String UPDATE_COMMAND_NAME = "update";
    public static final String REMOVE_COMMAND_NAME = "remove_by_id";
    public static final String CLEAR_COMMAND_NAME = "clear";
    public static final String EXIT_COMMAND_NAME = "exit";
    public static final String ADD_IF_MIN_COMMAND_NAME = "add_if_min";
    public static final String REMOVE_GREATER_COMMAND_NAME = "remove_greater";
    public static final String HISTORY_COMMAND_NAME = "history";
    public static final String SUM_OF_HEALTH_COMMAND_NAME = "sum_of_health";

    private final long RANDOM_SEED = 1821L;
    private final Duration ANIMATION_DURATION = Duration.millis(800);
    private final double MAX_SIZE = 250;

    @FXML
    private TableView<Organization> organizationTable;
    @FXML
    private TableColumn<Organization, Long> idColumn;
    @FXML
    private TableColumn<Organization, String> ownerColumn;
    @FXML
    private TableColumn<Organization, LocalDateTime> creationDateColumn;
    @FXML
    private TableColumn<Organization, String> nameColumn;
    @FXML
    private TableColumn<Organization, Integer> annualTurnoverColumn;
    @FXML
    private TableColumn<Organization, Long> coordinatesXColumn;
    @FXML
    private TableColumn<Organization, Integer> coordinatesYColumn;
    @FXML
    private TableColumn<Organization, String> fullNameColumn;
    @FXML
    private TableColumn<Organization, Long> employeesCountColumn;
    @FXML
    private TableColumn<Organization, OrganizationType> organizationTypeColumn;
    @FXML
    private TableColumn<Organization, String> addressStreetColumn;
    @FXML
    private TableColumn<Organization, Location> addressTownColumn;
    @FXML
    private AnchorPane canvasPane;
    @FXML
    private Tab tableTab;
    @FXML
    private Tab canvasTab;
    @FXML
    private Button infoButton;
    @FXML
    private Button addButton;
    @FXML
    private Button updateButton;
    @FXML
    private Button removeButton;
    @FXML
    private Button clearButton;
    @FXML
    private Button executeScriptButton;
    @FXML
    private Button addIfMinButton;
    @FXML
    private Button removeGreaterButton;
    @FXML
    private Button historyButton;
    @FXML
    private Button sumOfHealthButton;
    @FXML
    private Button refreshButton;
    @FXML
    private Tooltip infoButtonTooltip;
    @FXML
    private Tooltip addButtonTooltip;
    @FXML
    private Tooltip updateButtonTooltip;
    @FXML
    private Tooltip removeButtonTooltip;
    @FXML
    private Tooltip clearButtonTooltip;
    @FXML
    private Tooltip executeScriptButtonTooltip;
    @FXML
    private Tooltip addIfMinButtonTooltip;
    @FXML
    private Tooltip removeGreaterButtonTooltip;
    @FXML
    private Tooltip historyButtonTooltip;
    @FXML
    private Tooltip sumOfHealthButtonTooltip;
    @FXML
    private Tooltip refreshButtonTooltip;
    @FXML
    private ComboBox<String> languageComboBox;
    @FXML
    private Label usernameLabel;

    private Client client;
    private Stage askStage;
    private Stage primaryStage;
    private FileChooser fileChooser;
    private AskWindowController askWindowController;
    private Map<String, Color> userColorMap;
    private Map<Shape, Long> shapeMap;
    private Map<Long, Text> textMap;
    private Shape prevClicked;
    private Color prevColor;
    private Random randomGenerator;
    private ObservableResourceFactory resourceFactory;
    private Map<String, Locale> localeMap;

    /**
     * Initialize main window.
     */
    public void initialize() {
        initializeTable();
        fileChooser = new FileChooser();
        fileChooser.setInitialDirectory(new File("."));
        userColorMap = new HashMap<>();
        shapeMap = new HashMap<>();
        textMap = new HashMap<>();
        randomGenerator = new Random(RANDOM_SEED);
        localeMap = new HashMap<>();
        localeMap.put("Македонски", new Locale("mk", "MK"));
        localeMap.put("Русский", new Locale("ru", "RU"));
        localeMap.put("Polski", new Locale("pl", "PL"));
        localeMap.put("Español", new Locale("es", "PR"));
        languageComboBox.setItems(FXCollections.observableArrayList(localeMap.keySet()));
    }

    /**
     * Initialize table.
     */
    private void initializeTable() {
        idColumn.setCellValueFactory(cellData ->
                new ReadOnlyObjectWrapper<>(cellData.getValue().getId()));
        ownerColumn.setCellValueFactory(cellData ->
                new ReadOnlyObjectWrapper<>(cellData.getValue().getOwner().getUsername()));
        creationDateColumn.setCellValueFactory(cellData ->
                new ReadOnlyObjectWrapper<>(cellData.getValue().getCreationDate()));
        nameColumn.setCellValueFactory(cellData ->
                new ReadOnlyObjectWrapper<>(cellData.getValue().getName()));
        annualTurnoverColumn.setCellValueFactory(cellData ->
                new ReadOnlyObjectWrapper<>(cellData.getValue().getAnnualTurnover()));
        coordinatesXColumn.setCellValueFactory(cellData ->
                new ReadOnlyObjectWrapper<>(cellData.getValue().getCoordinates().getX()));
        coordinatesYColumn.setCellValueFactory(cellData ->
                new ReadOnlyObjectWrapper<>(cellData.getValue().getCoordinates().getY()));
        fullNameColumn.setCellValueFactory(cellData ->
                new ReadOnlyObjectWrapper<>(cellData.getValue().getFullName()));
        employeesCountColumn.setCellValueFactory(cellData ->
                new ReadOnlyObjectWrapper<>(cellData.getValue().getEmployeesCount()));
        organizationTypeColumn.setCellValueFactory(cellData ->
                new ReadOnlyObjectWrapper<>(cellData.getValue().getType()));
        addressStreetColumn.setCellValueFactory(cellData ->
                new ReadOnlyObjectWrapper<>(cellData.getValue().getOfficialAddress().getStreet()));
        addressTownColumn.setCellValueFactory(cellData ->
                new ReadOnlyObjectWrapper<>(cellData.getValue().getOfficialAddress().getTown()));
    }

    /**
     * Bind gui language.
     */
    private void bindGuiLanguage() {
        resourceFactory.setResources(ResourceBundle.getBundle
                (Main.BUNDLE, localeMap.get(languageComboBox.getSelectionModel().getSelectedItem())));

        idColumn.textProperty().bind(resourceFactory.getStringBinding("IdColumn"));
        ownerColumn.textProperty().bind(resourceFactory.getStringBinding("OwnerColumn"));
        creationDateColumn.textProperty().bind(resourceFactory.getStringBinding("CreationDateColumn"));
        nameColumn.textProperty().bind(resourceFactory.getStringBinding("NameColumn"));
        coordinatesXColumn.textProperty().bind(resourceFactory.getStringBinding("CoordinatesXColumn"));
        coordinatesYColumn.textProperty().bind(resourceFactory.getStringBinding("CoordinatesYColumn"));
        annualTurnoverColumn.textProperty().bind(resourceFactory.getStringBinding("AnnualTurnoverColumn"));
        fullNameColumn.textProperty().bind(resourceFactory.getStringBinding("FullNameColumn"));
        employeesCountColumn.textProperty().bind(resourceFactory.getStringBinding("EmployeesCountColumn"));
        organizationTypeColumn.textProperty().bind(resourceFactory.getStringBinding("OrganizationTypeColumn"));
        addressStreetColumn.textProperty().bind(resourceFactory.getStringBinding("AddressStreetColumn"));
        addressTownColumn.textProperty().bind(resourceFactory.getStringBinding("AddressTownColumn"));

        tableTab.textProperty().bind(resourceFactory.getStringBinding("TableTab"));
        canvasTab.textProperty().bind(resourceFactory.getStringBinding("CanvasTab"));

        infoButton.textProperty().bind(resourceFactory.getStringBinding("InfoButton"));
        addButton.textProperty().bind(resourceFactory.getStringBinding("AddButton"));
        updateButton.textProperty().bind(resourceFactory.getStringBinding("UpdateButton"));
        removeButton.textProperty().bind(resourceFactory.getStringBinding("RemoveButton"));
        clearButton.textProperty().bind(resourceFactory.getStringBinding("ClearButton"));
        executeScriptButton.textProperty().bind(resourceFactory.getStringBinding("ExecuteScriptButton"));
        addIfMinButton.textProperty().bind(resourceFactory.getStringBinding("AddIfMinButton"));
        removeGreaterButton.textProperty().bind(resourceFactory.getStringBinding("RemoveGreaterButton"));
        historyButton.textProperty().bind(resourceFactory.getStringBinding("HistoryButton"));
        sumOfHealthButton.textProperty().bind(resourceFactory.getStringBinding("SumOfHealthButton"));
        refreshButton.textProperty().bind(resourceFactory.getStringBinding("RefreshButton"));

        infoButtonTooltip.textProperty().bind(resourceFactory.getStringBinding("InfoButtonTooltip"));
        addButtonTooltip.textProperty().bind(resourceFactory.getStringBinding("AddButtonTooltip"));
        updateButtonTooltip.textProperty().bind(resourceFactory.getStringBinding("UpdateButtonTooltip"));
        removeButtonTooltip.textProperty().bind(resourceFactory.getStringBinding("RemoveButtonTooltip"));
        clearButtonTooltip.textProperty().bind(resourceFactory.getStringBinding("ClearButtonTooltip"));
        executeScriptButtonTooltip.textProperty().bind(resourceFactory.getStringBinding("ExecuteScriptButtonTooltip"));
        addIfMinButtonTooltip.textProperty().bind(resourceFactory.getStringBinding("AddIfMinButtonTooltip"));
        removeGreaterButtonTooltip.textProperty().bind(resourceFactory.getStringBinding("RemoveGreaterButtonTooltip"));
        historyButtonTooltip.textProperty().bind(resourceFactory.getStringBinding("HistoryButtonTooltip"));
        sumOfHealthButtonTooltip.textProperty().bind(resourceFactory.getStringBinding("SumOfHealthButtonTooltip"));
        refreshButtonTooltip.textProperty().bind(resourceFactory.getStringBinding("RefreshButtonTooltip"));
    }

    /**
     * Refresh button on action.
     */
    @FXML
    public void refreshButtonOnAction() {
        requestAction(REFRESH_COMMAND_NAME);
    }

    /**
     * Info button on action.
     */
    @FXML
    private void infoButtonOnAction() {
        requestAction(INFO_COMMAND_NAME);
    }

    /**
     * Add button on action.
     */
    @FXML
    private void addButtonOnAction() {
        askWindowController.clearOrganization();
        askStage.showAndWait();
        OrganizationRaw organizationRaw = askWindowController.getAndClear();
        if (organizationRaw != null) requestAction(ADD_COMMAND_NAME, "", organizationRaw);
    }

    /**
     * Update button on action.
     */
    @FXML
    private void updateButtonOnAction() {
        if (!organizationTable.getSelectionModel().isEmpty()) {
            long id = organizationTable.getSelectionModel().getSelectedItem().getId();
            askWindowController.setOrganization(organizationTable.getSelectionModel().getSelectedItem());
            askStage.showAndWait();
            OrganizationRaw organizationRaw = askWindowController.getAndClear();
            if (organizationRaw != null) requestAction(UPDATE_COMMAND_NAME, id + "", organizationRaw);
        } else OutputerUI.error("UpdateButtonSelectionException");

    }

    /**
     * Remove button on action.
     */
    @FXML
    private void removeButtonOnAction() {
        if (!organizationTable.getSelectionModel().isEmpty())
            requestAction(REMOVE_COMMAND_NAME,
                    organizationTable.getSelectionModel().getSelectedItem().getId().toString(), null);
        else OutputerUI.error("RemoveButtonSelectionException");
    }

    /**
     * Clear button on action.
     */
    @FXML
    private void clearButtonOnAction() {
        requestAction(CLEAR_COMMAND_NAME);
    }

    /**
     * Execute script button on action.
     */
    @FXML
    private void executeScriptButtonOnAction() {
        File selectedFile = fileChooser.showOpenDialog(primaryStage);
        if (selectedFile == null) return;
        if (client.processScriptToServer(selectedFile)) Platform.exit();
        else refreshButtonOnAction();
    }

    /**
     * Add if min button on action.
     */
    @FXML
    private void addIfMinButtonOnAction() {
        askWindowController.clearOrganization();
        askStage.showAndWait();
        OrganizationRaw organizationRaw = askWindowController.getAndClear();
        if (organizationRaw != null) requestAction(ADD_IF_MIN_COMMAND_NAME, "", organizationRaw);
    }

    /**
     * Remove greater button on action.
     */
    @FXML
    private void removeGreaterButtonOnAction() {
        if (!organizationTable.getSelectionModel().isEmpty()) {
            Organization organizationFromTable = organizationTable.getSelectionModel().getSelectedItem();
            OrganizationRaw organizationRaw = new OrganizationRaw(
                    organizationFromTable.getName(),
                    organizationFromTable.getCoordinates(),
                    organizationFromTable.getAnnualTurnover(),
                    organizationFromTable.getFullName(),
                    organizationFromTable.getEmployeesCount(),
                    organizationFromTable.getType(),
                    organizationFromTable.getOfficialAddress()
            );
            requestAction(REMOVE_GREATER_COMMAND_NAME, "", organizationRaw);
        } else OutputerUI.error("RemoveGreaterButtonSelectionException");
    }

    /**
     * History button on action.
     */
    @FXML
    private void historyButtonOnAction() {
        requestAction(HISTORY_COMMAND_NAME);
    }

    /**
     * Sum of health button on action.
     */
    @FXML
    private void sumOfHealthButtonOnAction() {
        requestAction(SUM_OF_HEALTH_COMMAND_NAME);
    }

    /**
     * Request action.
     */
    private void requestAction(String commandName, String commandStringArgument, Serializable commandObjectArgument) {
        NavigableSet<Organization> responsedMarines = client.processRequestToServer(commandName, commandStringArgument,
                commandObjectArgument);
        if (responsedMarines != null) {
            ObservableList<Organization> marinesList = FXCollections.observableArrayList(responsedMarines);
            organizationTable.setItems(marinesList);
            TableFilter.forTableView(organizationTable).apply();
            organizationTable.getSelectionModel().clearSelection();
            refreshCanvas();
        }
    }

    /**
     * Binds request action.
     */
    private void requestAction(String commandName) {
        requestAction(commandName, "", null);
    }

    /**
     * Refreshes canvas.
     */
    private void refreshCanvas() {
        shapeMap.keySet().forEach(s -> canvasPane.getChildren().remove(s));
        shapeMap.clear();
        textMap.values().forEach(s -> canvasPane.getChildren().remove(s));
        textMap.clear();
        for (Organization organization : organizationTable.getItems()) {
            if (!userColorMap.containsKey(organization.getOwner().getUsername()))
                userColorMap.put(organization.getOwner().getUsername(),
                        Color.color(randomGenerator.nextDouble(), randomGenerator.nextDouble(), randomGenerator.nextDouble()));

            double size = Math.min(organization.getAnnualTurnover(), MAX_SIZE);

            Shape circleObject = new Circle(size, userColorMap.get(organization.getOwner().getUsername()));
            circleObject.setOnMouseClicked(this::shapeOnMouseClicked);
            circleObject.translateXProperty().bind(canvasPane.widthProperty().divide(2).add(organization.getCoordinates().getX()));
            circleObject.translateYProperty().bind(canvasPane.heightProperty().divide(2).subtract(organization.getCoordinates().getY()));

            Text textObject = new Text(organization.getId().toString());
            textObject.setOnMouseClicked(circleObject::fireEvent);
            textObject.setFont(Font.font(size / 3));
            textObject.setFill(userColorMap.get(organization.getOwner().getUsername()).darker());
            textObject.translateXProperty().bind(circleObject.translateXProperty().subtract(textObject.getLayoutBounds().getWidth() / 2));
            textObject.translateYProperty().bind(circleObject.translateYProperty().add(textObject.getLayoutBounds().getHeight() / 4));

            canvasPane.getChildren().add(circleObject);
            canvasPane.getChildren().add(textObject);
            shapeMap.put(circleObject, organization.getId());
            textMap.put(organization.getId(), textObject);

            ScaleTransition circleAnimation = new ScaleTransition(ANIMATION_DURATION, circleObject);
            ScaleTransition textAnimation = new ScaleTransition(ANIMATION_DURATION, textObject);
            circleAnimation.setFromX(0);
            circleAnimation.setToX(1);
            circleAnimation.setFromY(0);
            circleAnimation.setToY(1);
            textAnimation.setFromX(0);
            textAnimation.setToX(1);
            textAnimation.setFromY(0);
            textAnimation.setToY(1);
            circleAnimation.play();
            textAnimation.play();
        }
    }

    /**
     * Shape on mouse clicked.
     */
    private void shapeOnMouseClicked(MouseEvent event) {
        Shape shape = (Shape) event.getSource();
        long id = shapeMap.get(shape);
        for (Organization organization : organizationTable.getItems()) {
            if (organization.getId() == id) {
                organizationTable.getSelectionModel().select(organization);
                break;
            }
        }
        if (prevClicked != null) {
            prevClicked.setFill(prevColor);
        }
        prevClicked = shape;
        prevColor = (Color) shape.getFill();
        shape.setFill(prevColor.brighter());
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public void setUsername(String username) {
        usernameLabel.setText(username);
    }

    public void setAskStage(Stage askStage) {
        this.askStage = askStage;
    }

    public void setPrimaryStage(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

    public void setAskWindowController(AskWindowController askWindowController) {
        this.askWindowController = askWindowController;
    }

    public void initLangs(ObservableResourceFactory resourceFactory) {
        this.resourceFactory = resourceFactory;
        for (String localeName : localeMap.keySet()) {
            if (localeMap.get(localeName).equals(resourceFactory.getResources().getLocale()))
                languageComboBox.getSelectionModel().select(localeName);
        }
        if (languageComboBox.getSelectionModel().getSelectedItem().isEmpty())
            languageComboBox.getSelectionModel().selectFirst();
        languageComboBox.setOnAction((event) ->
                resourceFactory.setResources(ResourceBundle.getBundle
                        (Main.BUNDLE, localeMap.get(languageComboBox.getValue()))));
        bindGuiLanguage();
    }
}

