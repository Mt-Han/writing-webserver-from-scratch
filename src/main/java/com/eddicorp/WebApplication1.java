package com.eddicorp;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

public class WebApplication1 {

    public static void main(String[] args) throws IOException, URISyntaxException {

        final ServerSocket serverSocket = new ServerSocket(8080);
        System.out.println("클라이언트를 기다릴거에요.");
        Socket clientSocket;

        while ((clientSocket = serverSocket.accept()) != null) {

            Output output = new Output();

            URL resource = output.getResource("pages/index.html");

            File file = new File(resource.toURI());

            System.out.println(file.toString());

            try (
                    final InputStream inputStream = new FileInputStream(file);
                    final OutputStream outputStream = clientSocket.getOutputStream();
            ) {
                final byte[] buffer = new byte[8192];
                int readCount;
                while ((readCount = inputStream.read(buffer)) != -1) {
                    System.out.println(readCount);
                    outputStream.write(buffer, 0, readCount);
                }
                outputStream.flush();
                clientSocket.close();
            } catch (FileNotFoundException e) {
                throw new RuntimeException(e);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public static class Output {

        public URL getResource(String path) {
            return this.getClass().getClassLoader().getResource(path);
        }
    }
}
