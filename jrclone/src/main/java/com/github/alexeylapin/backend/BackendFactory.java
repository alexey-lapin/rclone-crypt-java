package com.github.alexeylapin.backend;


import com.github.alexeylapin.BackendConfig;
import com.github.alexeylapin.Config;
import com.github.alexeylapin.DefaultRemoteRef;
import lombok.AllArgsConstructor;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;

@AllArgsConstructor
public class BackendFactory {

    private final Config backends;

//    public Optional<Backend> findByName(String name) {
//
//    }

    public Backend create(BackendConfig config) {
        Optional<String> remoteParameterOptional = config.findParameter(BackendConfig.PARAMETER_REMOTE);
        Backend delegate;
        if (remoteParameterOptional.isPresent()) {
            DefaultRemoteRef remoteRef = new DefaultRemoteRef(remoteParameterOptional.get());
            if (remoteRef.getName().isPresent()) {
                Optional<BackendConfig> remoteConfigOptional = backends.findBackend(remoteRef.getName().get());
                if (remoteConfigOptional.isPresent()) {
                    delegate = create(remoteConfigOptional.get());
                } else {
                    throw new RuntimeException("ref does not exist");
                }
            } else {
                delegate = new LocalBackend(Paths.get(remoteRef.getPath()));
            }
        } else {
            delegate = null;
        }

        if (BackendConfig.TYPE_LOCAL.equals(config.getType())) {
            return new LocalBackend(Paths.get(""));
        } else if (BackendConfig.TYPE_CRYPT.equals(config.getType())) {
            return new CryptBackend(config, delegate);
        } else {
            throw new RuntimeException("unknown type");
        }
    }


}
