package com.example.shellservce.SSHUtils;

import com.example.shellservce.Controllers.SSHController;
import com.jcraft.jsch.Channel;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SSHUtils {

    public PipedOutputStream outputStream;
    public PipedInputStream inputStream;
    private Session session = null;
    private String  username; // your username
    private String password; // your username's password

    private String host; // your remote server address
    private int port; // your remote server port

    private SseEmitter emitter;

    public SSHUtils(String username, String password, String host, int port,SseEmitter emitter) {
        this.username = username;
        this.password = password;
        this.host = host;
        this.port = port;
        this.emitter = emitter;
    }



    public void connect() {

        try {
            session = new JSch().getSession(username, host, port);

            session.setPassword(password);

            session.setConfig("StrictHostKeyChecking", "no");

            session.connect();

            System.out.println("Session connected:" + session.isConnected());

            Channel channel = session.openChannel("shell");



            OutputStream outputStream = new OutputStream() {
                @Override
                public void write(int b) throws IOException {
                    emitter.send(SseEmitter.event().name("test").data(b));
                }
            };

            PipedOutputStream pipedOutputStream = new PipedOutputStream();
            PipedInputStream pipedInputStream = new PipedInputStream(pipedOutputStream);


            this.outputStream = pipedOutputStream;

            PipedInputStream pipedInputStream1 = new PipedInputStream();
            PipedOutputStream pipedOutputStream1 = new PipedOutputStream(pipedInputStream1);

            this.inputStream = pipedInputStream1;

            channel.setOutputStream(outputStream);
            channel.setInputStream(pipedInputStream);

            channel.connect();


        } catch (JSchException e) {
            e.printStackTrace();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    public Session getSession() {
        return session;
    }

    public SseEmitter getEmitter() {
        return emitter;
    }
}
