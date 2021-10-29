package ru.ssau.server;

import lombok.SneakyThrows;

import java.net.ServerSocket;
import java.net.Socket;

import static ru.ssau.constant.Constant.PORT;

public class ParallelServer {

    @SneakyThrows
    public static void main(String[] args) {
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            while (true) {
                Socket socket = serverSocket.accept();
                RequestHandler requestHandler = new RequestHandler(socket);
                requestHandler.start();
            }
        }
    }
}
