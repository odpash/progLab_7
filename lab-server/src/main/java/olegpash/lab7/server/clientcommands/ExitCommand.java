package olegpash.lab7.server.clientcommands;

import olegpash.lab7.common.util.Request;
import olegpash.lab7.common.util.Response;
import olegpash.lab7.server.abstractions.AbstractClientCommand;

public class ExitCommand extends AbstractClientCommand {

    public ExitCommand() {
        super("exit",
                0,
                "shut down the client (all your changes will be lost)");
    }

    @Override
    public Response executeClientCommand(Request request) {
        return null;
    }
}
