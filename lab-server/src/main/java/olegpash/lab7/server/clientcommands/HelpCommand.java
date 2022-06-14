package olegpash.lab7.server.clientcommands;

import olegpash.lab7.common.util.Request;
import olegpash.lab7.common.util.Response;
import olegpash.lab7.server.abstractions.AbstractClientCommand;
import olegpash.lab7.server.util.CommandProcessor;

import java.util.HashMap;

public class HelpCommand extends AbstractClientCommand {

    private final CommandProcessor commandProcessor;
    private final HashMap<String, AbstractClientCommand> availableCommands;

    public HelpCommand(HashMap<String, AbstractClientCommand> availableCommands, CommandProcessor commandProcessor) {
        super("help",
                0,
                "show list available commands");
        this.availableCommands = availableCommands;
        this.commandProcessor = commandProcessor;
    }

    @Override
    public Response executeClientCommand(Request request) {
        return commandProcessor.help(request, availableCommands);
    }
}
