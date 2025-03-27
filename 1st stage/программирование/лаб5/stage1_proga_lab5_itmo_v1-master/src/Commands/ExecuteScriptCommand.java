package Commands;

import java.io.*;
import java.lang.reflect.Executable;
import java.lang.reflect.InvocationTargetException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.lang.reflect.Method;
import CommandsSupport.*;
import Support.*;
import JsonSupport.*;

public class ExecuteScriptCommand implements Command {
    private final LabWorkCollection collection;
    public  BufferedReader rd;
    static Stack<String> stackWithFiles = new Stack<>();
    static Stack<BufferedReader> stackWithReaders = new Stack<>();
    public String previousLine = "";
    public static int count = 0;

    public ExecuteScriptCommand(LabWorkCollection collection, BufferedReader bufferedReader) {
        this.collection = collection;
        this.rd = bufferedReader;
    }


    @Override
    public void execute() {
        String fileName = null;
        try {
            System.out.print("fileName: ");
            fileName = rd.readLine();
        } catch (Exception e) {
            System.out.println("Exeption!");
        }
        stackWithFiles.push(fileName);
        try {
            assert fileName != null;
            try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
                File file = new File(fileName);
                while (!file.exists()) {
                    System.out.println("File not found. Please enter a valid file name:");
                    fileName = rd.readLine();
                    file = new File(fileName);
                }
                stackWithReaders.push(reader);
                collection.changeReaders(reader);
                String input;
                while ((input = reader.readLine()) != null) {
                    if (input.equals("execute_script") && !checkRecursion(input, fileName)) {
                        throw new Exception("Exeption! Check the data!");
                    }
                    Map<String, Command> processor = collection.sendCommandMap();
                    String[] parts = input.split("\\s+");
                    String commandName = parts[0];
                    Command command = processor.get(commandName);
                    command.execute();
                }

            }
        } catch (Exception e) {
            System.out.println("Exeption!");
        } finally {
            stackWithReaders.pop();
            try {
                collection.changeReaders(stackWithReaders.peek());
            } catch (EmptyStackException e) {
                collection.changeReaders(new BufferedReader(new InputStreamReader(System.in)));
            }
        }
    }


    @Override
    public String getDescription() {
        return "execute_script";
    }
    public void changeReader(BufferedReader bufferedReader) {
        this.rd = bufferedReader;
    }
    public boolean checkRecursion(String currentCommand, String fileName) {
        if (count > 20) {
            System.exit(0);
        }
        System.out.println("COMMAND: " + currentCommand);
        System.out.println("FILE: " + fileName);
        try {
            if (Objects.equals(currentCommand, "execute_script") && stackWithFiles.contains(fileName)) {
                return false;
            } else if (Objects.equals(currentCommand, "execute_script") && !stackWithFiles.contains(fileName)) {
                Path path = Paths.get(currentCommand);
                stackWithFiles.push(currentCommand);
            }
        } catch (Exception e) {
            System.out.println("Exeption!");
        }
        return true;
    }
}
