package Commands;

import java.util.List;
import CommandsSupport.*;
import Support.*;
import JsonSupport.*;

public class HistoryCommand implements Command {
    private final CommandHistory history;

    public HistoryCommand(CommandHistory history) {
        this.history = history;
    }

    @Override
    public void execute() {
        List<Command> commands = history.getLastCommands();
        int startIdx = Math.max(0, commands.size() - 11);
        for (int i = startIdx; i < commands.size(); i++) {
            Command command = commands.get(i);
            System.out.println(command.getDescription());
        }
    }

    @Override
    public String getDescription() {
        return "history";
    }
}
