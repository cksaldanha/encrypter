/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cks.encrypt.cli.command;

import com.cks.encrypt.cli.command.Command;
import com.cks.encrypt.cli.command.AESCommand;
import com.cks.encrypt.cli.flag.FilesFlag;
import java.nio.file.Files;
import java.nio.file.Paths;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author colin.saldanha
 */
public class AESCommandTest {

    @Test
    public void testExecute_whenValidFileNameFlag() throws Exception {
        Command command = new AESCommand();
        command.addFlag(new FilesFlag(Paths.get("aes.key")));
        command.execute();
        assertNotNull(Paths.get("aes.key"));
        Files.deleteIfExists(Paths.get("aes.key"));
    }

    @Test
    public void testExecute_whenMultipleFilesSpecified() throws Exception {
        Command command = new AESCommand();
        command.addFlag(new FilesFlag(Paths.get("file1"), Paths.get("file2")));
        command.execute();
        assertNotNull(Paths.get("file1"));
        assertNotNull(Paths.get("file2"));
        Files.deleteIfExists(Paths.get("file1"));
        Files.deleteIfExists(Paths.get("file2"));
    }

    @Test
    public void testExecute_whenNoFileNameFlag() throws Exception {
        Command command = new AESCommand();
        command.execute();
        assertNotNull(Paths.get("aes.key"));
        Files.deleteIfExists(Paths.get("aes.key"));
    }
}
