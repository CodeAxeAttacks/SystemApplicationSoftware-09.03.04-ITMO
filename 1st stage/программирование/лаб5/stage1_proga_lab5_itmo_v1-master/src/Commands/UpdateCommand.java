package Commands;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import CommandsSupport.*;
import Support.*;
import JsonSupport.*;

public class UpdateCommand implements Command {
    private final LabWorkCollection collection;
    public  BufferedReader reader;

    public UpdateCommand(LabWorkCollection collection, BufferedReader bufferedReader) {
        this.collection = collection;
        this.reader = bufferedReader;
    }

    @Override
    public void execute() throws IOException {
        long id = 0;
        while (true) {
            try {
                System.out.print("ID: ");
                String idStr = reader.readLine();
                id = Long.parseLong(idStr);
                LabWork labWork = collection.getById(id);
                if (labWork == null) {
                    System.out.println("Support.LabWork with ID " + id + " not found. Please try again.");
                    continue;
                } else {
                    break;
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid input for ID. Please enter a number: ");
            }
        }
        LabWork labWork = collection.getById(id);
        System.out.println("Support.LabWork with ID " + id + ": " + labWork);
        System.out.print("Characteristic to update (name, coordinates, minimalPoint, difficulty, discipline): ");
        String characteristic = reader.readLine();

        while (!characteristic.equals("name") && !characteristic.equals("coordinates") && !characteristic.equals("minimalPoint") && !characteristic.equals("difficulty") && !characteristic.equals("discipline")) {
            System.out.print("Invalid input. Please enter one of the following characteristics: name, coordinates, minimalPoint, difficulty, discipline: ");
            characteristic = reader.readLine();
        }

        switch (characteristic) {
            case "name":
                System.out.print("New name: ");
                String name = reader.readLine();
                labWork.setName(name);
                break;
            case "coordinates":
                System.out.print("Enter new X coordinate: ");
                String xStr = reader.readLine();
                System.out.print("Enter new Y coordinate: ");
                String yStr = reader.readLine();

                double x = 0;
                double y = 0;
                boolean validInput = false;

                while (!validInput) {
                    try {
                        x = Double.parseDouble(xStr);
                        y = Double.parseDouble(yStr);
                        validInput = true;
                    } catch (NumberFormatException e) {
                        System.out.print("Invalid input. Please enter numbers for the coordinates.");
                        System.out.print("Enter new X coordinate: ");
                        xStr = reader.readLine();
                        System.out.print("Enter new Y coordinate: ");
                        yStr = reader.readLine();
                    }
                }

                Coordinates coordinates = new Coordinates((float) x, y);
                labWork.setCoordinates(coordinates);
                System.out.println("Support.Coordinates of the lab work with ID " + id + " have been updated to (" + x + ", " + y + ").");

                break;
            case "minimalPoint":
                System.out.print("New minimal point: ");
                String minimalPointStr = reader.readLine();

                float minimalPoint = 0;
                boolean validInput1 = false;

                while (!validInput1) {
                    try {
                        minimalPoint = Float.parseFloat(minimalPointStr);
                        validInput1 = true;
                    } catch (NumberFormatException e) {
                        System.out.print("Invalid input. Please enter a number for the minimal point: ");
                        minimalPointStr = reader.readLine();
                    }
                }

                labWork.setMinimalPoint(minimalPoint);
                System.out.println("Minimal point of the lab work has been updated to " + minimalPoint + ".");

                break;
            case "difficulty":
                System.out.print("New difficulty (VERY_EASY, NORMAL, IMPOSSIBLE, INSANE, TERRIBLE): ");
                String difficultyStr = reader.readLine().toUpperCase();
                Difficulty difficulty;

                while (true) {
                    try {
                        difficulty = Difficulty.valueOf(difficultyStr);
                        break;
                    } catch (IllegalArgumentException e) {
                        System.out.print("Invalid input. Please enter one of the following difficulties: VERY_EASY, NORMAL, IMPOSSIBLE, INSANE, TERRIBLE: ");
                        difficultyStr = reader.readLine().toUpperCase();
                    }
                }

                labWork.setDifficulty(difficulty);
                System.out.println("Support.Difficulty of the lab work has been updated to " + difficulty + ".");

                break;
            case "discipline":
                System.out.print("New discipline (name, selfStudyHours): ");
                String discipline = reader.readLine();
                switch (discipline) {
                    case "name":
                        System.out.print("New name: ");
                        String name1 = reader.readLine();
                        labWork.getDiscipline().setName(name1);
                        break;
                    case "selfStudyHours":
                        System.out.print("New self-study hours: ");
                        String selfStudyHoursStr = reader.readLine();
                        int selfStudyHours;

                        while (true) {
                            try {
                                selfStudyHours = Integer.parseInt(selfStudyHoursStr);
                                break;
                            } catch (NumberFormatException e) {
                                System.out.print("Invalid input. Please enter a valid number: ");
                                selfStudyHoursStr = reader.readLine();
                            }
                        }

                        labWork.getDiscipline().setSelfStudyHours(selfStudyHours);

                        break;
                    default:
                        System.out.println("Unknown discipline characteristic: " + discipline);
                        break;
                }
                break;
            default:
                System.out.println("Unknown characteristic: " + characteristic);
                break;
        }
    }

    @Override
    public String getDescription() {
        return "update";
    }
    public void changeReader(BufferedReader bufferedReader) {
        this.reader = bufferedReader;
    }
}
