package Commands;
import CommandsSupport.*;
import Support.*;
import JsonSupport.*;

public class HelpCommand implements Command {
    private final LabWorkCollection collection;

    public HelpCommand(LabWorkCollection collection) {
        this.collection = collection;
    }

    @Override
    public void execute() {
        System.out.println("------------------------------------------------\n" +
                "help : display help on available commands\n" +
                "info : print information about the collection to standard output (type, initialization date, number of elements, etc.)\n" +
                "show : print all elements of the collection to standard output\n" +
                "insert null {element} : add a new element with the given key\n" +
                "update id {element} : update the value of the collection element whose id is equal to the given one\n" +
                "remove_key null : remove an element from the collection by its key\n" +
                "clear : clear the collection\n" +
                "save : save the collection to a file\n" +
                "execute_script file_name : read and execute script from specified file. The script contains commands in the same form in which they are entered by the user in interactive mode.\n" +
                "exit : exit the program (without saving to a file)\n" +
                "history : print the last 11 commands (without their arguments)\n" +
                "replace_if_greater null {element} : replace value by key if new value is greater than old\n" +
                "replace_if_lowe null {element} : replace value by key if new value is less than old\n" +
                "min_by_minimal_point : output any object from the collection whose minimalPoint field value is minimal\n" +
                "group_counting_by_name : group the elements of the collection by the value of the name field, display the number of elements in each group\n" +
                "filter_greater_than_minimal_point minimalPoint : display elements whose minimalPoint field value is greater than the given one\n" +
                "------------------------------------------------");
    }


    @Override
    public String getDescription() {
        return "help";
    }
}
