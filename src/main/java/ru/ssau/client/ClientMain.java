package ru.ssau.client;

import lombok.SneakyThrows;
import ru.ssau.service.impl.Car;
import ru.ssau.service.impl.Motorcycle;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.List;

import static ru.ssau.constant.Constant.LOCALHOST;
import static ru.ssau.constant.Constant.PORT;

public class ClientMain {

    @SneakyThrows
    public static void main(String[] args) {
        try (Socket socket = new Socket(LOCALHOST, PORT);
             DataInputStream inputStream = new DataInputStream(new BufferedInputStream(socket.getInputStream()));
             ObjectOutputStream outputStream = new ObjectOutputStream(new BufferedOutputStream(socket.getOutputStream()))) {

            outputStream.writeObject(List.of(new Car("BRAND", 3), new Motorcycle("BRAND", 4)).toArray());
            outputStream.flush();
            System.out.println("Average price = " + inputStream.readDouble());
        }
    }
}
