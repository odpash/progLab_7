package olegpash.lab7.server;

import olegpash.lab7.common.util.RequestType;
import olegpash.lab7.common.util.TextColoring;
import olegpash.lab7.server.db.DBSSHConnector;
import olegpash.lab7.server.interfaces.SocketWorkerInterface;
import olegpash.lab7.server.util.CommandManager;
import olegpash.lab7.server.util.RequestWithAddress;
import olegpash.lab7.server.util.UsersManager;

import java.io.IOException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;


public class RequestThread implements Runnable {

    private final SocketWorkerInterface serverSocketWorker;
    private final CommandManager commandManager;
    private final UsersManager usersManager;
    private final ExecutorService fixedService = Executors.newFixedThreadPool(5);
    private final ExecutorService cachedService = Executors.newCachedThreadPool();
    private final ExecutorService cachedService2 = Executors.newCachedThreadPool();

    public RequestThread(SocketWorkerInterface serverSocketWorker, CommandManager commandManager, UsersManager usersManager) {
        this.serverSocketWorker = serverSocketWorker;
        this.commandManager = commandManager;
        this.usersManager = usersManager;
    }

    @Override
    public void run() {
        while (ServerConfig.getRunning()) {
            try {
                Future<RequestWithAddress> listenFuture = fixedService.submit(serverSocketWorker::listenForRequest);
                RequestWithAddress acceptedRequest = listenFuture.get();
                if (acceptedRequest != null) {
                    CompletableFuture
                            .supplyAsync(acceptedRequest::getRequest)
                            .thenApplyAsync(request -> {
                                if (request.getRequestType().equals(RequestType.COMMAND)) {
                                    return commandManager.executeClientCommand(request);
                                } else if (request.getRequestType().equals(RequestType.REGISTER)) {
                                    return usersManager.registerNewUser(request);
                                } else {
                                    return usersManager.loginUser(request);
                                }
                            }, cachedService)
                            .thenAcceptAsync(response -> {
                                try {
                                    serverSocketWorker.sendResponse(response, acceptedRequest.getSocketAddress());
                                } catch (IOException e) {
                                    ServerConfig.getConsoleTextPrinter().printlnText(TextColoring.getRedText(e.getMessage()));
                                }
                            }, cachedService2);
                }
            } catch (ExecutionException e) {
                ServerConfig.getConsoleTextPrinter().printlnText(TextColoring.getRedText(e.getMessage()));
            } catch (InterruptedException e) {
                ServerConfig.getConsoleTextPrinter().printlnText(TextColoring.getRedText("An error occurred while deserializing the request, try again"));
            }
        }
        try {
            serverSocketWorker.stopSocketWorker();
            DBSSHConnector.closeSSH();
            fixedService.shutdown();
            cachedService.shutdown();
            cachedService2.shutdown();
        } catch (IOException e) {
            ServerConfig.getConsoleTextPrinter().printlnText(TextColoring.getRedText("An error occurred during stopping the server"));
        }
    }
}
