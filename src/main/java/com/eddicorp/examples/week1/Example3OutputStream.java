package com.eddicorp.examples.week1;

import java.io.*;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class Example3OutputStream {

    public static void main(String[] args) throws IOException {
        final String response =
                "HTTP/1.1 200 OK\r\n" +
                        "Content-Type: text/html; charset=UTF-8\r\n" +
                        "Content-Length: 155\r\n" +
                        "\r\n" +
                        "<html>\r\n" +
                        "  <head>\r\n" +
                        "    <title>An Example Page</title>\r\n" +
                        "  </head>\r\n" +
                        "  <body>\r\n" +
                        "    <p>Hello World, this is a very simple HTML document.</p>\r\n" +
                        "  </body>\r\n" +
                        "</html>";
        final byte[] responseBytes = response.getBytes(StandardCharsets.UTF_8);
        final File fileToExport = new File("./response.txt");
        try (
                final InputStream inputStream = new ByteArrayInputStream(responseBytes);
                final OutputStream outputStream = new FileOutputStream(fileToExport)
        ) {
            final byte[] buffer = new byte[8192];
            int readCount;
            while ((readCount = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, readCount);
            }
            outputStream.flush();
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    public static class Output {
        public URL getResource(String path) {
            return this.getClass().getClassLoader().getResource(path);
        }
    }
}
