package Commands;

import CommandsSupport.*;
import Support.*;
import JsonSupport.*;

public class ShowCommand implements Command {
    private final LabWorkCollection collection;

    public ShowCommand(LabWorkCollection collection) {
        this.collection = collection;
    }

    @Override
    public void execute() {
        if (collection.getSize() != 0) {
            for (LabWork labWork : collection.getAll()) {
                System.out.println("ID: " + labWork.getId());
                System.out.println("Name: " + labWork.getName());
                System.out.println("Support.Coordinates: (" + labWork.getCoordinates().getX() + ", " + labWork.getCoordinates().getY() + ")");
                System.out.println("Creation Date: " + labWork.getCreationDate());
                System.out.println("Minimal Point: " + labWork.getMinimalPoint());
                System.out.println("Support.Difficulty: " + labWork.getDifficulty());
                System.out.println("Support.Discipline: " + labWork.getDiscipline().getName() + " (self-study hours: " + labWork.getDiscipline().getSelfStudyHours() + ")");
                System.out.println("------------------------");
            }
        }
        else {
            System.out.println("The collection is empty.");
        }
    }

    @Override
    public String getDescription() {
        return "show";
    }
}
