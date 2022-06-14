package olegpash.lab7.server.util;

import olegpash.lab7.common.util.Request;
import olegpash.lab7.common.util.Response;
import olegpash.lab7.common.util.TextColoring;
import olegpash.lab7.server.ServerConfig;
import olegpash.lab7.server.abstractions.AbstractClientCommand;
import olegpash.lab7.server.abstractions.AbstractServerCommand;
import olegpash.lab7.server.clientcommands.AddCommand;
import olegpash.lab7.server.clientcommands.AddIfMaxCommand;
import olegpash.lab7.server.clientcommands.ClearCommand;
import olegpash.lab7.server.clientcommands.CountLessThatNumberOfParticipantsCommand;
import olegpash.lab7.server.clientcommands.ExecuteScriptCommand;
import olegpash.lab7.server.clientcommands.ExitCommand;
import olegpash.lab7.server.clientcommands.HelpCommand;
import olegpash.lab7.server.clientcommands.HistoryCommand;
import olegpash.lab7.server.clientcommands.InfoCommand;
import olegpash.lab7.server.clientcommands.MinByStudioCommand;
import olegpash.lab7.server.clientcommands.RemoveAnyByNumberOfParticipantsCommand;
import olegpash.lab7.server.clientcommands.RemoveByIdCommand;
import olegpash.lab7.server.clientcommands.RemoveGreaterCommand;
import olegpash.lab7.server.clientcommands.ShowCommand;
import olegpash.lab7.server.clientcommands.UpdateCommand;
import olegpash.lab7.server.servercommands.ServerExitCommand;
import olegpash.lab7.server.servercommands.ServerHelpCommand;
import olegpash.lab7.server.servercommands.ServerHistoryCommand;

import java.time.format.DateTimeFormatter;

public class CommandManager {

    private final CommandProcessor commandProcessor;

    public CommandManager(CommandProcessor commandProcessor) {
        this.commandProcessor = commandProcessor;
        setCommands();
    }

    private void setCommands() {
        AbstractClientCommand helpClientCommand = new HelpCommand(ServerConfig.getClientAvailableCommands(), commandProcessor);
        AbstractClientCommand infoCommand = new InfoCommand(commandProcessor);
        AbstractClientCommand showCommand = new ShowCommand(commandProcessor);
        AbstractClientCommand addCommand = new AddCommand(commandProcessor);
        AbstractClientCommand updateCommand = new UpdateCommand(commandProcessor);
        AbstractClientCommand removeByIdCommand = new RemoveByIdCommand(commandProcessor);
        AbstractClientCommand clearCommand = new ClearCommand(commandProcessor);
        AbstractClientCommand exitClientCommand = new ExitCommand();
        AbstractClientCommand addIfMaxCommand = new AddIfMaxCommand(commandProcessor);
        AbstractClientCommand removeGreaterCommand = new RemoveGreaterCommand(commandProcessor);
        AbstractClientCommand historyCommand = new HistoryCommand(ServerConfig.getClientCommandHistory().getHistory(), commandProcessor);
        AbstractClientCommand removeAnyByNumberOfParticipantsCommand = new RemoveAnyByNumberOfParticipantsCommand(commandProcessor);
        AbstractClientCommand minByStudioCommand = new MinByStudioCommand(commandProcessor);
        AbstractClientCommand countLessThanNumberOfParticipantsCommand = new CountLessThatNumberOfParticipantsCommand(commandProcessor);
        AbstractClientCommand executeScriptCommand = new ExecuteScriptCommand();
        AbstractServerCommand helpServerCommand = new ServerHelpCommand(ServerConfig.getServerAvailableCommands());
        AbstractServerCommand exitServerCommand = new ServerExitCommand();
        AbstractServerCommand historyServerCommand = new ServerHistoryCommand(ServerConfig.getClientCommandHistory().getHistory());

        ServerConfig.getClientAvailableCommands().put(helpClientCommand.getName(), helpClientCommand);
        ServerConfig.getClientAvailableCommands().put(infoCommand.getName(), infoCommand);
        ServerConfig.getClientAvailableCommands().put(showCommand.getName(), showCommand);
        ServerConfig.getClientAvailableCommands().put(addCommand.getName(), addCommand);
        ServerConfig.getClientAvailableCommands().put(updateCommand.getName(), updateCommand);
        ServerConfig.getClientAvailableCommands().put(removeByIdCommand.getName(), removeByIdCommand);
        ServerConfig.getClientAvailableCommands().put(clearCommand.getName(), clearCommand);
        ServerConfig.getClientAvailableCommands().put(exitClientCommand.getName(), exitClientCommand);
        ServerConfig.getClientAvailableCommands().put(addIfMaxCommand.getName(), addIfMaxCommand);
        ServerConfig.getClientAvailableCommands().put(removeGreaterCommand.getName(), removeGreaterCommand);
        ServerConfig.getClientAvailableCommands().put(historyCommand.getName(), historyCommand);
        ServerConfig.getClientAvailableCommands().put(removeAnyByNumberOfParticipantsCommand.getName(), removeAnyByNumberOfParticipantsCommand);
        ServerConfig.getClientAvailableCommands().put(minByStudioCommand.getName(), minByStudioCommand);
        ServerConfig.getClientAvailableCommands().put(countLessThanNumberOfParticipantsCommand.getName(), countLessThanNumberOfParticipantsCommand);
        ServerConfig.getClientAvailableCommands().put(executeScriptCommand.getName(), executeScriptCommand);

        ServerConfig.getServerAvailableCommands().put(helpServerCommand.getName(), helpServerCommand);
        ServerConfig.getServerAvailableCommands().put(exitServerCommand.getName(), exitServerCommand);
        ServerConfig.getServerAvailableCommands().put(historyServerCommand.getName(), historyServerCommand);
    }

    public Response executeClientCommand(Request request) {
        ServerConfig.getClientCommandHistory().pushCommand(TextColoring.getBlueText(request.getCurrentTime().format(DateTimeFormatter.ofPattern("HH:mm:ss")))
                + " " + TextColoring.getGreenText(request.getClientInfo()) + ": " + request.getCommandName());
        return ServerConfig.getClientAvailableCommands().get(request.getCommandName()).executeClientCommand(request);
    }

    public String executeServerCommand(String commandName) {
        if (ServerConfig.getServerAvailableCommands().containsKey(commandName)) {
            return ServerConfig.getServerAvailableCommands().get(commandName).executeServerCommand();
        } else {
            return TextColoring.getRedText("There is no such command, type HELP to get list on commands");
        }
    }
}
