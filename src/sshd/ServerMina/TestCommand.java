package sshd.ServerMina;

import org.apache.sshd.server.Environment;
import org.apache.sshd.server.ExitCallback;
import org.apache.sshd.server.channel.ChannelSession;
import org.apache.sshd.server.command.Command;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

public class TestCommand implements Command {
    private final String command = "";
    private InputStream in;
    private OutputStream out;
    private ExitCallback exitCallback;

    public TestCommand(){

    }

    @Override
    public void setExitCallback(ExitCallback exitCallback) {
        System.out.println("channel exit callback: " + exitCallback.toString());
        this.exitCallback = exitCallback;
    }

    @Override
    public void setErrorStream(OutputStream outputStream) {
        try {
            outputStream.write("err".getBytes(StandardCharsets.UTF_8));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void setInputStream(InputStream inputStream) {
        System.out.println("channel input stream: " + inputStream);
        this.in = inputStream;
    }

    @Override
    public void setOutputStream(OutputStream outputStream) {
        System.out.println("channel output stream: " + outputStream);
        this.out = outputStream;
    }

    @Override
    public void start(ChannelSession channelSession, Environment environment) throws IOException {
        new Thread(() -> {
        System.out.println("Command started...");
        BufferedInputStream bis = new BufferedInputStream(this.in);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();

            try {
                for(int res = bis.read(); res != -1; res = bis.read()) {
                    System.out.print((char)res);
                    baos.write((byte)res);
                }
                System.out.println("bis read ended");
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            // Optionally print the full output after the loop
        System.out.println("Full command input: " + baos);
            String response = "aaa"+ baos.toString(StandardCharsets.UTF_8);
            try {
                out.write(response.getBytes());
                out.flush();
                out.close();
                exitCallback.onExit(0);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }).start();
    }
//        new Thread(() -> {
//            BufferedInputStream bis = new BufferedInputStream(this.in);
//            ByteArrayOutputStream buf = new ByteArrayOutputStream();
//            try {
//                for(int result = bis.read(); result != -1; result = bis.read()){
//                    buf.write((byte)result);
//                    this.out.write((byte)result);
//                }
//            //send whole incoming data back once all of it come
//            this.out.flush();
//            } catch (IOException e) {
//                throw new RuntimeException(e);
//            }
//
//            String packet = buf.toString(StandardCharsets.UTF_8);
//            System.out.println("From client come: " + packet);
//        });

    @Override
    public void destroy(ChannelSession channelSession) throws IOException {
        channelSession.close();
    }
}
