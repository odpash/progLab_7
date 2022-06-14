package olegpash.lab7.server.interfaces;

import olegpash.lab7.common.util.Response;
import olegpash.lab7.server.util.RequestWithAddress;

import java.io.IOException;
import java.net.SocketAddress;

public interface SocketWorkerInterface {
    RequestWithAddress listenForRequest() throws IOException, ClassNotFoundException;
    void sendResponse(Response response, SocketAddress address) throws IOException;
    void stopSocketWorker() throws IOException;
}
