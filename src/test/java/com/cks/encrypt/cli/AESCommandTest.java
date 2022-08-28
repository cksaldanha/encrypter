/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cks.encrypt.cli;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
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
        List<String> fileNames = new ArrayList<>();
        fileNames.add("aes.key");
        Flag filesFlag = new Flag("files", fileNames);
        command.addFlag(filesFlag);
        command.execute();
        assertNotNull(Paths.get("aes.key"));
        Files.deleteIfExists(Paths.get("aes.key"));
    }

    @Test
    public void testExecute_whenMultipleFilesSpecified() throws Exception {
        Command command = new AESCommand();
        List<String> fileNames = new ArrayList<>();
        fileNames.add("file1");
        fileNames.add("file2");
        Flag filesFlag = new Flag("files", fileNames);
        command.addFlag(filesFlag);
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
