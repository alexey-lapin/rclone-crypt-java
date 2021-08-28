package com.github.alexeylapin;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledOnOs;
import org.junit.jupiter.api.condition.OS;

import java.nio.file.Path;
import java.nio.file.Paths;

import static org.assertj.core.api.Assertions.assertThat;

@EnabledOnOs(OS.WINDOWS)
class DefaultRemoteRefTest {

    @Test
    void name() {
        String dir = System.getProperty("user.dir");
        Path path = Paths.get(dir);
        Path root = path.getRoot();
        boolean directory = root.toFile().exists();
        System.out.println();
    }

    @Test
    void name2() {
        String dir = "d:asdf/zcv";
        Path path = Paths.get(dir);
        Path root = path.getRoot();
        boolean directory = root.toFile().exists();
        System.out.println();
    }

    @Test
    void name3() {
        DefaultRemoteRef remoteRef = new DefaultRemoteRef("");

        //fail
    }

    @Test
    void name4() {
        DefaultRemoteRef remoteRef = new DefaultRemoteRef("qwer");

        assertThat(remoteRef.getName()).isEmpty();
        assertThat(remoteRef.getPath()).isEqualTo("qwer");
    }

    @Test
    void name5() {
        DefaultRemoteRef remoteRef = new DefaultRemoteRef("qwer/zxvc/asdf");

        assertThat(remoteRef.getName()).isEmpty();
        assertThat(remoteRef.getPath()).isEqualTo("qwer/zxvc/asdf");
    }

    @Test
    void name6() {
        DefaultRemoteRef remoteRef = new DefaultRemoteRef("ccc:qwer/zxvc/asdf");

        assertThat(remoteRef.getName()).contains("ccc");
        assertThat(remoteRef.getPath()).isEqualTo("qwer/zxvc/asdf");
    }

    @Test
    void name7() {
        DefaultRemoteRef remoteRef = new DefaultRemoteRef("c:qwer/zxvc/asdf");

        assertThat(remoteRef.getName()).isEmpty();
        assertThat(remoteRef.getPath()).isEqualTo("c:qwer/zxvc/asdf");
    }

}