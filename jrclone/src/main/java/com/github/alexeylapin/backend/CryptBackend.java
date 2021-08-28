package com.github.alexeylapin.backend;

import com.github.alexeylapin.BackendConfig;
import com.github.alexeylapin.Item;
import com.github.alexeylapin.Obscure;
import com.github.alexeylapin.RcloneCrypt;
import lombok.AllArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
public class CryptBackend implements Backend {

    private final RcloneCrypt crypt;
    private final Backend delegate;

    public CryptBackend(BackendConfig config, Backend delegate) {
        this.delegate = delegate;

        Obscure obscure = new Obscure();
        String password = obscure.reveal(config.findParameter("password")
                .orElseThrow(() -> new RuntimeException("no password")));
        String password2 = obscure.reveal(config.findParameter("password2")
                .orElseThrow(() -> new RuntimeException("no password2")));
        try {
            this.crypt = new RcloneCrypt(password.getBytes(), password2.getBytes());
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    @Override
    public List<Item> list(String path) {
        return delegate.list(crypt.encryptPath(path)).stream()
                .map(item -> new Item(crypt.decryptPath(item.getName())))
                .collect(Collectors.toList());
    }

    @Override
    public List<Item> listDirs(String path) {
        return delegate.listDirs(crypt.encryptPath(path)).stream()
                .map(item -> new Item(crypt.decryptPath(item.getName())))
                .collect(Collectors.toList());
    }

}
