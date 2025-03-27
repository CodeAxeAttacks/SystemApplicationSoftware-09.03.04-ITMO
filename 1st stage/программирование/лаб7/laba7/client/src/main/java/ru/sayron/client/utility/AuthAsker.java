package ru.sayron.client.utility;

import ru.sayron.client.Main;
import ru.sayron.common.exceptions.MustBeNotEmptyException;
import ru.sayron.common.exceptions.NotInDeclaredLimitsException;
import ru.sayron.common.utility.Outputer;

import java.util.NoSuchElementException;
import java.util.Scanner;

/**
 * Asks user a login and password.
 */
public class AuthAsker {
    private Scanner userScanner;

    public AuthAsker(Scanner userScanner) {
        this.userScanner = userScanner;
    }

    /**
     * Asks user a login.
     *
     * @return login.
     */
    public String askLogin() {
        String login;
        while (true) {
            try {
                Outputer.println("Enter login:");
                Outputer.print(Main.PS2);
                login = userScanner.nextLine().trim();
                if (login.equals("")) throw new MustBeNotEmptyException();
                break;
            } catch (NoSuchElementException exception) {
                Outputer.printerror("This login does not exist!");
            } catch (MustBeNotEmptyException exception) {
                Outputer.printerror("Name cannot be empty!");
            } catch (IllegalStateException exception) {
                Outputer.printerror("Unexpected error!");
                System.exit(0);
            }
        }
        return login;
    }

    /**
     * Asks user a password.
     *
     * @return password.
     */
    public String askPassword() {
        String password;
        while (true) {
            try {
                Outputer.println("Enter password:");
                Outputer.print(Main.PS2);
                password = userScanner.nextLine().trim();
                break;
            } catch (NoSuchElementException exception) {
                Outputer.printerror("Wrong login or password!");
            } catch (IllegalStateException exception) {
                Outputer.printerror("Unexpected error!");
                System.exit(0);
            }
        }
        return password;
    }

    /**
     * Asks a user a question.
     *
     * @param question A question.
     * @return Answer (true/false).
     */
    public boolean askQuestion(String question) {
        String finalQuestion = question + " (+/-):";
        String answer;
        while (true) {
            try {
                Outputer.println(finalQuestion);
                Outputer.print(Main.PS2);
                answer = userScanner.nextLine().trim();
                if (!answer.equals("+") && !answer.equals("-")) throw new NotInDeclaredLimitsException();
                break;
            } catch (NoSuchElementException exception) {
                Outputer.printerror("Answer not recognized!");
            } catch (NotInDeclaredLimitsException exception) {
                Outputer.printerror("The answer must be represented by '+' or '-'!");
            } catch (IllegalStateException exception) {
                Outputer.printerror("Unexpected error!");
                System.exit(0);
            }
        }
        return answer.equals("+");
    }
}
