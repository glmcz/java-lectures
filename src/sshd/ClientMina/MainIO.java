package sshd.ClientMina;

import org.apache.sshd.client.SshClient;
import org.apache.sshd.client.channel.ChannelSubsystem;
import org.apache.sshd.client.session.ClientSession;
import org.apache.sshd.common.io.nio2.Nio2ServiceFactoryFactory;
import org.apache.sshd.common.keyprovider.FileKeyPairProvider;
import org.apache.sshd.common.session.SessionHeartbeatController;

import java.io.*;
import java.nio.file.Paths;
import java.security.cert.CertificateException;
import java.time.Duration;

public class MainIO {

    public static void main(String[] args) throws IOException, CertificateException {
        MinaClient client = new MinaClient();
        client.startClient();
        ClientSession session = client.getSession();
        if (session.isAuthenticated()) {
            ChannelSubsystem exec = session.createSubsystemChannel("Test subsystem");

            exec.open().verify(3000); // Open new channel!
            OutputStream out = exec.getInvertedIn() ;
            InputStream input = exec.getInvertedOut();
            out.write("Hello server -1".getBytes());
            out.flush();
            out.close();
            Integer var = input.available();
            if (input.available() == 0) {
                BufferedInputStream bis = new BufferedInputStream(input);
                ByteArrayOutputStream buf = new ByteArrayOutputStream();
                for (int res = bis.read(); res != -1; res = bis.read()) {
                    buf.write((byte) res);
                }

                System.out.println("Sever response " + buf.toString());
            }
            input.close();
            exec.close();
        }
        client.stopClient();
    }
}
