package com.github.alexeylapin;

import lombok.AllArgsConstructor;

import java.util.Map;
import java.util.Optional;

@AllArgsConstructor
public class BackendConfig {

    public static final String PARAMETER_REMOTE = "remote";
    public static final String PARAMETER_TYPE = "type";

    public static final String TYPE_CRYPT = "crypt";
    public static final String TYPE_LOCAL = "local";

    private final Map<String, String> map;

    public String getType() {
        return map.get(PARAMETER_TYPE);
    }

    public Optional<String> findParameter(String name) {
        return Optional.ofNullable(map.get(name));
    }

}
