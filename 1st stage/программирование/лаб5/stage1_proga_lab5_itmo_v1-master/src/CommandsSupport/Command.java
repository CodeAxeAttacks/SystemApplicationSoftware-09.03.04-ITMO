package CommandsSupport;

import java.io.IOException;

public interface Command {
    void execute() throws IOException;
    String getDescription();
}
