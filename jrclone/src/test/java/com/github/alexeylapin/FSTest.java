package com.github.alexeylapin;

import com.github.alexeylapin.fs.CryptFileSystem;
import com.google.common.jimfs.Jimfs;
import org.junit.jupiter.api.Test;

import java.nio.file.FileSystem;
import java.nio.file.Path;
import java.nio.file.Paths;

public class FSTest {

    @Test
    void name1() {
//        Paths.get(URI.create("rclone-crypt:///qwer/asd/zxc"));
        Path rootPath = Paths.get("C:\\Users\\ALapin\\stuff\\rclone-test\\crypt-1");
        CryptFileSystem fs = new CryptFileSystem(rootPath);
        fs.getPath("C:/dir1");
    }

    @Test
    void name2() {
        FileSystem fileSystem = Jimfs.newFileSystem();
        Path path = fileSystem.getPath("bjhjh");
        path.toAbsolutePath();
    }

}
