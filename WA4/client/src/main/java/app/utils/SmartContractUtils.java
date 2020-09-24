package app.utils;

import app.exceptions.SmartContractException;

import java.io.File;
import java.io.FileInputStream;
import java.util.Base64;

public class SmartContractUtils {

    public static String loadContract(String filePath) throws SmartContractException {
        try {
            File f = new File(filePath);
            FileInputStream fis = new FileInputStream(f);
            byte[] contractBytes = new byte[fis.available()];
            fis.read(contractBytes);
            return Base64.getEncoder().encodeToString(contractBytes);
        } catch (Exception e) {
            throw new SmartContractException(filePath);
        }
    }

}
