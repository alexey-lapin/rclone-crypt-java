package com.github.alexeylapin;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

class IniFileTest {

    @Disabled
    @Test
    void name() throws IOException {
        String path = Paths.get(System.getProperty("user.home"), ".config", "rclone", "rclone.conf").toString();
        System.out.println(path);

        IniFile iniFile = new IniFile(path);

        String string = iniFile.getString("gddocs", "remote", "");
        System.out.println(string);
    }

}