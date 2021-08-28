package com.github.alexeylapin;

import javax.crypto.Cipher;
import java.util.Arrays;

public class EME {

    byte[] transform(Cipher encCipher, Cipher decCipher, byte[] tweak, byte[] inputData, boolean isEncrypt) throws Exception {
        byte[] T = tweak;
        byte[] P = inputData;
        if (T.length != 16) {
            throw new IllegalArgumentException();
        }
        if (P.length % 16 != 0) {
            throw new IllegalArgumentException();
        }
        int m = P.length / 16;
        if (m == 0 || m > 16 * 8) {
            throw new IllegalArgumentException();
        }

        byte[] C = new byte[P.length];

        byte[][] LTable = tabulateL(encCipher, m);
        byte[] PPj = new byte[16];
        for (int j = 0; j < m; j++) {
            byte[] Pj = Arrays.copyOfRange(inputData, j * 16, (j + 1) * 16);
            PPj = xorBlocks(Pj, LTable[j]);
            byte[] out = aesTransform(PPj, isEncrypt ? encCipher : decCipher);
            System.arraycopy(out, 0, C, j * 16, out.length);
        }
//        System.out.println(Hex.toHexString(C));

        byte[] MP = new byte[16];
        byte[] CView = new byte[16];
        System.arraycopy(C, 0, CView, 0, 16);
        MP = xorBlocks(CView, T);
        for (int j = 1; j < m; j++) {
            System.arraycopy(C, j * 16, CView, 0, 16);
            MP = xorBlocks(MP, CView);
        }

        byte[] MC = new byte[16];
        MC = aesTransform(MP, isEncrypt ? encCipher: decCipher);

        byte[] M = new byte[16];
        M = xorBlocks(MP, MC);

        byte[] CCCj = new byte[16];
        for (int j = 1; j < m; j++) {
            M = multByTwo(M);
            System.arraycopy(C, j * 16, CView, 0, 16);
            CCCj = xorBlocks(CView, M);
            System.arraycopy(CCCj, 0, C, j * 16, 16);
        }

        byte[] CCC1 = new byte[16];
        CCC1 = xorBlocks(MC, T);
        for (int j = 1; j < m; j++) {
            System.arraycopy(C, j * 16, CView, 0, 16);
            CCC1 = xorBlocks(CCC1, CView);
        }
        System.arraycopy(CCC1, 0, C, 0, 16);

        for (int j = 0; j < m; j++) {
            System.arraycopy(C, j * 16, CView, 0, 16);
            byte[] out = aesTransform(CView, isEncrypt ? encCipher: decCipher);
            System.arraycopy(out, 0, C, j * 16, 16);
            System.arraycopy(C, j * 16, CView, 0, 16);
            byte[] bytes = xorBlocks(CView, LTable[j]);
            System.arraycopy(bytes, 0, C, j * 16, 16);
        }

        return C;
    }

    byte[] aesTransform(byte[] src, Cipher cipher) throws Exception {
        return cipher.doFinal(src);
    }

    byte[][] tabulateL(Cipher cipher, int m) throws Exception {
        byte[] eZero = new byte[16];
//        byte[] L1 = new byte[16];
        byte[] Li = cipher.doFinal(eZero);
        byte[][] LTable = new byte[m][];
        for (int i = 0; i < m; i++) {
//            byte[] out = multByTwo(Li);
            Li = multByTwo(Li);
//            System.out.println(Hex.toHexString(Li));
            LTable[i] = Li;
        }
        return LTable;
    }

    byte[] xorBlocks(byte[] in1, byte[] in2) {
        if (in1.length != in2.length) {
            throw new IllegalArgumentException();
        }
        byte[] out = new byte[in1.length];
        for (int i = 0; i < in1.length; i++) {
            out[i] = (byte) (in1[i] ^ in2[i]);
        }
        return out;
    }

    byte[] multByTwo(byte[] in) {
        if (in.length != 16) {
            throw new IllegalArgumentException("");
        }
        int[] tmp = new int[16];
        tmp[0] = (Byte.toUnsignedInt(in[0]) * 2) & 0xff;
        if (Byte.toUnsignedInt(in[15]) >= 128) {
            tmp[0] ^= 135;
        }
        for (int j = 1; j < 16; j++) {
            tmp[j] = (2 * Byte.toUnsignedInt(in[j])) & 0xff;
            if (Byte.toUnsignedInt(in[j - 1]) >= 128) {
                tmp[j] += 1;
            }
        }
        byte[] out = new byte[16];
        for (int i = 0; i < 16; i++) {
            out[i] = (byte) tmp[i];
        }
        return out;
    }

}
