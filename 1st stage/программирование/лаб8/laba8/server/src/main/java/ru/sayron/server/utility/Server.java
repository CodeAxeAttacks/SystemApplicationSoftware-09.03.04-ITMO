package ru.sayron.server.utility;


import ru.sayron.common.exceptions.ClosingSocketException;
import ru.sayron.common.exceptions.ConnectionErrorException;
import ru.sayron.common.exceptions.OpeningServerSocketException;
import ru.sayron.server.utility.CollectionManager;
import ru.sayron.server.utility.CommandManager;
import ru.sayron.server.utility.ConnectionHandler;
import ru.sayron.server.utility.Outputer;
import ru.sayron.server.Main;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

/**
 * Runs the server.
 */
public class Server implements Runnable {
    private int port;
    private ServerSocket serverSocket;
    private CommandManager commandManager;
    private CollectionManager collectionManager;
    private boolean isStopped;
    private ExecutorService cachedThreadPool = Executors.newCachedThreadPool();
    private Semaphore semaphore;

    public Server(int port, int maxClients, CommandManager commandManager, CollectionManager collectionManager) {
        this.port = port;
        this.commandManager = commandManager;
        this.collectionManager = collectionManager;
        this.semaphore = new Semaphore(maxClients);
    }

    /**
     * Begins server operation.
     */
    @Override
    public void run() {
        try {
            openServerSocket();
            while (!isStopped()) {
                try {
                    acquireConnection();
                    if (isStopped()) throw new ConnectionErrorException();
                    Socket clientSocket = connectToClient();
                    cachedThreadPool.submit(new ConnectionHandler(this, clientSocket, commandManager,
                            collectionManager));
                } catch (ConnectionErrorException exception) {
                    if (!isStopped()) {
                        Outputer.printerror("Произошла ошибка при соединении с клиентом!");
                        Main.logger.error("Произошла ошибка при соединении с клиентом!");
                    } else break;
                }
            }
            cachedThreadPool.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
            Outputer.println("Работа сервера завершена.");
        } catch (OpeningServerSocketException exception) {
            Outputer.printerror("Сервер не может быть запущен!");
            Main.logger.error("Сервер не может быть запущен!");
        } catch (InterruptedException e) {
            Outputer.printerror("Произошла ошибка при завершении работы с уже подключенными клиентами!");
        }
    }

    /**
     * Acquire connection.
     */
    public void acquireConnection() {
        try {
            semaphore.acquire();
            Main.logger.info("Разрешение на новое соединение получено.");
        } catch (InterruptedException exception) {
            Outputer.printerror("Произошла ошибка при получении разрешения на новое соединение!");
            Main.logger.error("Произошла ошибка при получении разрешения на новое соединение!");
        }
    }

    /**
     * Release connection.
     */
    public void releaseConnection() {
        semaphore.release();
        Main.logger.info("Разрыв соединения зарегистрирован.");
    }

    /**
     * Finishes server operation.
     */
    public synchronized void stop() {
        try {
            Main.logger.info("Завершение работы сервера...");
            if (serverSocket == null) throw new ClosingSocketException();
            isStopped = true;
            cachedThreadPool.shutdown();
            serverSocket.close();
            Outputer.println("Завершение работы с уже подключенными клиентами...");
            Main.logger.info("Работа сервера завершена.");
        } catch (ClosingSocketException exception) {
            Outputer.printerror("Невозможно завершить работу еще не запущенного сервера!");
            Main.logger.error("Невозможно завершить работу еще не запущенного сервера!");
        } catch (IOException exception) {
            Outputer.printerror("Произошла ошибка при завершении работы сервера!");
            Outputer.println("Завершение работы с уже подключенными клиентами...");
            Main.logger.error("Произошла ошибка при завершении работы сервера!");
        }
    }

    /**
     * Checked stops of server.
     *
     * @return Status of server stop.
     */
    private synchronized boolean isStopped() {
        return isStopped;
    }

    /**
     * Open server socket.
     */
    private void openServerSocket() throws OpeningServerSocketException {
        try {
            Main.logger.info("Запуск сервера...");
            serverSocket = new ServerSocket(port);
            Main.logger.info("Сервер запущен.");
        } catch (IllegalArgumentException exception) {
            Outputer.printerror("Порт '" + port + "' находится за пределами возможных значений!");
            Main.logger.error("Порт '" + port + "' находится за пределами возможных значений!");
            throw new OpeningServerSocketException();
        } catch (IOException exception) {
            Outputer.printerror("Произошла ошибка при попытке использовать порт '" + port + "'!");
            Main.logger.error("Произошла ошибка при попытке использовать порт '" + port + "'!");
            throw new OpeningServerSocketException();
        }
    }

    /**
     * Connecting to client.
     */
    private Socket connectToClient() throws ConnectionErrorException {
        try {
            Outputer.println("Прослушивание порта '" + port + "'...");
            Main.logger.info("Прослушивание порта '" + port + "'...");
            Socket clientSocket = serverSocket.accept();
            Outputer.println("Соединение с клиентом установлено.");
            Main.logger.info("Соединение с клиентом установлено.");
            return clientSocket;
        } catch (IOException exception) {
            throw new ConnectionErrorException();
        }
    }
}
