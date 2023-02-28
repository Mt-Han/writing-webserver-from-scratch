package com.eddicorp;

import com.eddicorp.examples.week1.Example3OutputStream;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class WebApplication2 {

    public static void main(String[] args) throws IOException, URISyntaxException {


        final ServerSocket serverSocket = new ServerSocket(8080);
        System.out.println("클라이언트를 기다릴거에요.");
        Socket clientSocket ;

        while ((clientSocket = serverSocket.accept()) != null) {

            Example3OutputStream.Output output = new Example3OutputStream.Output();
            FileInputStream fis = new FileInputStream(output.getResource("pages/index.html").getFile());
            final byte[] responseBytes2 = fis.readAllBytes();

            final String response =
                    "HTTP/1.1 200 OK\r\n" +
                            "Content-Type: text/html; charset=UTF-8\r\n" +
                            "Content-Length: " + responseBytes2.length +
                            "\r\n" +
                            "\r\n";

            final byte[] responseBytes = response.getBytes(StandardCharsets.UTF_8);

            String result = new String(responseBytes) + new String(responseBytes2);

            try (
                    final InputStream inputStream1 = new ByteArrayInputStream(result.getBytes(StandardCharsets.UTF_8));
                    final InputStream inputStream2 = fis;
                    final OutputStream outputStream = clientSocket.getOutputStream();
//                    final OutputStream outputStream = new FileOutputStream(fileToExport);
            ) {
                final byte[] buffer = new byte[8192];
                int readCount;
                while ((readCount = inputStream1.read(buffer)) != -1) {
                    outputStream.write(buffer, 0, readCount);
                }
//                ObjectOutputStream oos = new ObjectOutputStream(outputStream);
                final byte[] buffer2 = new byte[8192];
                int readCount2;
//                while ((readCount2 = inputStream2.read(buffer2)) != -1) {

//                    outputStream.write(responseBytes, 0, responseBytes.length);
//                    outputStream.write(responseBytes2, 0, responseBytes2.length);
//                }
//                outputStream.flush();
//                outputStream.write(responseBytes);
//                outputStream.write(responseBytes2);
//                outputStream.write(responseBytes);
//                Writer writer = new OutputStreamWriter(outputStream, "UTF-8");
//                writer = new BufferedWriter(writer);
//                System.out.println(responseBytes.length + responseBytes2.length);
//                writer.write(new String(responseBytes));
//                writer.write(new String(responseBytes));

//                while ((readCount=inputStream2.read(buffer)) != -1) {
//                    writer.write(new String(buffer), 0, readCount);
//                }

//                String index = new String(responseBytes2);
//                writer.write(index);
//                oos.writeObject(response + "Hi");
//                System.out.println(response + "Hi");
//                oos.writeObject("Hi client");
//                oos.flush();
                outputStream.flush();
//                inputStream1.close();
//                inputStream2.close();
//                oos.close();
                outputStream.close();
//                System.out.println(index);
//                while ((readCount=inputStream2.read(buffer)) != -1) {
//                    outputStream.write(buffer, 0, readCount);
//                }
//                writer.flush();
            } catch (FileNotFoundException e) {
                throw new RuntimeException(e);
            } catch (IOException e) {
                throw new RuntimeException(e);
            } finally {
                if(clientSocket != null) {
                    clientSocket.close();
                }
            }
        }

    }

    public static class Output {
        public URL getResource(String path) {
            return this.getClass().getClassLoader().getResource(path);
        }
    }
}
