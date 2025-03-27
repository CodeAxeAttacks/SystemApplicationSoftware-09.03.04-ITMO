package ru.sayron.client.utility;

import ru.sayron.common.interaction.Request;
import ru.sayron.common.interaction.User;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Scanner;

/**
 * Handle user login and password.
 */
public class AuthHandler {
    private final String loginCommand = "login";
    private final String registerCommand = "register";

    private Scanner userScanner;

    public AuthHandler(Scanner userScanner) {
        this.userScanner = userScanner;
    }

    /**
     * Handle user authentication.
     *
     * @return Request of user.
     */
    public Request handle() {
        AuthAsker authAsker = new AuthAsker(userScanner);
        String command = authAsker.askQuestion("Do you already have an account?") ? loginCommand : registerCommand;
        User user = new User(authAsker.askLogin(), authAsker.askPassword());
        return new Request(command, "", user);
    }
}

