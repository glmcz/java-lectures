package sshd.ClientMina;

import org.apache.sshd.client.SshClient;
import org.apache.sshd.client.keyverifier.RequiredServerKeyVerifier;
import org.apache.sshd.client.session.ClientSession;
import org.apache.sshd.common.io.nio2.Nio2ServiceFactoryFactory;
import org.apache.sshd.common.keyprovider.FileKeyPairProvider;
import org.apache.sshd.common.session.SessionHeartbeatController;
import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.time.Duration;

import de.vw.tapsss.ssh.CryptoUtilities;

public class MinaClient {
    private SshClient client = null;
    private ClientSession session = null;

    public MinaClient() throws CertificateException, IOException {
        client = SshClient.setUpDefaultClient();
        client.setIoServiceFactoryFactory(new Nio2ServiceFactoryFactory());
        client.getHostBasedAuthenticationReporter();
        // Load host key from file
        FileKeyPairProvider keyPairProvider = new FileKeyPairProvider(Paths.get("/Users/martindurak/.ssh/hostKey"));
        client.setKeyIdentityProvider(keyPairProvider);

        X509Certificate serverCertificate = CryptoUtilities.getX509Certificate(loadServerCertificate());
        RequiredServerKeyVerifier requiredServerKeyVerifier = new RequiredServerKeyVerifier(serverCertificate.getPublicKey());
        client.setServerKeyVerifier(requiredServerKeyVerifier);
    }

    private static File loadServerCertificate() {
        String path = "/Users/martindurak/vw/lectures/src/sshd/ClientMina/serverCertificate.cer";
        return new File(path);
    }

    public void startClient() throws IOException {
        client.start();
        session = client.connect("glmcz", "localhost", 2244).verify(3000).getSession();
        session.addPasswordIdentity("123456");
        session.setSessionHeartbeat(SessionHeartbeatController.HeartbeatType.RESERVED, Duration.ofMinutes(20));
        session.auth().verify(3000);
    }

    public ClientSession getSession() {
        return session;
    }

    public void stopClient() {
        client.stop();
    }

}
