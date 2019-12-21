package asa.org.bd.ammsma.crypto;

import android.util.Base64;

import java.nio.charset.StandardCharsets;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class Cryptography {

    /*public static JsonJavaViceVersaImport Decrypt(File selectedFile) throws Exception {

        int size = (int) selectedFile.length();
        byte[] bytes = new byte[size];
        String password = "ABCDEF";

        BufferedInputStream buf = new BufferedInputStream(new FileInputStream(selectedFile));

        buf.read(bytes, 0, size);

        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        byte[] keyBytes = new byte[16];
        byte[] b = password.getBytes("UTF-8");
        int len = b.length;
        if (len > keyBytes.length)
            len = keyBytes.length;
        System.arraycopy(b, 0, keyBytes, 0, len);
        SecretKeySpec keySpec = new SecretKeySpec(keyBytes, "AES");
        IvParameterSpec ivSpec = new IvParameterSpec(keyBytes);
        cipher.init(Cipher.DECRYPT_MODE, keySpec, ivSpec);

        try {
            buf.close();
            Reader targetReader = new InputStreamReader(new ByteArrayInputStream(cipher.doFinal(Base64.decode(bytes, Base64.DEFAULT))));
            *//*String s = new String(cipher.doFinal(Base64.decode(bytes, Base64.DEFAULT)));*//*
            JsonJavaViceVersaImport jsonJavaViceVersaImport = new Gson().fromJson(targetReader, JsonJavaViceVersaImport.class);
            targetReader.close();
            return jsonJavaViceVersaImport;
        } catch (Exception e) {
            Log.i("Error in Decryption", e.toString());
        }

        buf.close();

        return null;
    }*/

    public static String DecryptPass(String encryptedPass, String key) throws Exception {


        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        byte[] keyBytes = new byte[16];
        byte[] b = key.getBytes(StandardCharsets.UTF_8);
        int len = b.length;
        if (len > keyBytes.length)
            len = keyBytes.length;
        System.arraycopy(b, 0, keyBytes, 0, len);
        SecretKeySpec keySpec = new SecretKeySpec(keyBytes, "AES");
        IvParameterSpec ivSpec = new IvParameterSpec(keyBytes);
        cipher.init(Cipher.DECRYPT_MODE, keySpec, ivSpec);

        byte[] results = cipher.doFinal(Base64.decode(encryptedPass.getBytes(), Base64.DEFAULT));
        return new String(results, StandardCharsets.UTF_8);
    }

    public static String Encrypt(String text) throws Exception {
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        byte[] keyBytes = new byte[16];
        byte[] b = "ABCDEF".getBytes(StandardCharsets.UTF_8);
        int len = b.length;
        if (len > keyBytes.length)
            len = keyBytes.length;
        System.arraycopy(b, 0, keyBytes, 0, len);
        SecretKeySpec keySpec = new SecretKeySpec(keyBytes, "AES");
        IvParameterSpec ivSpec = new IvParameterSpec(keyBytes);
        cipher.init(Cipher.ENCRYPT_MODE, keySpec, ivSpec);

        byte[] results = Base64.encode(cipher.doFinal(text.getBytes(StandardCharsets.UTF_8)), Base64.DEFAULT);
        return new String(results, StandardCharsets.UTF_8);
    }

    public static byte[] encrypt128WithIv(byte[] data, byte[] key, byte[] ivs) {
        try {
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            SecretKeySpec secretKeySpec = new SecretKeySpec(key, "AES");
            byte[] finalIvs = new byte[16];

            int len = ivs.length > 16 ? 16 : ivs.length;
            System.arraycopy(ivs, 0, finalIvs, 0, len);
            IvParameterSpec ivps = new IvParameterSpec(finalIvs);
            cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec, ivps);
            return cipher.doFinal(data);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    public static byte[] decrypt128WithIv(byte[] data, byte[] key, byte[] ivs) {
        try {
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            SecretKeySpec secretKeySpec = new SecretKeySpec(key, "AES");
            byte[] finalIvs = new byte[16];
            int len = ivs.length > 16 ? 16 : ivs.length;
            System.arraycopy(ivs, 0, finalIvs, 0, len);
            IvParameterSpec ivps = new IvParameterSpec(finalIvs);
            cipher.init(Cipher.DECRYPT_MODE, secretKeySpec, ivps);
            return cipher.doFinal(data);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }
}
