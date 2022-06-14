package olegpash.lab7.server;

import olegpash.lab7.common.util.Request;
import olegpash.lab7.common.util.RequestType;
import olegpash.lab7.common.util.Response;
import olegpash.lab7.server.util.CommandManager;
import olegpash.lab7.server.util.UsersManager;

import java.util.concurrent.Callable;

public class ExecutionTask implements Callable<Response> {

    private final Request request;
    private final CommandManager commandManager;
    private final UsersManager usersManager;

    public ExecutionTask(Request request, CommandManager commandManager, UsersManager usersManager) {
        this.request = request;
        this.commandManager = commandManager;
        this.usersManager = usersManager;
    }

    public Response call() {
        if (request.getRequestType().equals(RequestType.COMMAND)) {
            return commandManager.executeClientCommand(request);
        } else if (request.getRequestType().equals(RequestType.REGISTER)) {
            return usersManager.registerNewUser(request);
        } else {
            return usersManager.loginUser(request);
        }
    }
}
