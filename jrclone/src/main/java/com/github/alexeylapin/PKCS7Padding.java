package com.github.alexeylapin;

public class PKCS7Padding {

    public byte[] pad(int n, byte[] in) {
        if (n <= 1 || n >= 256) {
            throw new IllegalArgumentException();
        }
        int length = in.length;
        int padding = n - (length % n);
        byte[] out = new byte[in.length + padding];
        System.arraycopy(in, 0, out, 0, in.length);
        for (int i = padding; i > 0; i--) {
            out[out.length - i] = (byte) padding;
        }
        return out;
    }

    public byte[] unpad(int n, byte[] buf) {
        if (n <= 1 || n >= 256) {
            throw new IllegalArgumentException();
        }
        int length = buf.length;
        if (length == 0) {
            throw new IllegalArgumentException();
        }
        if (length % n != 0) {
            throw new IllegalArgumentException();
        }
        int padding = buf[buf.length - 1];
        if (padding > n) {
            throw new IllegalArgumentException();
        }
        if (padding == 0) {
            throw new IllegalArgumentException();
        }
        for(int i = 0; i < padding; i++) {
            if(buf[length-1-i] != (byte) padding) {
                throw new IllegalArgumentException();
            }
        }
        byte[] out = new byte[buf.length - padding];
        System.arraycopy(buf, 0, out, 0, buf.length - padding);
        return out;
    }

}
