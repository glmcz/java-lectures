package socket.server;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLServerSocket;
import javax.net.ssl.SSLServerSocketFactory;
import javax.net.ssl.SSLSocket;
import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.security.cert.X509Certificate;
import java.sql.Time;
import java.time.Duration;
import java.time.Instant;

public class Main {
    public static void setupSocketServer() throws Exception {
        SSLUtilities sslUtilities = new SSLUtilities();
        sslUtilities.generateRSAPairKeys();
        sslUtilities.createKeyStore(sslUtilities.getKeyPair(), "./sslStore.jks", "123456".toCharArray());
        sslUtilities.setupKeyManager();
        SSLContext sslContext = sslUtilities.getpSSLContext();

        SSLServerSocketFactory ssf = sslContext.getServerSocketFactory();
        SSLServerSocket sslServerSocket = (SSLServerSocket) ssf.createServerSocket(8402);

        BufferedInputStream bis = null;
        ByteArrayOutputStream baos = null;

        while (true) {
            Socket incoming = sslServerSocket.accept();
            OutputStream out = incoming.getOutputStream();
            bis = new BufferedInputStream(incoming.getInputStream());
            baos = new ByteArrayOutputStream();
            System.out.println("Server is up and accepted connection from " + incoming.getRemoteSocketAddress());
            int res;
            Instant timeOuf =  Instant.now();
            try {
                int EOF = 0x04;
                while ((res = bis.read()) != EOF) {
                    baos.write(res);
                }
                System.out.println("Incoming data:" + baos.toString());

                String response = "hello from Synergy server";
                out.write(response.getBytes());
                out.write(0x04);
                out.flush();
            } catch (IOException e) {
                System.out.println(e.toString());
                throw new RuntimeException(e);
            } finally {
                bis.close();
                incoming.close();
                if (out != null){
                    System.out.println("Closing output stream");
                    out.close();
                }
            }
        }
    }

    public static void main(String[] args) throws IOException {
        try {
            setupSocketServer();
        }catch (Exception e){
            System.out.println(e.toString());
        }
    }
}
