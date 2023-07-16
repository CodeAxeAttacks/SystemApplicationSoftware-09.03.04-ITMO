import Commands.*;
import CommandsSupport.CommandProcessor;
import JsonSupport.Parser;
import Support.LabWorkCollection;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        if (args.length < 1) {
            System.out.println("Usage: java Main <filename>");
            System.exit(1);
        }

        String filename = args[0];
        Parser parser = new Parser(filename);
        try {
            LabWorkCollection labWorkCollection = parser.parse();
            CommandProcessor processor = new CommandProcessor(labWorkCollection, new InsertCommand(labWorkCollection, new BufferedReader(new InputStreamReader(System.in))),
                    new UpdateCommand(labWorkCollection,new BufferedReader(new InputStreamReader(System.in))), new ReplaceIfGreater(labWorkCollection, new BufferedReader(new InputStreamReader(System.in))),
                    new MinByMinimalPointCommand(labWorkCollection),new FilterGraterThanMinimalPoint(labWorkCollection, new BufferedReader(new InputStreamReader(System.in))),new ExecuteScriptCommand(labWorkCollection, new BufferedReader(new InputStreamReader(System.in))),
                    new ReplaceIfLowe(labWorkCollection, new BufferedReader(new InputStreamReader(System.in))));
            Scanner scanner = new Scanner(System.in);
            System.out.println("Welcome to the Support.LabWorkCollection program!\n" + "To check the list of commands type: help");
            while (true) {
                System.out.print("> ");
                String input = scanner.nextLine().trim();

                if (input.equalsIgnoreCase("exit")) {
                    System.out.println("Exiting...");
                    break;
                }

                processor.executeCommand(input);
            }
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }
    }
}
