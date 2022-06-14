package olegpash.lab7.server.clientcommands;

import olegpash.lab7.common.util.Request;
import olegpash.lab7.common.util.Response;
import olegpash.lab7.server.abstractions.AbstractClientCommand;
import olegpash.lab7.server.util.CommandProcessor;

public class AddIfMaxCommand extends AbstractClientCommand {

    private final CommandProcessor commandProcessor;

    public AddIfMaxCommand(CommandProcessor commandProcessor) {
        super("add_if_max",
                0,
                "add a new item to the collection if its value exceeds the value of the largest item in this collection");
        this.commandProcessor = commandProcessor;
    }

    @Override
    public Response executeClientCommand(Request request) {
        return commandProcessor.addIfMax(request);
    }
}
