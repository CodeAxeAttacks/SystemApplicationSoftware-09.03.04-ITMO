package Commands;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Date;
import CommandsSupport.*;
import Support.*;
import JsonSupport.*;

public class ReplaceIfLowe implements Command {
    private final LabWorkCollection collection;
    public  BufferedReader reader;

    public ReplaceIfLowe(LabWorkCollection collection, BufferedReader bufferedReader) {
        this.collection = collection;
        this.reader = bufferedReader;
    }

    @Override
    public void execute() throws IOException {
        java.util.Date date = new Date();
        System.out.print("ID to compare: ");
        String idc = reader.readLine();
        System.out.println("Enter the Support.LabWork data:");

        System.out.print("ID: ");
        String id = reader.readLine();

        System.out.print("Name: ");
        String name = reader.readLine();

        System.out.print("Support.Coordinates (x y): ");
        String[] coordinatesInput = reader.readLine().split("\\s+");
        Coordinates coordinates = new Coordinates((float) Double.parseDouble(coordinatesInput[0]), Double.parseDouble(coordinatesInput[1]));

        System.out.print("Minimal Point: ");
        float minimalPoint = Float.parseFloat(reader.readLine());

        System.out.print("Support.Difficulty (VERY_EASY, NORMAL, IMPOSSIBLE, INSANE, TERRIBLE): ");
        String difficultyInput = reader.readLine().toUpperCase();
        Difficulty difficulty = Difficulty.valueOf(difficultyInput);

        System.out.print("Support.Discipline Name: ");
        String disciplineName = reader.readLine();

        System.out.print("Self Study Hours: ");
        int selfStudyHours = Integer.parseInt(reader.readLine());

        Discipline discipline = new Discipline(disciplineName, selfStudyHours);

        LabWork labWork = new LabWork();
        labWork.setId(Long.parseLong(id));
        labWork.setName(name);
        labWork.setCoordinates(coordinates);
        labWork.setMinimalPoint(minimalPoint);
        labWork.setCreationDate(date);
        labWork.setDifficulty(difficulty);
        labWork.setDiscipline(discipline);

        collection.add(labWork);

        if (Long.parseLong(idc) > Long.parseLong(id)) {
            try {
                if (collection.getById(Long.parseLong(idc)) == null) {
                    System.out.println("Support.LabWork with ID " + idc + " not found in the collection.");
                } else {
                    collection.remove(Long.parseLong(idc));
                    System.out.println("Support.LabWork with ID " + idc + " removed from the collection.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid ID format. Please enter a valid long integer.");
            }
        }
        else {
            try {
                if (collection.getById(Long.parseLong(id)) == null) {
                    System.out.println("Support.LabWork with ID " + id + " not found in the collection.");
                } else {
                    collection.remove(Long.parseLong(id));
                    System.out.println("Support.LabWork with ID " + id + " removed from the collection.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid ID format. Please enter a valid long integer.");
            }
        }
    }

    @Override
    public String getDescription() {
        return "replace_if_greater";
    }
    public void changeReader(BufferedReader bufferedReader){
        this.reader = bufferedReader;
    }
}
