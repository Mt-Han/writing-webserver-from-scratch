package com.eddicorp;

import com.eddicorp.application.service.posts.Post;
import com.eddicorp.examples.week1.Example3OutputStream;
import com.samskivert.mustache.Mustache;
import com.samskivert.mustache.Template;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WebApplication3 {

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

            Mustache.Compiler compiler = Mustache.compiler();
            Template template = compiler.compile(new String(responseBytes2));


//            List<Posts> posts = new ArrayList<>();
//            posts.add(new Posts("title1", "autor1", "content1"));
//            posts.add(new Posts("title2", "autor2", "content2"));
//            posts.add(new Posts("title3", "autor3", "content3"));

            Map<String, Object> context = new HashMap<>();
//            context.put("posts", posts);
            context.put("isLoggedIn", "123");

            String t = template.execute(context);


            String result = new String(responseBytes) + t;

            try (
                    final InputStream inputStream1 = new ByteArrayInputStream(result.getBytes(StandardCharsets.UTF_8));
                    final OutputStream outputStream = clientSocket.getOutputStream();
            ) {
                final byte[] buffer = new byte[8192];
                int readCount;
                while ((readCount = inputStream1.read(buffer)) != -1) {
                    outputStream.write(buffer, 0, readCount);
                }
                outputStream.flush();
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
