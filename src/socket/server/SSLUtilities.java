package socket.server;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.*;
import java.security.cert.Certificate;
import java.security.cert.X509Certificate;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

public class SSLUtilities {
    private KeyPair pair = null;
    private final PublicKey userPubKey = null;
    private final PrivateKey userPrivateKey = null;
    private KeyStore keyStore = null;
    private KeyManagerFactory keyManagerFactory = null;


    public void generateRSAPairKeys() throws IOException, NoSuchAlgorithmException, InvalidKeySpecException {
        String pubPath = String.valueOf(Paths.get("./pubKey"));
        String privatePath = String.valueOf(Paths.get("./privateKey"));
        // skip saving to file
        File pubFile = new File(pubPath);
        File privateFile = new File(privatePath);
        if ((pubFile.exists()) && (privateFile.exists())) {
            this.pair = new KeyPair(
                    loadPublicKeyFromFile(pubPath),
                    loadPrivateKeyFromFile(privatePath)
            );
        }else {
            KeyPairGenerator generator = KeyPairGenerator.getInstance("RSA");
            generator.initialize(2024);

            this.pair = generator.generateKeyPair();
            byte[] encodedPub = this.pair.getPublic().getEncoded();
            try {
                FileOutputStream fos = new FileOutputStream(pubPath);
                fos.write(encodedPub);
                fos.close();
            }catch (Exception e){
                System.out.println(e.toString());
            }

            byte[] encodedPrivate = this.pair.getPrivate().getEncoded();
            try {
                FileOutputStream fos = new FileOutputStream(privatePath);
                fos.write(encodedPrivate);
                fos.close();
            }catch (Exception e){
                System.out.println(e.toString());
            }
        }
    }

    public void createKeyStore(KeyPair keyPair, String storePath, char[] passwd) throws Exception {
        keyStore = KeyStore.getInstance("JKS");
        keyStore.load(null, null);
        X509Certificate certificate = X509CertificateUtils.generateSelfSignedCertificate(this.pair);
        // save cert to file so we can use it for client
        X509CertificateUtils.X509CertificateToDER(certificate, Paths.get("./serverCert.der"));
        try {
            FileOutputStream fos = new FileOutputStream(storePath);
            keyStore.setKeyEntry("synergy", keyPair.getPrivate(), passwd, new Certificate[]{certificate});
            keyStore.store(fos, passwd);
            fos.close();

        }catch (Exception e) {
            System.out.println(e.toString());
        }
    }

    public void setupKeyManager() throws NoSuchAlgorithmException, UnrecoverableKeyException, KeyStoreException {
        keyManagerFactory = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
        keyManagerFactory.init(keyStore, "123456".toCharArray());
    }

    public SSLContext getpSSLContext() throws NoSuchAlgorithmException, KeyManagementException {
        SSLContext sslContext = SSLContext.getInstance("TLS");
        sslContext.init(keyManagerFactory.getKeyManagers(), null, new SecureRandom());
        return sslContext;
    }


    public PublicKey loadPublicKeyFromFile(String filePath) throws IOException, NoSuchAlgorithmException, InvalidKeySpecException {
        byte[] byteKey = Files.readAllBytes(Paths.get(filePath));
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(byteKey);

        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        return keyFactory.generatePublic(keySpec);
    }

    public PrivateKey loadPrivateKeyFromFile(String filePath) throws IOException, NoSuchAlgorithmException, InvalidKeySpecException {
        byte[] keyBytes = Files.readAllBytes(Paths.get(filePath));
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(keyBytes);

        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        return keyFactory.generatePrivate(keySpec);
    }

    public KeyPair getKeyPair(){
        return this.pair;
    }

    public PublicKey getUserPublicKey(){
        return this.userPubKey;
    }

    public PrivateKey getUserPrivateKey(){
        return this.userPrivateKey;
    }
}
