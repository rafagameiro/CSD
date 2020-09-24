package app.utils;


import java.util.Base64;

public class SmartContractUtils {

    public static String parseContract(String contractB64) {
        return new String(Base64.getDecoder().decode(contractB64));
    }
}
