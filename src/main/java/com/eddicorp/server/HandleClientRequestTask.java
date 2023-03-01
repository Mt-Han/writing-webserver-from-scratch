package com.eddicorp.server;

import com.eddicorp.http.request.HttpRequest;
import com.eddicorp.http.response.HttpResponse;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

public class HandleClientRequestTask implements Runnable {

    private final Socket clientSocket;
    private final RequestHandler requestHandler = new RequestHandler();

    public HandleClientRequestTask(Socket clientSocket) {
        this.clientSocket = clientSocket;
    }

    @Override
    public void run() {
        try (
                final InputStream inputStream = clientSocket.getInputStream();
                final OutputStream outputStream = clientSocket.getOutputStream();
        ) {
            requestHandler.handle(inputStream, outputStream);
        } catch (Throwable throwable) {
            throwable.printStackTrace();
            System.out.println("error" + throwable);
            System.err.println(throwable);
        }
    }
}
