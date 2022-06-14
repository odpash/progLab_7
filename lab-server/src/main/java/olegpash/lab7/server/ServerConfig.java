package olegpash.lab7.server;

import olegpash.lab7.common.abstractions.AbstractTextPrinter;
import olegpash.lab7.common.util.CommandHistory;
import olegpash.lab7.common.util.ConsoleTextPrinter;
import olegpash.lab7.server.abstractions.AbstractClientCommand;
import olegpash.lab7.server.abstractions.AbstractServerCommand;

import java.util.HashMap;

public final class ServerConfig {

    private static final AbstractTextPrinter CONSOLE_TEXT_PRINTER = new ConsoleTextPrinter();
    private static final HashMap<String, AbstractClientCommand> CLIENT_AVAILABLE_COMMANDS = new HashMap<>();
    private static final HashMap<String, AbstractServerCommand> SERVER_AVAILABLE_COMMANDS = new HashMap<>();
    private static final CommandHistory CLIENT_COMMAND_HISTORY = new CommandHistory();
    private static boolean isRunning = true;

    private ServerConfig() {
    }

    public static boolean getRunning() {
        return isRunning;
    }

    public static void toggleRun() {
        isRunning = !isRunning;
    }

    public static AbstractTextPrinter getConsoleTextPrinter() {
        return CONSOLE_TEXT_PRINTER;
    }

    public static HashMap<String, AbstractClientCommand> getClientAvailableCommands() {
        return CLIENT_AVAILABLE_COMMANDS;
    }

    public static HashMap<String, AbstractServerCommand> getServerAvailableCommands() {
        return SERVER_AVAILABLE_COMMANDS;
    }

    public static CommandHistory getClientCommandHistory() {
        return CLIENT_COMMAND_HISTORY;
    }
}
