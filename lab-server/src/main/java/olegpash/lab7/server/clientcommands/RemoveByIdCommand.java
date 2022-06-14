package olegpash.lab7.server.clientcommands;

import olegpash.lab7.common.util.Request;
import olegpash.lab7.common.util.Response;
import olegpash.lab7.server.abstractions.AbstractClientCommand;
import olegpash.lab7.server.util.CommandProcessor;

public class RemoveByIdCommand extends AbstractClientCommand {

    private final CommandProcessor commandProcessor;

    public RemoveByIdCommand(CommandProcessor commandProcessor) {
        super("remove_by_id",
                1,
                "delete a group from a collection by its id",
                "id");
        this.commandProcessor = commandProcessor;
    }

    @Override
    public Response executeClientCommand(Request request) {
        return commandProcessor.removeById(request);
    }
}
