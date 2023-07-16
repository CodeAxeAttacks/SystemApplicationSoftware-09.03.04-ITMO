package ru.sayron.server.commands;

/**
 * Interface for all commands.
 */
public interface Command {
    String getDescription();
    String getName();
    String getUsage();
    boolean execute(String stringArgument, Object objectArgument);
}
