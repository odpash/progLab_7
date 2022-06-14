package olegpash.lab7.server.clientcommands;

import olegpash.lab7.common.util.Request;
import olegpash.lab7.common.util.Response;
import olegpash.lab7.server.abstractions.AbstractClientCommand;
import olegpash.lab7.server.util.CommandProcessor;

public class AddCommand extends AbstractClientCommand {

    private final CommandProcessor commandProcessor;

    public AddCommand(CommandProcessor commandProcessor) {
        super("add",
                0,
                "add a new item to the collection");
        this.commandProcessor = commandProcessor;
    }

    @Override
    public Response executeClientCommand(Request request) {
        return commandProcessor.add(request);
    }
}
