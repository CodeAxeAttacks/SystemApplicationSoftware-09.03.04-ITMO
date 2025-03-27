package Commands;
import CommandsSupport.*;
import Support.*;
import JsonSupport.*;

public class ClearCommand implements Command {
    private final LabWorkCollection collection;

    public ClearCommand(LabWorkCollection collection) {
        this.collection = collection;
    }
    @Override
    public void execute() {
        collection.clearAll();
        System.out.println("The collection is empty.");
    }

    @Override
    public String getDescription() {
        return "clear";
    }
}
