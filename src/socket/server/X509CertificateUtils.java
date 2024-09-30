package socket.server;

import org.bouncycastle.asn1.x509.BasicConstraints;
import org.bouncycastle.asn1.x509.X509Extensions;
import org.bouncycastle.x509.X509V3CertificateGenerator;

import javax.security.auth.x500.X500Principal;
import java.io.FileOutputStream;
import java.math.BigInteger;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.KeyPair;
import java.security.cert.X509Certificate;
import java.util.Date;

public class X509CertificateUtils {
    private static final String SIG_ALG = "SHA256withRSA";

    public static X509Certificate generateSelfSignedCertificate(KeyPair keyPair) throws Exception {

        String rootDN = "CN=localhost, O=Test, L=Test city, C=US";

        BigInteger serialNum = BigInteger.valueOf(System.currentTimeMillis());

        Date notBefore = new Date(System.currentTimeMillis() - 1000L * 60 * 60 * 24);
        Date notAfter = new Date(System.currentTimeMillis() + 2000L * 60 * 60 * 24);

        X509V3CertificateGenerator generator = new X509V3CertificateGenerator();
        generator.setSerialNumber(serialNum);
        generator.setIssuerDN(new X500Principal(rootDN));
        generator.setNotBefore(notBefore);
        generator.setNotAfter(notAfter);
        generator.setSubjectDN(new X500Principal(rootDN));
        generator.setPublicKey(keyPair.getPublic());
        generator.setSignatureAlgorithm(SIG_ALG);
        generator.addExtension(X509Extensions.BasicConstraints, true, new BasicConstraints(true));
        return generator.generate(keyPair.getPrivate());
    }

    public static void X509CertificateToDER(X509Certificate certificate, Path filePath) {
        if (Files.exists(filePath)){
            System.out.println("Warning:Certificate already exists. Overwriting existing file.");
        }

        try (FileOutputStream fos = new FileOutputStream(filePath.toString()))
        {
            fos.write(certificate.getEncoded());
            fos.close();
        }catch (Exception e){
            System.out.println("Warning:Could not write Certificate to file");
        }
    }
}
