package ru.sayron.client.utility;

import ru.sayron.client.Main;
import ru.sayron.common.data.*;
import ru.sayron.common.exceptions.CommandUsageException;
import ru.sayron.common.exceptions.IncorrectInputInScriptException;
import ru.sayron.common.exceptions.ScriptRecursionException;
import ru.sayron.common.interaction.OrganizationRaw;
import ru.sayron.common.interaction.Request;
import ru.sayron.common.interaction.ResponseCode;
import ru.sayron.common.utility.Outputer;
import ru.sayron.common.interaction.User;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.NoSuchElementException;
import java.util.Scanner;
import java.util.Stack;

public class UserHandler {
    private final int maxRewriteAttempts = 1;

    private Scanner userScanner;
    private Stack<File> scriptStack = new Stack<>();
    private Stack<Scanner> scannerStack = new Stack<>();

    public UserHandler(Scanner userScanner) {
        this.userScanner = userScanner;
    }


    public Request handle(ResponseCode serverResponseCode, User user) {
        String userInput;
        String[] userCommand;
        ProcessingCode processingCode;
        int rewriteAttempts = 0;
        try {
            do {
                try {
                    if (fileMode() && (serverResponseCode == ResponseCode.ERROR ||
                            serverResponseCode == ResponseCode.SERVER_EXIT))
                        throw new IncorrectInputInScriptException();
                    while (fileMode() && !userScanner.hasNextLine()) {
                        userScanner.close();
                        userScanner = scannerStack.pop();
                        scriptStack.pop();
                    }
                    if (fileMode()) {
                        userInput = userScanner.nextLine();
                        if (!userInput.isEmpty()) {
                            Outputer.print(Main.PS1);
                            Outputer.println(userInput);
                        }
                    } else {
                        Outputer.print(Main.PS1);
                        userInput = userScanner.nextLine();
                    }
                    userCommand = (userInput.trim() + " ").split(" ", 2);
                    userCommand[1] = userCommand[1].trim();
                } catch (NoSuchElementException | IllegalStateException exception) {
                    Outputer.println();
                    Outputer.printerror("An error occurred while entering the command!");
                    userCommand = new String[]{"", ""};
                    rewriteAttempts++;
                    if (rewriteAttempts >= maxRewriteAttempts) {
                        Outputer.printerror("Number of input attempts exceeded!");
                        System.exit(0);
                    }
                }
                processingCode = processCommand(userCommand[0], userCommand[1]);
            } while (processingCode == ProcessingCode.ERROR && !fileMode() || userCommand[0].isEmpty());
            try {
                if (fileMode() && (serverResponseCode == ResponseCode.ERROR || processingCode == ProcessingCode.ERROR))
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
                        Outputer.println("Executing a script '" + scriptFile.getName() + "'...");
                        break;
                }
            } catch (FileNotFoundException exception) {
                Outputer.printerror("Script file not found!");
            } catch (ScriptRecursionException exception) {
                Outputer.printerror("Scripts cannot be called recursively!");
                throw new IncorrectInputInScriptException();
            }
        } catch (IncorrectInputInScriptException exception) {
            Outputer.printerror("Script execution aborted!");
            while (!scannerStack.isEmpty()) {
                userScanner.close();
                userScanner = scannerStack.pop();
            }
            scriptStack.clear();
            return new Request(user);
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
                case "help":
                    if (!commandArgument.isEmpty()) throw new CommandUsageException();
                    break;
                case "info":
                    if (!commandArgument.isEmpty()) throw new CommandUsageException();
                    break;
                case "show":
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
                case "save":
                    if (!commandArgument.isEmpty()) throw new CommandUsageException();
                    break;
                case "execute_script":
                    if (commandArgument.isEmpty()) throw new CommandUsageException("<file_name>");
                    return ProcessingCode.SCRIPT;
                case "client_exit":
                    if (!commandArgument.isEmpty()) throw new CommandUsageException();
                    break;
                case "remove_greater":
                    if (!commandArgument.isEmpty()) throw new CommandUsageException("{element}");
                    return ProcessingCode.OBJECT;
                case "remove_lower":
                    if (!commandArgument.isEmpty()) throw new CommandUsageException("{element}");
                    return ProcessingCode.OBJECT;
                case "history":
                    if (!commandArgument.isEmpty()) throw new CommandUsageException();
                    break;
                case "count_by_employees_count":
                    if (commandArgument.isEmpty()) throw new CommandUsageException();
                    break;
                case "filter_contains_name":
                    if (commandArgument.isEmpty()) throw new CommandUsageException("<name>");
                    break;
                case "filter_greater_than_employees_count":
                    if (!commandArgument.isEmpty()) throw new CommandUsageException();
                    break;
                case "server_exit":
                    if (!commandArgument.isEmpty()) throw new CommandUsageException();
                    break;
                case "login":
                    if (!commandArgument.isEmpty()) throw new CommandUsageException();
                    break;
                case "register":
                    if (!commandArgument.isEmpty()) throw new CommandUsageException();
                    break;
                default:
                    Outputer.println("Command '" + command + "' is not found. Type 'help' for help.");
                    return ProcessingCode.ERROR;
            }
        } catch (CommandUsageException exception) {
            if (exception.getMessage() != null) command += " " + exception.getMessage();
            Outputer.println("Usage: '" + command + "'");
            return ProcessingCode.ERROR;
        }
        return ProcessingCode.OK;
    }


    private OrganizationRaw generateOrganizationAdd() throws IncorrectInputInScriptException {
        OrganizationAsker organizationAsker = new OrganizationAsker(userScanner);
        if (fileMode()) organizationAsker.setFileMode();
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


    private OrganizationRaw generateOrganizationUpdate() throws IncorrectInputInScriptException {
        OrganizationAsker organizationAsker = new OrganizationAsker(userScanner);
        if (fileMode()) organizationAsker.setFileMode();
        String name = organizationAsker.askQuestion("Do you want to change the name of the organization?") ?
                organizationAsker.askName() : null;
        Coordinates coordinates = organizationAsker.askQuestion("Do you want to change the coordinates of the organization?") ?
                organizationAsker.askCoordinates() : null;
        int annualTurnover = organizationAsker.askQuestion("Do you want to change the annual turnover of the organization?") ?
                organizationAsker.askTurnover() : null;
        String fullName = organizationAsker.askQuestion("Do you want to change the full name of the organization?") ?
                organizationAsker.askFullName() : null;
        Long employeesCount = organizationAsker.askQuestion("Do you want to change the number of employees in your organization?") ?
                organizationAsker.askEmployeesCount() : null;
        OrganizationType type = organizationAsker.askQuestion("Do you want to change the organization type?") ?
                organizationAsker.askType() : null;
        Address officialAddress = organizationAsker.askQuestion("Do you want to change the address of the organization?") ?
                organizationAsker.askAddress() : null;
        return new OrganizationRaw(
                name,
                coordinates,
                annualTurnover,
                fullName,
                employeesCount,
                type,
                officialAddress
        );
    }


    private boolean fileMode() {
        return !scannerStack.isEmpty();
    }
}
