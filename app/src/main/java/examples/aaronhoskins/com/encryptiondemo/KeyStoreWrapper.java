package examples.aaronhoskins.com.encryptiondemo;

import android.content.Context;
import android.security.KeyPairGeneratorSpec;

import java.math.BigInteger;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.Calendar;

import javax.security.auth.x500.X500Principal;

public class KeyStoreWrapper {
    public static final String PROVIDER_KEYSTORE = "AndroidKeyStore";
    private Context context;
    KeyStore keyStore;

    public KeyStoreWrapper(Context context) throws Exception{
        this.context = context;
        keyStore = KeyStore.getInstance(PROVIDER_KEYSTORE);
        keyStore.load(null);
    }

    public KeyPair createKeyPair(String alias) throws Exception {
        KeyPairGenerator generator =
                KeyPairGenerator
                        .getInstance("RSA", PROVIDER_KEYSTORE);

        final Calendar startTime = Calendar.getInstance();
        Calendar endTime = Calendar.getInstance();
        endTime.add(Calendar.YEAR, 1);

        KeyPairGeneratorSpec generatedSpec =
                new KeyPairGeneratorSpec.Builder(context)
                .setAlias(alias)
                .setSerialNumber(BigInteger.ONE)
                .setStartDate(startTime.getTime())
                .setEndDate(endTime.getTime())
                .setSubject(new X500Principal(String.format("CN = %s CA Certificate", alias)))
                .build();

        generator.initialize(generatedSpec);
        return generator.generateKeyPair();

    }

    public KeyPair getAsymKey(String alias) throws Exception {
        PrivateKey privateKey
                = (PrivateKey)keyStore.getKey(alias, null);
        PublicKey publicKey = keyStore.getCertificate(alias).getPublicKey();

        if(privateKey != null && publicKey != null) {
            return new KeyPair(publicKey, privateKey);
        }
        return null;
    }

    public void removeKeys(String alias) throws Exception{
        keyStore.deleteEntry(alias);
    }
}
