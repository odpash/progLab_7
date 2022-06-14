package olegpash.lab7.server.clientcommands;

import olegpash.lab7.common.util.Request;
import olegpash.lab7.common.util.Response;
import olegpash.lab7.server.abstractions.AbstractClientCommand;
import olegpash.lab7.server.util.CommandProcessor;

public class RemoveGreaterCommand extends AbstractClientCommand {

    private final CommandProcessor commandProcessor;

    public RemoveGreaterCommand(CommandProcessor commandProcessor) {
        super("remove_greater",
                0,
                "remove all items from the collection that exceed the specified");
        this.commandProcessor = commandProcessor;
    }

    @Override
    public Response executeClientCommand(Request request) {
        return commandProcessor.removeGreater(request);
    }
}
