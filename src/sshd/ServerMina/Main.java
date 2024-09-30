package sshd.ServerMina;

import java.io.File;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.security.Security;
import java.time.Duration;

import org.apache.sshd.common.keyprovider.KeyPairProvider;
import org.apache.sshd.server.SshServer;
import org.apache.sshd.common.io.nio2.Nio2ServiceFactoryFactory;
import org.apache.sshd.common.session.SessionHeartbeatController;
import org.apache.sshd.server.auth.AsyncAuthException;
import org.apache.sshd.server.auth.password.PasswordAuthenticator;
import org.apache.sshd.server.auth.password.PasswordChangeRequiredException;
import org.apache.sshd.server.keyprovider.SimpleGeneratorHostKeyProvider;
import org.apache.sshd.server.session.ServerSession;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

public class Main {

    public static void main(String[] args) throws IOException, GeneralSecurityException {
        Security.addProvider(new BouncyCastleProvider());
        SshServer server = SshServer.setUpDefaultServer();
        server.setIoServiceFactoryFactory(new Nio2ServiceFactoryFactory());
        server.setPort(2224);
        server.setHost("localhost");
        server.setSessionHeartbeat(SessionHeartbeatController.HeartbeatType.RESERVED, Duration.ofMinutes(3));
        server.setCommandFactory(new TestCommandFactory());
        KeyPairProvider hostKeyProvider = new SimpleGeneratorHostKeyProvider(new File("/Users/martindurak/.ssh/hostKey").toPath());
        server.setKeyPairProvider(hostKeyProvider);
        //hostKeyProvider.loadKeys(null);
        server.setPasswordAuthenticator( new PasswordAuthenticator() {
            @Override
            public boolean authenticate(String s, String s1, ServerSession serverSession) throws PasswordChangeRequiredException, AsyncAuthException {
                return true;
            }
        });

        new Thread(() -> {
            try {
                server.start();
            }catch (Exception e) {
                e.printStackTrace();
            }

        }).start();

        while (server.isOpen()) {
          //System.out.println("server is running");
        }
    }
}
