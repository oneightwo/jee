package ru.ssau.server;

import lombok.SneakyThrows;
import ru.ssau.service.Vehicle;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataOutputStream;
import java.io.ObjectInputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.stream.Stream;

import static ru.ssau.constant.Constant.PORT;

public class ServerMain {

    @SneakyThrows
    public static void main(String[] args) {
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            Socket socket = serverSocket.accept();
            try (ObjectInputStream inputStream = new ObjectInputStream(new BufferedInputStream(socket.getInputStream()));
                 DataOutputStream outputStream = new DataOutputStream(new BufferedOutputStream(socket.getOutputStream()))) {

                double averagePrice = Stream.of((Object[]) inputStream.readObject())
                        .map(o -> (Vehicle) o)
                        .peek(System.out::println)
                        .map(Vehicle::getModelPrices)
                        .flatMap(Stream::of)
                        .mapToDouble(i -> i)
                        .average()
                        .orElse(Double.NaN);
                System.out.println("averagePrice: " + averagePrice);
                outputStream.writeDouble(averagePrice);
                outputStream.flush();
            }
        }
    }
}
