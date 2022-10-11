package com.example.shellservce.SSHUtils;

import com.example.shellservce.Controllers.SSHController;
import com.example.shellservce.Entities.Method;
import com.jcraft.jsch.*;
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

    private String keyLocation;
    private String host; // your remote server address
    private int port; // your remote server port

    private SseEmitter emitter;

    private Method method;



    public void connect() {

        try {
            JSch jSch = new JSch();
            if(method.equals(Method.key)){
                jSch.addIdentity(keyLocation);
            }

            session = jSch.getSession(username, host, port);

            if(method.equals(Method.password)){
                session.setPassword(password);
                session.setConfig("StrictHostKeyChecking", "no");
            }

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
