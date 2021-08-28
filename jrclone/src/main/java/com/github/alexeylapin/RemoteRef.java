package com.github.alexeylapin;

import java.util.Optional;

public interface RemoteRef {

    Optional<String> getName();

    String getPath();

}
