package app.utils;

import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.security.*;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;

public class CertificateUtils {
    public static Certificate readCertificate(String keyStoreFile, String alias, String password) throws IOException, KeyStoreException, CertificateException, NoSuchAlgorithmException {
        FileInputStream is = new FileInputStream(keyStoreFile);
        KeyStore keystore = KeyStore.getInstance(KeyStore.getDefaultType());
        keystore.load(is, password.toCharArray());
        return keystore.getCertificate(alias);
    }

    public static PublicKey readPublicKey(String keyStoreFile, String alias, String password) throws IOException, KeyStoreException, CertificateException, NoSuchAlgorithmException, UnrecoverableKeyException {
        FileInputStream is = new FileInputStream(keyStoreFile);
        KeyStore keystore = KeyStore.getInstance(KeyStore.getDefaultType());
        keystore.load(is, password.toCharArray());
        Certificate cert = keystore.getCertificate(alias);

        return cert.getPublicKey();
    }

    public static boolean verifySignature(byte[] signBytes, byte[] message, PublicKey publicKey, Signature signature) throws InvalidKeyException, SignatureException {
        signature.initVerify(publicKey);
        signature.update(message);
        return signature.verify(signBytes);
    }

    public static Certificate getCertificateFromBytes(byte[] bytes) throws IOException, CertificateException {
        try (ByteArrayInputStream bis = new ByteArrayInputStream(bytes)) {
            CertificateFactory certificateFactory = CertificateFactory.getInstance("X.509");
            return certificateFactory.generateCertificate(bis);
        }
    }

}