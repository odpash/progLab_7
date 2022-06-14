package olegpash.lab7.server.servercommands;

import olegpash.lab7.common.util.TextColoring;
import olegpash.lab7.server.ServerConfig;
import olegpash.lab7.server.abstractions.AbstractServerCommand;

public class ServerExitCommand extends AbstractServerCommand {

    public ServerExitCommand() {
        super("exit", "shut downs the server");
    }

    @Override
    public String executeServerCommand() {
        ServerConfig.toggleRun();
        return TextColoring.getGreenText("Server shutdown");
    }
}
