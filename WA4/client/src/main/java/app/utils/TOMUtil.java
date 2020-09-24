package app.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.security.*;

public class TOMUtil {
    private static Logger logger = LoggerFactory.getLogger(TOMUtil.class);
    private static String sigAlgorithm = "SHA512withRSA";
    private static String hashAlgorithm = "SHA-512";
    private static String sigAlgorithmProvider = "SunRsaSign";
    private static String hashAlgorithmProvider = "SUN";

    public TOMUtil() {
    }

    public static byte[] signMessage(PrivateKey key, byte[] message) {
        byte[] result = null;

        try {
            Signature signatureEngine = getSigEngine();
            signatureEngine.initSign(key);
            signatureEngine.update(message);
            result = signatureEngine.sign();
        } catch (Exception var4) {
            logger.error("Failed to sign message", var4);
        }

        return result;
    }

    public static boolean verifySignature(PublicKey key, byte[] message, byte[] signature) {
        boolean result = false;

        try {
            Signature signatureEngine = getSigEngine();
            signatureEngine.initVerify(key);
            result = verifySignature(signatureEngine, message, signature);
        } catch (Exception var5) {
            logger.error("Failed to verify signature", var5);
        }

        return result;
    }

    public static boolean verifySignature(Signature initializedSignatureEngine, byte[] message, byte[] signature) throws SignatureException {
        initializedSignatureEngine.update(message);
        return initializedSignatureEngine.verify(signature);
    }

    public static final byte[] computeHash(byte[] data) {
        byte[] result = null;

        try {
            MessageDigest md = getHashEngine();
            result = md.digest(data);

        } catch (NoSuchAlgorithmException var3) {
            logger.error("Failed to compute hash", var3);
        }

        return result;
    }

    public static Signature getSigEngine() throws NoSuchAlgorithmException {
        return Signature.getInstance(sigAlgorithm, Security.getProvider(sigAlgorithmProvider));
    }

    public static MessageDigest getHashEngine() throws NoSuchAlgorithmException {
        return MessageDigest.getInstance(hashAlgorithm, Security.getProvider(hashAlgorithmProvider));
    }
}