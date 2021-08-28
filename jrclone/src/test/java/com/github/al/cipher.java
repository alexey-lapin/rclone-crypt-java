package com.github.al;

import com.github.alexeylapin.RcloneCrypt;
import com.iwebpp.crypto.TweetNaclFast;
import org.apache.commons.codec.binary.Base32;
import org.bouncycastle.crypto.generators.SCrypt;
import org.bouncycastle.crypto.paddings.PKCS7Padding;
import org.bouncycastle.util.encoders.Hex;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.io.File;
import java.io.FileInputStream;
import java.security.MessageDigest;
import java.util.Arrays;
import java.util.Locale;

public class cipher {

    @Test
    void name() throws Exception {
        MessageDigest md5 = MessageDigest.getInstance("MD5");
        byte[] key = md5.digest();
        byte[] value = md5.digest();

        Cipher rc4 = Cipher.getInstance("RC4");
        rc4.init(Cipher.ENCRYPT_MODE, new SecretKeySpec(key, "RC4"));
        byte[] bytes = rc4.doFinal(value);
        System.out.println("bytes = " + bytes);

    }

//    @Test
//    void name2() throws Exception {
//        byte[] key = new byte[32];
//        SecretKeySpec skSpec = new SecretKeySpec(key, "AES");
//        Cipher cipher = Cipher.getInstance("AES/ECB/NoPadding");
//        cipher.init(Cipher.ENCRYPT_MODE, skSpec);
//        byte[] bytes = cipher.doFinal(new byte[16]);
//
//        System.out.println(bytes);
//        System.out.println(Hex.toHexString(bytes));
//
//        byte[] bytes1 = multByTwo(bytes);
//        System.out.println(Hex.toHexString(bytes1));
//    }

//    @Test
//    void name4() throws Exception {
//        byte[] key = new byte[32];
//        SecretKeySpec skSpec = new SecretKeySpec(key, "AES");
//        Cipher cipher = Cipher.getInstance("AES/ECB/NoPadding");
//        cipher.init(Cipher.ENCRYPT_MODE, skSpec);
//
////        byte[][] bytes = tabulateL(cipher, 32);
//        byte[] transform = transform(cipher, new byte[16], new byte[512], true);
////        System.out.println(bytes);
//        System.out.println(">> " + Hex.toHexString(transform));
//    }

//    @Test
//    void name5() throws Exception {
//        String pass = "1234";
////        String pass = "";
//        String salt = "1234";
////        String salt = "";
////        byte[] saltBytes = {(byte) 0xA8, 0x0D, (byte) 0xF4, 0x3A, (byte) 0x8F, (byte) 0xBD, 0x03, 0x08, (byte) 0xA7, (byte) 0xCA, (byte) 0xB8, 0x3E, 0x58, 0x1F, (byte) 0x86, (byte) 0xB1};
//        byte[] saltBytes = salt.getBytes();
//        byte[] generate = SCrypt.generate(pass.getBytes(), saltBytes, 16384, 8, 1, 80);
////        byte[] generate = new byte[80];
//
//        byte[] nameKey = new byte[32];
//        System.arraycopy(generate, 32, nameKey, 0, 32);
//        byte[] nameTweak = new byte[16];
//        System.arraycopy(generate, 64, nameTweak, 0, 16);
//
//        SecretKeySpec skSpec = new SecretKeySpec(nameKey, "AES");
//        Cipher cipher = Cipher.getInstance("AES/ECB/NoPadding");
//        cipher.init(Cipher.ENCRYPT_MODE, skSpec);
//
////        String input = "some-path-------";
//        String input = "test.txt";
//
////        com.github.alexeylapin.PKCS7Padding padding = new com.github.alexeylapin.PKCS7Padding();
//        byte[] bytes = input.getBytes();
////        padding.addPadding(bytes, 16);
//        bytes = pad(16, bytes);
//
//        byte[] transform = transform(cipher, nameTweak, bytes, true);
//        String s = encode(transform);
//        System.out.println(s);
//    }

    @Test
    void name3() {
        byte b = -2;
        int i = Byte.toUnsignedInt(b);
        byte bb = (byte) i;
        System.out.println();
    }

//    @Test
//    void name6() {
//        String s1 = encode("".getBytes());
//        System.out.println(s1);
//
//        String s2 = encode("1".getBytes());
//        System.out.println(s2);
//
//        String s3 = encode("12".getBytes());
//        System.out.println(s3);
//
//        String s4 = encode("123".getBytes());
//        System.out.println(s4);
//
//        String s5 = encode("1234".getBytes());
//        System.out.println(s5);
//
//        String s6 = encode("12345".getBytes());
//        System.out.println(s6);
//
//        String s7 = encode("123456".getBytes());
//        System.out.println(s7);
//
//        String s8 = encode("1234567".getBytes());
//        System.out.println(s8);
//    }


    @Test
    void name7() throws Exception {
        RcloneCrypt rcloneCrypt = new RcloneCrypt("1234".getBytes(), "1234".getBytes());
        String s = rcloneCrypt.encryptSegment("test.txt");
        System.out.println(s);
    }

    @Test
    void name8() throws Exception {
        RcloneCrypt rcloneCrypt = new RcloneCrypt("1234".getBytes(), "1234".getBytes());
//        String s = rcloneCrypt.decryptSegment("i5qodltltbua0vkl2rt1667uo0");
        String s = rcloneCrypt.decryptSegment("45bsao76asesh60oh40s18kb2s");
        System.out.println(s);
    }

    @Disabled
    @Test
    void name9() throws Exception {
        File file = new File("45bsao76asesh60oh40s18kb2s\\i5qodltltbua0vkl2rt1667uo0");
        try (FileInputStream fileInputStream = new FileInputStream(file)) {
            byte[] magic = new byte[8];
            fileInputStream.read(magic);
            byte[] nonce = new byte[24];
            fileInputStream.read(nonce);
            System.out.println();

            String pass = "1234";
            String salt = "1234";
            byte[] saltBytes = salt.getBytes();
            byte[] generate = SCrypt.generate(pass.getBytes(), saltBytes, 16384, 8, 1, 80);

            //        byte[] nameKey = new byte[32];
//        System.arraycopy(generate, 32, nameKey, 0, 32);
//        byte[] nameTweak = new byte[16];
//        System.arraycopy(generate, 64, nameTweak, 0, 16);
//            byte[] bytes = new byte[64 * 1024];
            byte[] bytes = new byte[20];
            fileInputStream.read(bytes);

            TweetNaclFast.SecretBox sbox = new TweetNaclFast.SecretBox(generate);
            byte[] open = sbox.open(bytes, nonce);
            System.out.println(new String(open));
        }
    }

}
