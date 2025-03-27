package ru.sayron.client;

import ru.sayron.client.command.Client1;
import ru.sayron.client.command.ExecScript;
import ru.sayron.client.utility.UserHandler;
import ru.sayron.common.exceptions.NotInDeclaredLimitsException;
import ru.sayron.common.exceptions.WrongAmountOfElementsException;
import ru.sayron.common.utility.Outputer;
import ru.sayron.client.utility.AuthHandler;

import java.util.Scanner;

public class Main {
    public static final String PS1 = "$ ";
    public static final String PS2 = "> ";

    private static final int RECONNECTION_TIMEOUT = 5 * 1000;
    private static final int MAX_RECONNECTION_ATTEMPTS = 5;

    private static String host;
    private static int port;
    private static String script;

/*  private static boolean initializeConnectionAddress(String[] hostAndPortArgs) {
        try {
            if (hostAndPortArgs.length > 3 ) throw new WrongAmountOfElementsException();
            if (hostAndPortArgs.length == 2) {
                host = hostAndPortArgs[0];
                port = Integer.parseInt(hostAndPortArgs[1]);
            } else if (hostAndPortArgs.length == 3) {
                host = hostAndPortArgs[0];
                port = Integer.parseInt(hostAndPortArgs[1]);
                script = hostAndPortArgs[2];
            }
            if (port < 0) throw new NotInDeclaredLimitsException();
            return true;
        } catch (WrongAmountOfElementsException exception) {
            String jarName = new java.io.File(Main.class.getProtectionDomain()
                    .getCodeSource()
                    .getLocation()
                    .getPath())
                    .getName();
            Outputer.println("Usage: 'java -jar " + jarName + " <host> <port>'");
        } catch (NumberFormatException exception) {
            Outputer.printerror("The port must be represented by a number!");
        } catch (NotInDeclaredLimitsException exception) {
            Outputer.printerror("The port cannot be negative!");
        }
        return false;
    }
*/
    public static void main(String[] args) {
        if (!initialize(args)) return;
        Scanner userScanner = new Scanner(System.in);
        AuthHandler authHandler = new AuthHandler(userScanner);
        UserHandler userHandler = new UserHandler(userScanner);
        Client client = new Client(host, port, RECONNECTION_TIMEOUT, MAX_RECONNECTION_ATTEMPTS, userHandler, authHandler);
        client.run();
        userScanner.close();
    }

    private static boolean initialize(String[] args) {
        try {
            if (args.length != 2) throw new WrongAmountOfElementsException();
            host = args[0];
            port = Integer.parseInt(args[1]);
            if (port < 0) throw new NotInDeclaredLimitsException();
            return true;
        } catch (WrongAmountOfElementsException exception) {
            String jarName = new java.io.File(Main.class.getProtectionDomain()
                    .getCodeSource()
                    .getLocation()
                    .getPath())
                    .getName();
            Outputer.println("Использование: 'java -jar " + jarName + " <host> <port>'");
        } catch (NumberFormatException exception) {
            Outputer.printerror("Порт должен быть представлен числом!");
        } catch (NotInDeclaredLimitsException exception) {
            Outputer.printerror("Порт не может быть отрицательным!");
        }
        return false;
    }


/*    public static void main(String[] args) {
        if (!initializeConnectionAddress(args)) return;
        if (script == null) {
            Scanner userScanner = new Scanner(System.in);
            UserHandler userHandler = new UserHandler(userScanner);
            Client client = new Client(host, port, RECONNECTION_TIMEOUT, MAX_RECONNECTION_ATTEMPTS, userHandler);
            client.run();
            userScanner.close();
        } else {
            Scanner userScanner = new Scanner(System.in);
            ExecScript execScript = new ExecScript(userScanner, script);
            Client1 client = new Client1(host, port, RECONNECTION_TIMEOUT, MAX_RECONNECTION_ATTEMPTS, execScript);
            client.run();
            userScanner.close();
        }
    }
 */
}