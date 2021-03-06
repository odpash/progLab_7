package olegpash.lab7.server.clientcommands;

import olegpash.lab7.common.util.Request;
import olegpash.lab7.common.util.Response;
import olegpash.lab7.server.abstractions.AbstractClientCommand;
import olegpash.lab7.server.util.CommandProcessor;

public class RemoveAnyByNumberOfParticipantsCommand extends AbstractClientCommand {

    private final CommandProcessor commandProcessor;

    public RemoveAnyByNumberOfParticipantsCommand(CommandProcessor commandProcessor) {
        super("remove_any_by_number_of_participants",
                1,
                "delete a group with a specified number of members from the collection",
                "number of participants");
        this.commandProcessor = commandProcessor;
    }

    @Override
    public Response executeClientCommand(Request request) {
        return commandProcessor.removeAnyByNumberOfParticipants(request);
    }
}
