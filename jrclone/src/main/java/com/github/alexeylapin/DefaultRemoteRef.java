package com.github.alexeylapin;

import java.nio.file.Paths;
import java.util.Optional;

// remote:
// remote:path/to/dir
// path/to/dir
// c:\path\to\dir
// C:/path/to/dir
// C:
// C:/
public class DefaultRemoteRef implements RemoteRef {

    private final String ref;
    private final String name;
    private final String path;

    public DefaultRemoteRef(String ref) {
        this.ref = ref;
        boolean hasColon = ref.contains(":");
        if (hasColon) {
            boolean isValidPathRef;
            try {
                isValidPathRef = Paths.get(ref).getRoot().toFile().exists();
            } catch (Exception ex) {
                isValidPathRef = false;
            }
            if (isValidPathRef) {
                this.name = null;
                this.path = ref;
            } else {
                String[] parts = ref.split(":");
                if (parts.length == 1) {
                    this.name = parts[0];
                    this.path = "";
                } else if (parts.length == 2) {
                    this.name = parts[0];
                    this.path = parts[1];
                } else {
                    throw new RuntimeException("bad");
                }
            }
        } else {
            this.name = null;
            this.path = ref;
        }
    }

    @Override
    public Optional<String> getName() {
        return Optional.ofNullable(name);
    }

    @Override
    public String getPath() {
        return path;
    }

    private static boolean isWindows() {
        String os = System.getProperty("os.name");
        return os.startsWith("Windows");
    }

}
