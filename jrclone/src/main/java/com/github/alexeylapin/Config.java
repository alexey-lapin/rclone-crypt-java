package com.github.alexeylapin;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

public class Config {

    private final Map<String, BackendConfig> configs;

    public Config() {
        this(Paths.get(System.getProperty("user.home"), ".config", "rclone", "rclone.conf").toString());
    }

    public Config(String path) {
        IniFile iniFile;
        try {
            iniFile = new IniFile(path);
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
        configs = iniFile.getEntries().entrySet().stream()
                .collect(Collectors.toMap(item -> item.getKey(), item -> new BackendConfig(item.getValue())));
    }

    public Optional<BackendConfig> findBackend(String name) {
        return Optional.ofNullable(configs.get(name));
    }

}
