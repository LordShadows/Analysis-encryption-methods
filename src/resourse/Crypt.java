package resourse;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.*;
import java.security.*;
import java.util.*;

/**
 * Created by DELL on 31.01.2017.
 * @author Daniel Sandrutski
 */

public class Crypt {
    private static final byte[] SALT8 = new byte[]{
            (byte) 0xc7, (byte) 0x73, (byte) 0x21, (byte) 0x8c,
            (byte) 0x7e, (byte) 0xc8, (byte) 0xee, (byte) 0x99};

    private static final byte[] SALT16 = new byte[]{
            (byte) 0xc7, (byte) 0x73, (byte) 0x21, (byte) 0x8c,
            (byte) 0x7e, (byte) 0xc8, (byte) 0xee, (byte) 0x99,
            (byte) 0x77, (byte) 0xe3, (byte) 0x21, (byte) 0xfc,
            (byte) 0x75, (byte) 0xc6, (byte) 0x1e, (byte) 0xf9};
    /**
     * Симмитричное шифрование
     */
    public static byte[] encode(String cleartext, byte[] key, String chiper, String typeKey) {
        try {
            SecretKey DESKey = new SecretKeySpec(key, 0, key.length, typeKey);
            return crypt(cleartext.getBytes("UTF-8"), DESKey, Cipher.ENCRYPT_MODE, chiper);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Симметричная расшифровка
     */
    public static String decode(byte[] ciphertext, byte[] key, String chiper, String typeKey) {
        try {
            SecretKey DESKey = new SecretKeySpec(key, 0, key.length, typeKey);
            return new String(crypt(ciphertext, DESKey, Cipher.DECRYPT_MODE, chiper), "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Основной метод симмертичного шифрования
     */
    private static byte[] crypt(byte[] input, Key key, int mode, String chiper) {
        IvParameterSpec iv;
        if(!chiper.contains("AES")) iv = new IvParameterSpec(SALT8); else iv = new IvParameterSpec(SALT16);
        Cipher c;
        try {
            c = Cipher.getInstance(chiper);
            c.init(mode, key, iv);
            return c.doFinal(input);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    /**
     * Метод ассимитричной расшифровки сеансового ключа
     */
    public static String decryptRSA(ArrayList<byte[]> cipherText, PrivateKey key)
    {
        StringBuilder decodetext = new StringBuilder();
        try {
            final Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
            cipher.init(Cipher.DECRYPT_MODE, key);
            for(int i = 0; i < cipherText.size(); i++) {
                decodetext.append(new String(cipher.doFinal(cipherText.get(i))));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return decodetext.toString();
    }

    /**
     * Метод для ассимметричного шифрования сеансового ключа
     */
    public static ArrayList<byte[]> encryptRSA(String text, PublicKey key)
    {
        ArrayList<byte[]> cipherText = new ArrayList<>();
        int keylength = 117;
        try {
            final Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
            cipher.init(Cipher.ENCRYPT_MODE, key);
            do {
                String textblock;
                if(text.length() - keylength >= 0){
                    textblock = text.substring(0, keylength);
                    text = text.substring(keylength);
                }
                else
                {
                    textblock = text;
                    text = "";
                }
                cipherText.add(cipher.doFinal(textblock.getBytes()));

            } while (text.length() > 0);

        } catch (Exception e) {
        e.printStackTrace();
        }
        return cipherText;
    }

    public static byte[] encryptKeyRSA(byte[] text, PublicKey key)
    {
        byte[] cipherText = null;
        try {
            final Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
            cipher.init(Cipher.ENCRYPT_MODE, key);
            cipherText = cipher.doFinal(text);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return cipherText;
    }

    /**
     * Метод ассимитричной расшифровки сеансового ключа
     */
    public static byte[] decryptKeyRSA(byte[] text, PrivateKey key)
    {
        byte[] dectyptedText = null;
        try {
            final Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
            cipher.init(Cipher.DECRYPT_MODE, key);
            dectyptedText = cipher.doFinal(text);

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return dectyptedText;
    }


    /**
     * Генерация ассимитричного ключа
     */
    public static KeyPair generateKeysRSA() {
        try {
            final KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA");
            keyGen.initialize(2048, new SecureRandom());
            final KeyPair key = keyGen.generateKeyPair();

            return key;
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Генерация сеансового ключа
     */
    public static byte[] generateKey(String keyType, int bit) {
        try {
            KeyGenerator keyGen = KeyGenerator.getInstance(keyType);
            keyGen.init(bit);
            SecretKey secretKey = keyGen.generateKey();
            return secretKey.getEncoded();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static String byte2Hex(byte b[])
    {
        String hs = "";
        String stmp;
        for (byte aB : b) {
            stmp = Integer.toHexString(aB & 0xff);
            if (stmp.length() == 1)
                hs = hs + "0" + stmp;
            else
                hs = hs + stmp;
        }
        return hs.toLowerCase();
    }

    private static byte hex2Byte(char a1, char a2)
    {
        int k;
        if (a1 >= '0' && a1 <= '9') k = a1 - 48;
        else if (a1 >= 'a' && a1 <= 'f') k = (a1 - 97) + 10;
        else if (a1 >= 'A' && a1 <= 'F') k = (a1 - 65) + 10;
        else k = 0;
        k <<= 4;
        if (a2 >= '0' && a2 <= '9') k += a2 - 48;
        else if (a2 >= 'a' && a2 <= 'f') k += (a2 - 97) + 10;
        else if (a2 >= 'A' && a2 <= 'F') k += (a2 - 65) + 10;
        else k += 0;
        return (byte) (k & 0xff);
    }

    private static byte[] hex2Byte(String str)
    {
        int len = str.length();
        if (len % 2 != 0) return null;
        byte r[] = new byte[len / 2];
        int k = 0;
        for (int i = 0; i < str.length() - 1; i += 2)
        {
            r[k] = hex2Byte(str.charAt(i), str.charAt(i + 1));
            k++;
        }
        return r;
    }
}
