package com.github.alexeylapin.jrclone;

import com.github.alexeylapin.jrclone.command.ListCommand;
import com.github.alexeylapin.jrclone.command.ListDirCommand;
import picocli.CommandLine;
import picocli.CommandLine.Command;

@Command(name = "jrclone", subcommands = {ListCommand.class, ListDirCommand.class})
public class JRClone {

    public static void main(String[] args) {
        System.exit(new CommandLine(new JRClone()).execute(args));
    }

}
