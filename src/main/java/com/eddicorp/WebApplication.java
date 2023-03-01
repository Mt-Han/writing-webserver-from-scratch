package com.eddicorp;

import com.eddicorp.server.HandleClientRequestTask;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class WebApplication {

    public static void main(String[] args) throws IOException {
        final ThreadPoolExecutor executor = new ThreadPoolExecutor(
                8,
                16,
                10000,
                TimeUnit.MINUTES,
                new ArrayBlockingQueue<>(500)
        );

        final ServerSocket serverSocket = new ServerSocket(8080);
        Socket clientSocket;
        System.out.println("start");
        while ((clientSocket = serverSocket.accept()) != null) {
            final HandleClientRequestTask task = new HandleClientRequestTask(clientSocket);
            executor.execute(task);
        }
    }
}
