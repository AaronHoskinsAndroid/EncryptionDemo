package examples.aaronhoskins.com.encryptiondemo;

import android.util.Base64;

import java.security.Key;


import javax.crypto.Cipher;

public class CipherWrapper {
    String transformation;
    Cipher cipher;

    public CipherWrapper(String transformation) throws Exception{
        this.transformation = transformation;
        cipher = Cipher.getInstance(transformation);
    }

    public String encryptString(String stringToEncrypt, Key key) throws Exception{
        cipher.init(Cipher.ENCRYPT_MODE, key);
        byte[] encryptedBytes = cipher.doFinal(stringToEncrypt.getBytes());
        return Base64.encodeToString(encryptedBytes, Base64.DEFAULT);
    }

    public String decryptedString(String stringToDecrypt, Key key) throws Exception {
        cipher.init(Cipher.DECRYPT_MODE, key);
        byte[] encryptedBytes = Base64.decode(stringToDecrypt, Base64.DEFAULT);
        byte[] decryptedBytes = cipher.doFinal(encryptedBytes);
        return new String(decryptedBytes);
    }
}
