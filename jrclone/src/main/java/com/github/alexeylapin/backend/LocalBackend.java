package com.github.alexeylapin.backend;

import com.github.alexeylapin.Item;
import lombok.AllArgsConstructor;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
public class LocalBackend implements Backend {

    private final Path root;

    @Override
    public List<Item> list(String path) {
        try {
            Path relative = root.resolve(path);
            return Files.walk(relative)
                    .filter(Files::isRegularFile)
                    .map(item -> fromPath(relative, item))
                    .collect(Collectors.toList());
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    @Override
    public List<Item> listDirs(String path) {
        try {
            Path relative = root.resolve(path);
            return Files.list(relative)
                    .filter(item -> item.toFile().isDirectory())
                    .map(item -> fromPath(relative, item))
                    .collect(Collectors.toList());
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    private Item fromPath(Path from, Path to) {
        Path relative = from.relativize(to);
        return new Item(relative.toString());
    }

}
