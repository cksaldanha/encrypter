/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cks.encrypt.cli;

import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import org.junit.AfterClass;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.BeforeClass;

/**
 *
 * @author colin.saldanha
 */
public class EncryptCommandTest {

    @BeforeClass
    public static void GenerateKeysAndPlainFile() throws Exception {
        AESCommand aesCommand = new AESCommand();
        aesCommand.execute();

        RSACommand rsaCommand = new RSACommand();
        rsaCommand.execute();

        try (Writer writer = Files.newBufferedWriter(Paths.get("simple.txt"))) {
            writer.write("This is a sample sentence");
        }
    }

    @AfterClass
    public static void DeleteKeysAndPlainFile() throws Exception {
        Files.deleteIfExists(Paths.get("aes.key"));
        Files.deleteIfExists(Paths.get("rsaPublic.key"));
        Files.deleteIfExists(Paths.get("rsaPrivate.key"));
        Files.deleteIfExists(Paths.get("simple.txt"));
    }

    @Test
    public void testGetCommand_noFlagsProvided() {
        try {
            Command command = new EncryptCommand();
            assertEquals("encrypt", command.getCommand());
            fail("Exception was supposed to occur");
        } catch (Exception x) {
        }
    }

    @Test
    public void testGetCommand_withProperAESFlag() {
        Flag mode = new Flag("mode", "aes");
        Command command = new EncryptCommand(new Flag[]{mode});
        assertEquals("encrypt", command.getCommand());
    }

    @Test
    public void testGetCommand_withProperRSAFlagsPublic() {
        Flag mode = new Flag("mode", "rsa");
        Flag publicFlag = new Flag("type", "public");
        Command command = new EncryptCommand(new Flag[]{mode, publicFlag});
        assertEquals("encrypt", command.getCommand());
    }

    @Test
    public void testGetCommand_withProperRSAFlagsPrivate() {
        Flag mode = new Flag("mode", "rsa");
        Flag publicFlag = new Flag("type", "private");
        Command command = new EncryptCommand(new Flag[]{mode, publicFlag});
        assertEquals("encrypt", command.getCommand());
    }

    @Test
    public void testExecute_withAESmode() throws Exception {
        //Arrange
        try {
            Flag mode = new Flag("mode", "aes");
            Flag keypath = new Flag("keypath", "aes.key");
            List<String> fileNames = new ArrayList<>();
            fileNames.add("simple.txt");
            Flag files = new Flag("files", fileNames);
            EncryptCommand command = new EncryptCommand(mode, keypath, files);

            //Action
            command.execute();

            //Assert
            assertTrue(Files.exists(Paths.get("simple.secure")));
            assertTrue(Files.size(Paths.get("simple.secure")) > 0);

        } finally {
            Files.deleteIfExists(Paths.get("simple.secure"));
        }
    }

    @Test
    public void testExecute_withRSAmode() throws Exception {
        //Arrange
        try {
            Flag mode = new Flag("mode", "rsa");
            Flag keypath = new Flag("keypath", "rsaPublic.key");
            Flag type = new Flag("type", "public");
            List<String> fileNames = new ArrayList<>();
            fileNames.add("simple.txt");
            Flag files = new Flag("files", fileNames);
            EncryptCommand command = new EncryptCommand(mode, keypath, type, files);

            //Action
            command.execute();

            //Assert
            assertTrue(Files.exists(Paths.get("simple.secure")));
            assertTrue(Files.size(Paths.get("simple.secure")) > 0);

        } finally {
            Files.deleteIfExists(Paths.get("simple.secure"));
        }
    }
}
