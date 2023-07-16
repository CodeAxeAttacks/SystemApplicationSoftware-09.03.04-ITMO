package CommandsSupport;

import CommandsSupport.Command;

import java.util.ArrayList;
import java.util.List;

public class CommandHistory {
    private final int maxCommands = 11;
    private final List<Command> commands = new ArrayList<>();

    public void addCommand(Command command) {
        if (commands.size() == maxCommands) {
            commands.remove(0);
        }
        commands.add(command);
    }

    public List<Command> getLastCommands() {
        return new ArrayList<>(commands);
    }
}
