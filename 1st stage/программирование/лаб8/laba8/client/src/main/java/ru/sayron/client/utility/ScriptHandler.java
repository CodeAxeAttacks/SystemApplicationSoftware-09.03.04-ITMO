package ru.sayron.client.utility;

import ru.sayron.client.Main;
import ru.sayron.common.data.*;
import ru.sayron.common.exceptions.CommandUsageException;
import ru.sayron.common.exceptions.IncorrectInputInScriptException;
import ru.sayron.common.exceptions.ScriptRecursionException;
import ru.sayron.common.interaction.OrganizationRaw;
import ru.sayron.common.interaction.Request;
import ru.sayron.common.interaction.ResponseCode;
import ru.sayron.common.interaction.User;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.NoSuchElementException;
import java.util.Scanner;
import java.util.Stack;

/*
 * Script handler.
 */
public class ScriptHandler {
    private final int maxRewriteAttempts = 1;

    private Scanner userScanner;
    private Stack<File> scriptStack = new Stack<>();
    private Stack<Scanner> scannerStack = new Stack<>();

    public ScriptHandler(File scriptFile) {
        try {
            userScanner = new Scanner(scriptFile);
            scannerStack.add(userScanner);
            scriptStack.add(scriptFile);
        } catch (Exception exception) { /* ? */ }
    }

    /**
     * Receives user input.
     *
     * @param serverResponseCode Previous response code.
     * @param user               User object.
     * @return New request to server.
     */
    public Request handle(ResponseCode serverResponseCode, User user) {
        String userInput;
        String[] userCommand;
        ProcessingCode processingCode;
        int rewriteAttempts = 0;
        try {
            do {
                try {
                    if (serverResponseCode == ResponseCode.ERROR || serverResponseCode == ResponseCode.SERVER_EXIT)
                        throw new IncorrectInputInScriptException();
                    while (!scannerStack.isEmpty() && !userScanner.hasNextLine()) {
                        userScanner.close();
                        userScanner = scannerStack.pop();
                        if (!scannerStack.isEmpty()) scriptStack.pop();
                        else return null;
                    }
                    userInput = userScanner.nextLine();
                    if (!userInput.isEmpty()) {
                        Outputer.print(Main.PS1);
                        Outputer.println(userInput);
                    }
                    userCommand = (userInput.trim() + " ").split(" ", 2);
                    userCommand[1] = userCommand[1].trim();
                } catch (NoSuchElementException | IllegalStateException exception) {
                    Outputer.println();
                    Outputer.printerror("CommandErrorException");
                    userCommand = new String[]{"", ""};
                    rewriteAttempts++;
                    if (rewriteAttempts >= maxRewriteAttempts) {
                        Outputer.printerror("RewriteAttemptsException");
                        System.exit(0);
                    }
                }
                processingCode = processCommand(userCommand[0], userCommand[1]);
            } while (userCommand[0].isEmpty());
            try {
                if (serverResponseCode == ResponseCode.ERROR || processingCode == ProcessingCode.ERROR)
                    throw new IncorrectInputInScriptException();
                switch (processingCode) {
                    case OBJECT:
                        OrganizationRaw organizationAddRaw = generateOrganizationAdd();
                        return new Request(userCommand[0], userCommand[1], organizationAddRaw, user);
                    case UPDATE_OBJECT:
                        OrganizationRaw organizationUpdateRaw = generateOrganizationUpdate();
                        return new Request(userCommand[0], userCommand[1], organizationUpdateRaw, user);
                    case SCRIPT:
                        File scriptFile = new File(userCommand[1]);
                        if (!scriptFile.exists()) throw new FileNotFoundException();
                        if (!scriptStack.isEmpty() && scriptStack.search(scriptFile) != -1)
                            throw new ScriptRecursionException();
                        scannerStack.push(userScanner);
                        scriptStack.push(scriptFile);
                        userScanner = new Scanner(scriptFile);
                        Outputer.println("ScriptRunning", scriptFile.getName());
                        break;
                }
            } catch (FileNotFoundException exception) {
                Outputer.printerror("ScriptFileNotFoundException");
                throw new IncorrectInputInScriptException();
            } catch (ScriptRecursionException exception) {
                Outputer.printerror("ScriptRecursionException");
                throw new IncorrectInputInScriptException();
            }
        } catch (IncorrectInputInScriptException exception) {
            OutputerUI.error("IncorrectInputInScriptException");
            while (!scannerStack.isEmpty()) {
                userScanner.close();
                userScanner = scannerStack.pop();
            }
            scriptStack.clear();
            return null;
        }
        return new Request(userCommand[0], userCommand[1], null, user);
    }

    /**
     * Processes the entered command.
     *
     * @return Status of code.
     */
    private ProcessingCode processCommand(String command, String commandArgument) {
        try {
            switch (command) {
                case "":
                    return ProcessingCode.ERROR;
                case "info":
                    if (!commandArgument.isEmpty()) throw new CommandUsageException();
                    break;
                case "add":
                    if (!commandArgument.isEmpty()) throw new CommandUsageException("{element}");
                    return ProcessingCode.OBJECT;
                case "update":
                    if (commandArgument.isEmpty()) throw new CommandUsageException("<ID> {element}");
                    return ProcessingCode.UPDATE_OBJECT;
                case "remove_by_id":
                    if (commandArgument.isEmpty()) throw new CommandUsageException("<ID>");
                    break;
                case "clear":
                    if (!commandArgument.isEmpty()) throw new CommandUsageException();
                    break;
                case "execute_script":
                    if (commandArgument.isEmpty()) throw new CommandUsageException("<file_name>");
                    return ProcessingCode.SCRIPT;
                case "exit":
                    if (!commandArgument.isEmpty()) throw new CommandUsageException();
                    break;
                case "add_if_min":
                    if (!commandArgument.isEmpty()) throw new CommandUsageException("{element}");
                    return ProcessingCode.OBJECT;
                case "remove_greater":
                    if (!commandArgument.isEmpty()) throw new CommandUsageException("{element}");
                    return ProcessingCode.OBJECT;
                case "history":
                    if (!commandArgument.isEmpty()) throw new CommandUsageException();
                    break;
                case "sum_of_health":
                    if (!commandArgument.isEmpty()) throw new CommandUsageException();
                    break;
                default:
                    Outputer.println("CommandNotFoundException", command);
                    return ProcessingCode.ERROR;
            }
        } catch (CommandUsageException exception) {
            if (exception.getMessage() != null) command += " " + exception.getMessage();
            Outputer.println("Using", command);
            return ProcessingCode.ERROR;
        }
        return ProcessingCode.OK;
    }

    /**
     * Generates marine to add.
     *
     * @return Marine to add.
     * @throws IncorrectInputInScriptException When something went wrong in script.
     */
    private OrganizationRaw generateOrganizationAdd() throws IncorrectInputInScriptException {
        OrganizationAsker organizationAsker = new OrganizationAsker(userScanner);
        return new OrganizationRaw(
                organizationAsker.askName(),
                organizationAsker.askCoordinates(),
                organizationAsker.askTurnover(),
                organizationAsker.askFullName(),
                organizationAsker.askEmployeesCount(),
                organizationAsker.askType(),
                organizationAsker.askAddress()
        );
    }

    /**
     * Generates marine to update.
     *
     * @return Marine to update.
     * @throws IncorrectInputInScriptException When something went wrong in script.
     */
    private OrganizationRaw generateOrganizationUpdate() throws IncorrectInputInScriptException {
        OrganizationAsker organizationAsker = new OrganizationAsker(userScanner);
        String name = organizationAsker.askQuestion("ChangeNameQuestion") ?
                organizationAsker.askName() : null;
        Coordinates coordinates = organizationAsker.askQuestion("ChangeCoordinatesQuestion") ?
                organizationAsker.askCoordinates() : null;
        int annualTurnover = organizationAsker.askQuestion("ChangeAnnualTurnoverQuestion") ?
                organizationAsker.askTurnover() : -1;
        String fullName = organizationAsker.askQuestion("ChangeFullNameQuestion") ?
                organizationAsker.askFullName() : null;
        long employeesCount = organizationAsker.askQuestion("ChangeEmployeesCountQuestion") ?
                organizationAsker.askEmployeesCount() : null;
        OrganizationType organizationType = organizationAsker.askQuestion("ChangeOrganizationTypeQuestion") ?
                organizationAsker.askType() : null;
        Address address = organizationAsker.askQuestion("ChangeAddressQuestion") ?
                organizationAsker.askAddress() : null;
        return new OrganizationRaw(
                name,
                coordinates,
                annualTurnover,
                fullName,
                employeesCount,
                organizationType,
                address
        );
    }
}

