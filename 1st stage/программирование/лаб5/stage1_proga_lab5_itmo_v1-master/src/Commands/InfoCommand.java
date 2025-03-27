package Commands;

import CommandsSupport.*;
import Support.*;
import JsonSupport.*;

public class InfoCommand implements Command {
    private final LabWorkCollection collection;

    public InfoCommand(LabWorkCollection collection) {
        this.collection = collection;
    }

    @Override
    public void execute() {
        System.out.println("Type: " + collection.getClass().getSimpleName());
        System.out.println("Initialization Date: " + collection.getInitializationDate());
        System.out.println("Number of Elements: " + collection.getSize());
    }

    @Override
    public String getDescription() {
        return "info";
    }
}
