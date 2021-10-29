package ru.ssau.server;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import ru.ssau.service.Vehicle;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataOutputStream;
import java.io.ObjectInputStream;
import java.net.Socket;
import java.util.stream.Stream;

@RequiredArgsConstructor
public class RequestHandler extends Thread {

    private final Socket socket;

    @SneakyThrows
    @Override
    public void run() {
        try (ObjectInputStream inputStream = new ObjectInputStream(new BufferedInputStream(socket.getInputStream()));
             DataOutputStream outputStream = new DataOutputStream(new BufferedOutputStream(socket.getOutputStream()))) {
            Thread.sleep(1000L);

            double averagePrice = Stream.of((Object[]) inputStream.readObject())
                    .map(o -> (Vehicle) o)
                    .peek(System.out::println)
                    .map(Vehicle::getModelPrices)
                    .flatMap(Stream::of)
                    .mapToDouble(i -> i)
                    .average()
                    .orElse(Double.NaN);
            outputStream.writeDouble(averagePrice);
            outputStream.flush();
        }
    }
}
