package Commands;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Date;
import CommandsSupport.*;
import Support.*;
import JsonSupport.*;

public class InsertCommand implements Command {
    private final LabWorkCollection collection;
    public BufferedReader reader;
    public InsertCommand(LabWorkCollection collection, BufferedReader reader) {
        this.collection = collection;
        this.reader = reader;
    }


    @Override
    public void execute() throws IOException {
        java.util.Date date = new Date();
        System.out.println("Enter the Support.LabWork data:");
        System.out.print("ID: ");
        String id = reader.readLine();
        boolean checker = true;
        while (checker) {
            for (LabWork labWork : collection.getAll()) {
                if (labWork.getId() == Long.parseLong(id)) {
                    System.out.print("Invalid input. Element with this ID already exists, select another one: ");
                    id = reader.readLine();
                    checker = true;
                }
                else {
                    checker = false;
                }
            }
        }

        System.out.print("Name: ");
        String name = reader.readLine();

        System.out.print("Support.Coordinates (x y): ");
        Coordinates coordinates = null;
        while (coordinates == null) {
            try {
                String[] coordinatesInput = reader.readLine().split("\\s+");
                float x = Float.parseFloat(coordinatesInput[0]);
                double y = Double.parseDouble(coordinatesInput[1]);
                coordinates = new Coordinates(x, y);
            } catch (NumberFormatException | ArrayIndexOutOfBoundsException e) {
                System.out.print("Invalid input. Please enter two numbers separated by a space: ");
            }
        }

        System.out.print("Minimal Point: ");
        float minimalPoint = 0;
        boolean validInput = false;
        while (!validInput) {
            try {
                minimalPoint = Float.parseFloat(reader.readLine());
                validInput = true;
            } catch (NumberFormatException e) {
                System.out.print("Invalid input. Please enter a number: ");
            }
        }

        System.out.print("Support.Difficulty (VERY_EASY, NORMAL, IMPOSSIBLE, INSANE, TERRIBLE): ");
        Difficulty difficulty = null;
        while (difficulty == null) {
            try {
                String difficultyInput = reader.readLine().trim().toUpperCase();
                difficulty = Difficulty.valueOf(difficultyInput);
            } catch (IllegalArgumentException e) {
                System.out.print("Invalid input. Please enter a valid difficulty level: ");
            }
        }

        System.out.print("Support.Discipline Name: ");
        String disciplineName = reader.readLine();

        System.out.print("Self Study Hours: ");
        int selfStudyHours = 0;
        boolean validInput1 = false;
        while (!validInput1) {
            try {
                selfStudyHours = Integer.parseInt(reader.readLine());
                validInput1 = true;
            } catch (NumberFormatException e) {
                System.out.print("Invalid input. Please enter a number: ");
            }
        }

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

        System.out.println("New Support.LabWork added successfully.");
    }

    @Override
    public String getDescription() {
        return "insert";
    }

    public void changeReader(BufferedReader bufferedReader) {
        this.reader = bufferedReader;
    }
}
