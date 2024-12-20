package com.github.alexeylapin.fs;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.channels.SeekableByteChannel;
import java.nio.file.AccessMode;
import java.nio.file.CopyOption;
import java.nio.file.DirectoryStream;
import java.nio.file.FileStore;
import java.nio.file.FileSystem;
import java.nio.file.FileSystemAlreadyExistsException;
import java.nio.file.FileSystemNotFoundException;
import java.nio.file.LinkOption;
import java.nio.file.OpenOption;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.FileAttribute;
import java.nio.file.attribute.FileAttributeView;
import java.nio.file.spi.FileSystemProvider;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class SystemCryptFileSystemProvider extends FileSystemProvider {

    static final String FILE_SYSTEM_KEY = "fileSystem";

    private static final Map<URI, FileSystem> fileSystems = new ConcurrentHashMap<>();

    @Override
    public String getScheme() {
        return "rclone-crypt";
    }

    @Override
    public FileSystem newFileSystem(URI uri, Map<String, ?> env) throws IOException {
        FileSystem fileSystem = (FileSystem) env.get(FILE_SYSTEM_KEY);
        if (fileSystems.putIfAbsent(uri, fileSystem) != null) {
            throw new FileSystemAlreadyExistsException(uri.toString());
        }
        return fileSystem;
    }

    @Override
    public FileSystem getFileSystem(URI uri) {
        FileSystem fileSystem = fileSystems.get(uri);
        if (fileSystem == null) {
            throw new FileSystemNotFoundException(uri.toString());
        }
        return fileSystem;
    }

    @Override
    public Path getPath(URI uri) {
        String path = uri.getPath();
        return getFileSystem(toFileSystemUri(uri)).getPath(path);
    }

    @Override
    public SeekableByteChannel newByteChannel(Path path, Set<? extends OpenOption> options, FileAttribute<?>... attrs) throws IOException {
        return null;
    }

    @Override
    public DirectoryStream<Path> newDirectoryStream(Path dir, DirectoryStream.Filter<? super Path> filter) throws IOException {
        return null;
    }

    @Override
    public void createDirectory(Path dir, FileAttribute<?>... attrs) throws IOException {

    }

    @Override
    public void delete(Path path) throws IOException {

    }

    @Override
    public void copy(Path source, Path target, CopyOption... options) throws IOException {

    }

    @Override
    public void move(Path source, Path target, CopyOption... options) throws IOException {

    }

    @Override
    public boolean isSameFile(Path path, Path path2) throws IOException {
        return false;
    }

    @Override
    public boolean isHidden(Path path) throws IOException {
        return false;
    }

    @Override
    public FileStore getFileStore(Path path) throws IOException {
        return null;
    }

    @Override
    public void checkAccess(Path path, AccessMode... modes) throws IOException {

    }

    @Override
    public <V extends FileAttributeView> V getFileAttributeView(Path path, Class<V> type, LinkOption... options) {
        return null;
    }

    @Override
    public <A extends BasicFileAttributes> A readAttributes(Path path, Class<A> type, LinkOption... options) throws IOException {
        return null;
    }

    @Override
    public Map<String, Object> readAttributes(Path path, String attributes, LinkOption... options) throws IOException {
        return Map.of();
    }

    @Override
    public void setAttribute(Path path, String attribute, Object value, LinkOption... options) throws IOException {

    }

    private static URI toFileSystemUri(URI uri) {
        try {
            return new URI(
                    uri.getScheme(), uri.getUserInfo(), uri.getHost(), uri.getPort(), null, null, null);
        } catch (URISyntaxException e) {
            throw new AssertionError(e);
        }
    }

}
