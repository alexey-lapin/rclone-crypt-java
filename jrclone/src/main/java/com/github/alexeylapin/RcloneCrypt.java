package com.github.alexeylapin;

import org.bouncycastle.crypto.generators.SCrypt;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

// ls
// lsd
// copy
// cat
public class RcloneCrypt {

    private static final byte[] DEFAULT_SALT_BYTES = {(byte) 0xA8, 0x0D, (byte) 0xF4, 0x3A, (byte) 0x8F, (byte) 0xBD, 0x03, 0x08, (byte) 0xA7, (byte) 0xCA, (byte) 0xB8, 0x3E, 0x58, 0x1F, (byte) 0x86, (byte) 0xB1};

    private byte[] passBytes;
    private byte[] saltBytes;

    private byte[] nameKey;
    private byte[] nameTweak;

    private Cipher encCypher;
    private Cipher decCypher;

    public RcloneCrypt(byte[] passBytes, byte[] saltBytes) throws Exception {
        this.passBytes = passBytes;
        this.saltBytes = saltBytes;

        byte[] generate = SCrypt.generate(passBytes, saltBytes, 16384, 8, 1, 80);

        this.nameKey = new byte[32];
        System.arraycopy(generate, 32, nameKey, 0, 32);
        this.nameTweak = new byte[16];
        System.arraycopy(generate, 64, nameTweak, 0, 16);

        SecretKeySpec skSpec = new SecretKeySpec(nameKey, "AES");
        this.encCypher = Cipher.getInstance("AES/ECB/NoPadding");
        this.encCypher.init(Cipher.ENCRYPT_MODE, skSpec);

        this.decCypher = Cipher.getInstance("AES/ECB/NoPadding");
        this.decCypher.init(Cipher.DECRYPT_MODE, skSpec);
    }

    public String encryptSegment(String plaintext) {
        if ("".equals(plaintext)) {
            return plaintext;
        }

        byte[] paddedPlaintext = new PKCS7Padding().pad(16, plaintext.getBytes());
        byte[] ciphertext = new byte[0];
        try {
            ciphertext = new EME().transform(encCypher, decCypher, nameTweak, paddedPlaintext, true);
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
        return new CustomBase32().encode(ciphertext);
    }

    public String decryptSegment(String ciphertext) {
        if ("".equals(ciphertext)) {
            return ciphertext;
        }
        byte[] rawCiphertext = new CustomBase32().decode(ciphertext);
        if (rawCiphertext.length % 16 != 0) {
            throw new IllegalArgumentException();
        }
        if (rawCiphertext.length == 0) {
            throw new IllegalArgumentException();
        }
        if (rawCiphertext.length > 2048) {
            throw new IllegalArgumentException();
        }
        byte[] paddedPlaintext;
        try {
            paddedPlaintext = new EME().transform(encCypher, decCypher, nameTweak, rawCiphertext, false);
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
        byte[] plaintext = new PKCS7Padding().unpad(16, paddedPlaintext);
        return new String(plaintext);
    }

    public String encryptPath(String path) {
        return StreamSupport.stream(Paths.get(path).spliterator(), false)
                .map(item -> encryptSegment(item.toString()))
                .collect(Collectors.joining("/"));
    }

    public String decryptPath(String path) {
        return StreamSupport.stream(Paths.get(path).spliterator(), false)
                .map(item -> decryptSegment(item.toString()))
                .collect(Collectors.joining("/"));
    }

}
