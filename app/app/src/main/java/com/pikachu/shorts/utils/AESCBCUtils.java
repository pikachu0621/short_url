
package com.pikachu.shorts.utils;

import com.pikachu.shorts.App;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;


/**
 * @author Pikachu
 * @Project ThisAv
 * @Package com.pikachu.shorts
 * @Date 2021/3/26 ( 下午 1:16 )
 * @description
 */
public class AESCBCUtils {

    private static IvParameterSpec ivs;
    private static SecretKeySpec keys;
    private static Cipher cipher;
    private static final String hexStr = "0123456789ABCDEF";
    private static final char[] hexCode = hexStr.toCharArray();


    public static void init(String aesKey) {
        //LogsUtils.showLog(aesKey);
        String Coding = "UTF-8";
        String key = Md5(aesKey, true);
        String iv = Md5(key.substring(0, 8), true);
        try {
            ivs = new IvParameterSpec(iv.getBytes(Coding));
            keys = new SecretKeySpec(key.getBytes(Coding), "AES");
            cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * 加密
     *
     * @param value str
     * @return str
     */
    public static String encrypt(String value) {
        try {
            cipher.init(Cipher.ENCRYPT_MODE, keys, ivs);
            return bytesToHexStr(cipher.doFinal(value.getBytes(StandardCharsets.UTF_8)));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    /**
     * 解密
     *
     * @param encrypted str
     * @return str
     */
    public static String decrypt(String encrypted) {
        try {
            cipher.init(Cipher.DECRYPT_MODE, keys, ivs);
            byte[] decode = hexStrToBytes(encrypted);
            return new String(cipher.doFinal(decode));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    /**
     * md5
     *
     * @param sourceStr 字符串
     * @param is16      是否为16位长度
     * @return MD5
     */
    public static String Md5(String sourceStr, boolean is16) {
        String md5_32 = null;
        String md5_16 = null;
        String result;
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(sourceStr.getBytes());
            byte[] b = md.digest();
            int i;
            StringBuilder buf = new StringBuilder();
            for (byte value : b) {
                i = value;
                if (i < 0) i += 256;
                if (i < 16) buf.append("0");
                buf.append(Integer.toHexString(i));
            }
            result = buf.toString();
            md5_32 = result;
            md5_16 = buf.substring(8, 24);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        if (is16)
            return md5_16;
        return md5_32;
    }


    /**
     * 2 to 16
     *
     * @param data bytes
     * @return string
     */
    public static String bytesToHexStr(byte[] data) {
        StringBuilder r = new StringBuilder(data.length * 2);
        for (byte b : data) {
            r.append(hexCode[(b >> 4) & 0xF]);
            r.append(hexCode[(b & 0xF)]);
        }
        return r.toString();
    }


    /**
     * 16 to 2
     *
     * @param hex 16 string
     * @return bytes
     */
    public static byte[] hexStrToBytes(String hex) {
        int len = (hex.length() / 2);
        byte[] result = new byte[len];
        char[] abhor = hex.toCharArray();
        for (int i = 0; i < len; i++) {
            int pos = i * 2;
            result[i] = (byte) (toByte(abhor[pos]) << 4 | toByte(abhor[pos + 1]));
        }
        return result;
    }

    private static int toByte(char c) {
        return (byte) hexStr.indexOf(c);
    }


}
