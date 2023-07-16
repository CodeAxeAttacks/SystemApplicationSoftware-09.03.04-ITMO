package Commands;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import CommandsSupport.*;
import Support.*;
import JsonSupport.*;

public class SaveCommand implements Command {
    private final LabWorkCollection collection;

    public SaveCommand(LabWorkCollection collection) {
        this.collection = collection;
    }

    @Override
    public void execute() throws IOException {
        JsonWriter writerT = new JsonWriter(collection, "testing.json");
            try {
                writerT.writeToJson();
                System.out.println("Collection saved to testing.json.");
            } catch (IOException e) {
                System.out.println("Error saving collection to testing.json: " + e.getMessage());
            }
    }

    @Override
    public String getDescription() {
        return "save";
    }
}
