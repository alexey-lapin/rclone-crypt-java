package com.github.alexeylapin;

import org.junit.jupiter.api.Test;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;

public class ObscureTest {

    int[] cryptKey = new int[]{
            0x9c, 0x93, 0x5b, 0x48, 0x73, 0x0a, 0x55, 0x4d,
            0x6b, 0xfd, 0x7c, 0x63, 0xc8, 0x86, 0xa9, 0x2b,
            0xd3, 0x90, 0x19, 0x8e, 0xb8, 0x12, 0x8a, 0xfb,
            0xf4, 0xde, 0x16, 0x2b, 0x8b, 0x95, 0xf6, 0x38,
    };

    @Test
    void name() throws Exception {
        String obscured = "";
        byte[] decode = Base64.getUrlDecoder().decode(obscured);

        byte[] iv = new byte[16];
        byte[] data = new byte[decode.length - 16];
        System.arraycopy(decode, 0, iv, 0, iv.length);
        System.arraycopy(decode, 16, data, 0, data.length);

        SecretKeySpec skSpec = new SecretKeySpec(bytes(cryptKey), "AES");

        Cipher cipher = Cipher.getInstance("AES/CTR/NoPadding");
        cipher.init(Cipher.DECRYPT_MODE, skSpec, new IvParameterSpec(iv));

        byte[] bytes = cipher.doFinal(data);
        String s = new String(bytes);
        System.out.println(s);
    }

    byte[] bytes(int[] ints) {
        byte[] bytes = new byte[ints.length];
        for (int i = 0; i < ints.length; i++) {
            bytes[i] = (byte) ints[i];
        }
        return bytes;
    }

}
