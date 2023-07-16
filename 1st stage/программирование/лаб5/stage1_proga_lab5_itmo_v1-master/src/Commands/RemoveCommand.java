package Commands;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import CommandsSupport.*;
import Support.*;
import JsonSupport.*;

public class RemoveCommand implements Command {
    private final LabWorkCollection collection;

    public RemoveCommand(LabWorkCollection collection) {
        this.collection = collection;
    }

    @Override
    public void execute() throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        System.out.print("Enter the Support.LabWork ID to remove: ");
        String idStr = reader.readLine().trim();

        if (idStr.isEmpty()) {
            System.out.println("Invalid ID. Please enter a non-empty string.");
            return;
        }

        try {
            long id = Long.parseLong(idStr);
            if (collection.getById(id) == null) {
                System.out.println("Support.LabWork with ID " + id + " not found in the collection.");
            } else {
                collection.remove(id);
                System.out.println("Support.LabWork with ID " + id + " removed from the collection.");
            }
        } catch (NumberFormatException e) {
            System.out.println("Invalid ID format. Please enter a valid long integer.");
        }
    }

    @Override
    public String getDescription() {
        return "remove_key {id} : remove a Support.LabWork from the collection by its ID";
    }
}
