package com.github.alexeylapin;

import org.apache.commons.codec.binary.Base32;

//TODO: get rid of apache dependency
public class CustomBase32 {

    private final Base32 BASE32 = new Base32(true);

    String encode(byte[] bytes) {
        String base32 = BASE32.encodeToString(bytes).toLowerCase();
        while (base32.length() > 0 && base32.charAt(base32.length() - 1) == '=') {
            base32 = base32.substring(0, base32.length() - 1);
        }
        return base32;
    }

    byte[] decode(String in) {
        if (in.endsWith("=")) {
            throw new IllegalArgumentException();
        }
        int roundUpToMultipleOf8 = (in.length() + 7) & (-8);
        int equals = roundUpToMultipleOf8 - in.length();
        for (int i = 0; i < equals; i++) {
            in += "=";
        }
        return BASE32.decode(in.toUpperCase());
    }

}
