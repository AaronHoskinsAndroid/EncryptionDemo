package examples.aaronhoskins.com.encryptiondemo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import java.security.KeyPair;

public class MainActivity extends AppCompatActivity {
    public static final String TRANSFORMATION = "RSA/ECB/PKCS1Padding";
    TextView tvEncryptedString;
    TextView tvDecryptedString;
    EditText etTextToEncrypt;
    SharedPreferences prefs;
    KeyStoreWrapper keyStoreWrapper;
    CipherWrapper cipherWrapper;
    KeyPair keyPair;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        prefs = getSharedPreferences("encrypted", MODE_PRIVATE);
        bindViews();

        try{
            keyStoreWrapper = new KeyStoreWrapper(getApplicationContext());
            cipherWrapper = new CipherWrapper(TRANSFORMATION);
            keyStoreWrapper.createKeyPair("master");
            keyPair = keyStoreWrapper.getAsymKey("master");
        } catch (Exception e) {
            Log.d("TAG", "ERROR IN SETUP", e);
        }
    }

    private void bindViews() {
        tvDecryptedString = findViewById(R.id.tvDecryptedString);
        tvEncryptedString = findViewById(R.id.tvEncryptedString);
        etTextToEncrypt = findViewById(R.id.etTextToEncrypt);
    }

    public void onClick(View view) {

        switch(view.getId()) {
            case R.id.btnEncryptUserText:
                try {
                    final String textToEncrypt = etTextToEncrypt.getText().toString();
                    final String encryptedString
                            = cipherWrapper.encryptString(textToEncrypt,
                            keyPair.getPublic());
                    prefs.edit().putString("key", encryptedString).apply();
                } catch(Exception e) {
                    Log.d("TAG", "ERROR IN ENCRYPTION", e);
                }
                break;
            case R.id.banDecryptText:
                try {
                    final String stringToDecrypt = prefs.getString("key", "ERROR");
                    if (!stringToDecrypt.equals("ERROR")) {
                        final String decryptedString
                                = cipherWrapper.decryptedString(stringToDecrypt,
                                keyPair.getPrivate());
                        tvEncryptedString.setText(stringToDecrypt);
                        tvDecryptedString.setText(decryptedString);
                    }
                } catch (Exception e) {
                    Log.d("TAG", "ERROR IN DECRYPTION", e);
                }
                break;
        }
    }
}
