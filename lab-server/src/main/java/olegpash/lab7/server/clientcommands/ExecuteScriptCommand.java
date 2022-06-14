package olegpash.lab7.server.clientcommands;

import olegpash.lab7.common.util.Request;
import olegpash.lab7.common.util.Response;
import olegpash.lab7.server.abstractions.AbstractClientCommand;

public class ExecuteScriptCommand extends AbstractClientCommand {

    public ExecuteScriptCommand() {
        super("execute_script",
                1,
                "read and execute the script from the specified file",
                "file name");
    }

    @Override
    public Response executeClientCommand(Request request) {
        return null;
    }
}
