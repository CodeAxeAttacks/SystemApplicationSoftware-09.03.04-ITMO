package ru.sayron.client.command;

import ru.sayron.client.command.ExecScript;
import ru.sayron.client.utility.AuthHandler;
import ru.sayron.common.exceptions.ConnectionErrorException;
import ru.sayron.common.exceptions.NotInDeclaredLimitsException;
import ru.sayron.common.interaction.Request;
import ru.sayron.common.interaction.Response;
import ru.sayron.common.interaction.User;
import ru.sayron.common.utility.Outputer;

import java.io.*;
import java.net.InetSocketAddress;
import java.nio.channels.SocketChannel;

public class Client1 {
    private String host;
    private int port;
    private int reconnectionTimeout;
    private int reconnectionAttempts;
    private int maxReconnectionAttempts;
    private ExecScript userHandler;
    private SocketChannel socketChannel;
    private ObjectOutputStream serverWriter;
    private ObjectInputStream serverReader;
    private AuthHandler authHandler;
    private User user;

    public Client1(String host, int port, int reconnectionTimeout, int maxReconnectionAttempts, ExecScript userHandler, AuthHandler authHandler) {
        this.host = host;
        this.port = port;
        this.reconnectionTimeout = reconnectionTimeout;
        this.maxReconnectionAttempts = maxReconnectionAttempts;
        this.userHandler = userHandler;
        this.authHandler = authHandler;
    }


    public void run() {
        try {
            boolean processingStatus = true;
            while (processingStatus) {
                try {
                    connectToServer();
                    processingStatus = processRequestToServer();
                } catch (ConnectionErrorException exception) {
                    if (reconnectionAttempts >= maxReconnectionAttempts) {
                        Outputer.printerror("Connection attempts exceeded!");
                        break;
                    }
                    try {
                        Thread.sleep(reconnectionTimeout);
                    } catch (IllegalArgumentException timeoutException) {
                        Outputer.printerror("Connection timeout '" + reconnectionTimeout +
                                "' is out of range!");
                        Outputer.println("Reconnection will be made immediately.");
                    } catch (Exception timeoutException) {
                        Outputer.printerror("An error occurred while trying to connect!");
                        Outputer.println("Reconnection will be made immediately.");
                    }
                }
                reconnectionAttempts++;
            }
            if (socketChannel != null) socketChannel.close();
            Outputer.println("Client1 work completed successfully.");
        } catch (NotInDeclaredLimitsException exception) {
            Outputer.printerror("The Client1 cannot be started!");
        } catch (IOException exception) {
            Outputer.printerror("An error occurred while trying to terminate the connection to the server!");
        }
    }


    private void connectToServer() throws ConnectionErrorException, NotInDeclaredLimitsException {
        try {
            if (reconnectionAttempts >= 1) Outputer.println("Reconnecting to the server...");
            socketChannel = SocketChannel.open(new InetSocketAddress(host, port));
            Outputer.println("The connection to the server was successfully established.");
            Outputer.println("Waiting for permission to share data...");
            serverWriter = new ObjectOutputStream(socketChannel.socket().getOutputStream());
            serverReader = new ObjectInputStream(socketChannel.socket().getInputStream());
            Outputer.println("Permission to share data received.");
        } catch (IllegalArgumentException exception) {
            Outputer.printerror("Server address entered incorrectly!");
            throw new NotInDeclaredLimitsException();
        } catch (IOException exception) {
            Outputer.printerror("An error occurred while connecting to the server!");
            throw new ConnectionErrorException();
        }
    }


    private boolean processRequestToServer() {
        Request requestToServer = null;
        Response serverResponse = null;
        do {
            try {
                requestToServer = serverResponse != null ? userHandler.handle(serverResponse.getResponseCode(), user) :
                        userHandler.handle(null, user);
                if (requestToServer.isEmpty()) continue;
                serverWriter.writeObject(requestToServer);
                serverResponse = (Response) serverReader.readObject();
                Outputer.print(serverResponse.getResponseBody());
            } catch (InvalidClassException | NotSerializableException exception) {
                Outputer.printerror("An error occurred while sending data to the server!");
            } catch (ClassNotFoundException exception) {
                Outputer.printerror("An error occurred while reading the received data!");
            } catch (IOException exception) {
                Outputer.printerror("The connection to the server has been broken!");
                try {
                    reconnectionAttempts++;
                    connectToServer();
                } catch (ConnectionErrorException | NotInDeclaredLimitsException reconnectionException) {
                    if (requestToServer.getCommandName().equals("exit"))
                        Outputer.println("The team will not be registered on the server.");
                    else Outputer.println("Try to repeat the command later.");
                }
            }
        } while (!requestToServer.getCommandName().equals("client_exit"));
        return false;
    }
}
