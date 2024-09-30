package sshd.ClientMina;

import org.apache.sshd.client.channel.ChannelSubsystem;
import org.apache.sshd.client.session.ClientSession;
import org.apache.sshd.common.channel.StreamingChannel;

import java.io.*;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.security.cert.CertificateException;
import java.time.Duration;
import java.time.Instant;

public class SynergySocketClient {
    private Socket socket;
    private Boolean synchronous;
    private Boolean debug = false;
    // TODO: make configurable
    private Duration openTimeout = Duration.ofSeconds(30);
    private ChannelSubsystem channelSubsystem;


    public static void main(String[] args) throws IOException, CertificateException, InterruptedException {
        MinaClient client = new MinaClient();
        client.startClient();
        ClientSession session = client.getSession();
        if (session.isAuthenticated() && session.isOpen()) {
            ChannelSubsystem exec = session.createSubsystemChannel("synergy");
            exec.open().verify(3000); // Open new channel!
            OutputStream out = exec.getInvertedIn() ;
            InputStream input = exec.getInvertedOut();
            ByteArrayOutputStream errorStream = new ByteArrayOutputStream();
            exec.setErr(errorStream);

            out.write("Hello server".getBytes());
            out.write(0x04);
            out.flush();
            out.close();
//            Instant timeOut = Instant.now();
//            timeOut.wait(1200);

                BufferedInputStream bis = new BufferedInputStream(input);
                ByteArrayOutputStream buf = new ByteArrayOutputStream();
                int res;
                int EOF = 0x04;
                while ((res = bis.read()) != EOF) {
                    buf.write((byte) res);
                }

                // No need to response we already get data
//                buf.write(0x04);
                System.out.println("Sever response: " + buf.toString());
                buf.close();

            exec.close();
        }
        client.stopClient();
    }
}
