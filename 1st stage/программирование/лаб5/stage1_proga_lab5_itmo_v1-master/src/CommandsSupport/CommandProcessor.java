package CommandsSupport;

import Commands.*;
import Support.*;
import JsonSupport.*;
import java.util.HashMap;
import java.util.Map;

public class CommandProcessor {
    private final LabWorkCollection collection;
    private final Map<String, Command> commands;
    private final CommandHistory history;
    private InsertCommand insertCommand;
    private UpdateCommand updateCommand;
    private ReplaceIfGreater replaceIfGreater;
    private MinByMinimalPointCommand minByMinimalPointCommand;
    private FilterGraterThanMinimalPoint filterGraterThanMinimalPoint;
    private ExecuteScriptCommand executeScriptCommand;
    private ReplaceIfLowe replaceIfLowe;


    public CommandProcessor(LabWorkCollection collection, InsertCommand insertCommand, UpdateCommand updateCommand,
                            ReplaceIfGreater replaceIfGreater, MinByMinimalPointCommand minByMinimalPointCommand,
                            FilterGraterThanMinimalPoint filterGraterThanMinimalPoint, ExecuteScriptCommand executeScriptCommand,
                            ReplaceIfLowe replaceIfLowe) {
        this.insertCommand = insertCommand;
        this.updateCommand = updateCommand;
        this.replaceIfGreater = replaceIfGreater;
        this.minByMinimalPointCommand = minByMinimalPointCommand;
        this.filterGraterThanMinimalPoint = filterGraterThanMinimalPoint;
        this.executeScriptCommand = executeScriptCommand;
        this.replaceIfLowe = replaceIfLowe;
        this.collection = collection;
        commands = new HashMap<>();
        history = new CommandHistory();
        commands.put("show", new ShowCommand(collection));
        commands.put("info", new InfoCommand(collection));
        commands.put("help", new HelpCommand(collection));
        commands.put("clear", new ClearCommand(collection));
        commands.put("min_by_minimal_point", new MinByMinimalPointCommand(collection));
        commands.put("save", new SaveCommand(collection));
        commands.put("group_counting_by_name", new GroupCountingByNameCommand(collection));
        commands.put("history", new HistoryCommand(history));
        commands.put("remove", new RemoveCommand(collection));
        commands.put("insert", this.insertCommand);
        commands.put("update", this.updateCommand);
        commands.put("replace_if_greater", this.replaceIfGreater);
        commands.put("filter_greater_than_minimal_point", this.filterGraterThanMinimalPoint);
        commands.put("execute_script", this.executeScriptCommand);
        commands.put("replace_if_lowe", this.replaceIfLowe);
        collection.getMappingOfCommands(commands);
        collection.getInsetCommand(this.insertCommand);
        collection.getUpdateCommand(this.updateCommand);
        collection.getExecuteScriptCommand(this.executeScriptCommand);
        collection.getFilterGraterThanMinimalPoint(this.filterGraterThanMinimalPoint);
        collection.getReplaceIfGreater(this.replaceIfGreater);
        collection.getReplaceIfLowe(this.replaceIfLowe);
    }


    public void executeCommand(String input) {
        String[] parts = input.split("\\s+");
        String commandName = parts[0];
        Command command = commands.get(commandName);
        if (command == null) {
            System.out.println("Unknown command: " + commandName);
            return;
        }
        try {
            command.execute();
        }catch (Exception e){
            System.out.println("command.execute();");
        }
        history.addCommand(command);
    }
}