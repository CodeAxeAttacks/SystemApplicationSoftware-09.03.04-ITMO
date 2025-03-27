package ru.sayron.server.commands;

import ru.sayron.common.interaction.User;

/**
 * Interface for all commands.
 */
public interface Command {
    String getDescription();
    String getName();
    String getUsage();
    boolean execute(String stringArgument, Object objectArgument, User user);
}
