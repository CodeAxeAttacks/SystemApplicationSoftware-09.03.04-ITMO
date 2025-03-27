package Commands;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Collection;
import CommandsSupport.*;
import Support.*;
import JsonSupport.*;

public class FilterGraterThanMinimalPoint implements Command {
    private final LabWorkCollection collection;
    public  BufferedReader reader;

    public FilterGraterThanMinimalPoint(LabWorkCollection collection, BufferedReader bufferedReader) {
        this.collection = collection;
        this.reader = bufferedReader;
    }


    @Override
    public void execute() throws IOException {
        System.out.print("minimalPoint: ");
        float minimalPoint = Float.parseFloat(reader.readLine());
        Collection<LabWork> labWorks = collection.getAll();
        for (LabWork labWork : labWorks) {
            if (labWork.getMinimalPoint() > minimalPoint) {
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
    }


    @Override
    public String getDescription() {
        return "filter_greater_than_minimal_point";
    }
    public void changeReader(BufferedReader bufferedReader) {
        this.reader = bufferedReader;
    }
}
