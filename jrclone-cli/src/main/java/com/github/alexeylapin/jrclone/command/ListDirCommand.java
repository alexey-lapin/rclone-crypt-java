package com.github.alexeylapin.jrclone.command;

import com.github.alexeylapin.BackendConfig;
import com.github.alexeylapin.Config;
import com.github.alexeylapin.DefaultRemoteRef;
import com.github.alexeylapin.backend.Backend;
import com.github.alexeylapin.backend.BackendFactory;
import picocli.CommandLine.Command;
import picocli.CommandLine.Parameters;

@Command(name = "lsd")
public class ListDirCommand implements Runnable {

    @Parameters
    private String path;

    @Override
    public void run() {
        DefaultRemoteRef remoteRef = new DefaultRemoteRef(path);

        Config config = new Config();
        BackendConfig backendConfig = config.findBackend(remoteRef.getName().get())
                .orElseThrow(() -> new RuntimeException("remote not found"));

        Backend backend = new BackendFactory(config).create(backendConfig);

        backend.listDirs(remoteRef.getPath()).forEach(item -> System.out.println(item.getName()));
    }

}
