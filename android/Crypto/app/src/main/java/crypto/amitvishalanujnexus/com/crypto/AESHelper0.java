package crypto.amitvishalanujnexus.com.crypto;

import android.util.Base64;
import android.util.Log;
import android.widget.Toast;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Arrays;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.spec.SecretKeySpec;

/**
 * Created by Vipul Srivastava on 5/26/2015.
 */
public class AESHelper0 {

    /*This is random key generation
    public static SecretKeySpec keygenerate(){
        SecretKeySpec sks = null;
        try {
            SecureRandom sr = SecureRandom.getInstance("SHA1PRNG");
            sr.setSeed(("any data used at" +
                    " random seed").getBytes());
            KeyGenerator kg = KeyGenerator.getInstance("AES");
            kg.init(128, sr);
            sks = new SecretKeySpec((kg.generateKey()).getEncoded(), "AES");
        } catch (Exception e) {
            Log.e("", "AES secret key spec error");

        }

        return sks;
    }*/

    //This is user generated key
    public static SecretKeySpec keygenerate(String passphrase){
        SecretKeySpec sks = null;
        try{
        byte[] key = passphrase.getBytes("UTF-8");
        MessageDigest sha = MessageDigest.getInstance("SHA-1");
        key = sha.digest(key);
        key = Arrays.copyOf(key, 16); // use only first 128 bit

        sks = new SecretKeySpec(key, "AES");}
        catch (NoSuchAlgorithmException e){}
        catch(UnsupportedEncodingException e){}
        return sks;
    }

    public static String v_encrypt(String text,SecretKeySpec sks){
        byte[] encodedBytes = null;
        try {
            Cipher c = Cipher.getInstance("AES");
            c.init(Cipher.ENCRYPT_MODE, sks);
            encodedBytes = c.doFinal(text.getBytes());
        } catch (Exception e) {
            Log.e("", "AES encryption error");
            e.printStackTrace();
        }
        return Base64.encodeToString(encodedBytes, Base64.DEFAULT);
    }

    public static String v_decrypt(String text,SecretKeySpec sks){
        byte[] decodedBytes = null;
        byte[] encodedBytes = null;
        encodedBytes = Base64.decode(text,Base64.DEFAULT);
        try {
            Cipher c = Cipher.getInstance("AES");
            c.init(Cipher.DECRYPT_MODE, sks);
            decodedBytes = c.doFinal(encodedBytes);
        } catch (Exception e) {
            Log.e("", "AES decryption error");
            e.printStackTrace();
        }
        return new String(decodedBytes);
    }
}
