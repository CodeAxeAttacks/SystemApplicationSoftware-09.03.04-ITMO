package ru.sayron.server;

import ch.qos.logback.classic.Logger;
import org.slf4j.LoggerFactory;
import ru.sayron.server.commands.*;
import ru.sayron.server.utility.*;

/**
 * Main server class. Creates all server instances.
 *
 * @author .
 */
public class Main {
    public static final int PORT = 1821;
    public static final int CONNECTION_TIMEOUT = 60 * 1000;
    public static final String ENV_VARIABLE = "LAB5";
    public static Logger logger = (Logger) LoggerFactory.getLogger("ServerLogger");

    public static void main(String[] args) {
        FileManager collectionFileManager = new FileManager(ENV_VARIABLE);
        CollectionManager collectionManager = new CollectionManager(collectionFileManager);
        collectionManager.loadCollection();
        CommandManager commandManager = new CommandManager(
                new HelpCommand(),
                new InfoCommand(collectionManager),
                new ShowCommand(collectionManager),
                new AddCommand(collectionManager),
                new UpdateIdCommand(collectionManager),
                new RemoveByIdCommand(collectionManager),
                new ClearCommand(collectionManager),
                new SaveCommand(collectionManager),
                new ClientExitCommand(),
                new ExecuteScriptCommand(),
                new ExecScriptCommand(),
                new EmployeesCountCommand(collectionManager),
                new RemoveGreaterCommand(collectionManager),
                new HistoryCommand(),
                new FilterContainsNameCommand(collectionManager),
                new RemoveLowerCommand(collectionManager),
                new FilterGreaterThanEmployeesCountCommand(collectionManager),
                new ServerExitCommand()
        );
        RequestHandler requestHandler = new RequestHandler(commandManager);
        Server server = new Server(PORT, CONNECTION_TIMEOUT, requestHandler);
        server.run();
    }
}
