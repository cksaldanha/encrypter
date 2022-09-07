/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cks.encrypt.cli;

import com.cks.encrypt.cli.flag.FilesFlag;
import com.cks.encrypt.cli.flag.KeyValueFlag;
import com.cks.encrypt.cli.flag.Flag;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Paths;
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
        Flag mode = new KeyValueFlag("mode", "aes");
        Command command = new EncryptCommand(new Flag[]{mode});
        assertEquals("encrypt", command.getCommand());
    }

    @Test
    public void testGetCommand_withProperRSAFlagsPublic() {
        Flag mode = new KeyValueFlag("mode", "rsa");
        Flag publicFlag = new KeyValueFlag("type", "public");
        Command command = new EncryptCommand(new Flag[]{mode, publicFlag});
        assertEquals("encrypt", command.getCommand());
    }

    @Test
    public void testGetCommand_withProperRSAFlagsPrivate() {
        Flag mode = new KeyValueFlag("mode", "rsa");
        Flag publicFlag = new KeyValueFlag("type", "private");
        Command command = new EncryptCommand(new Flag[]{mode, publicFlag});
        assertEquals("encrypt", command.getCommand());
    }

    @Test
    public void testExecute_withAESmode() throws Exception {
        //Arrange
        try {
            Flag mode = new KeyValueFlag("mode", "aes");
            Flag keypath = new KeyValueFlag("keypath", "aes.key");
            Flag files = new FilesFlag(Paths.get("simple.txt"));
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
            Flag mode = new KeyValueFlag("mode", "rsa");
            Flag keypath = new KeyValueFlag("keypath", "rsaPublic.key");
            Flag type = new KeyValueFlag("type", "public");
            Flag files = new FilesFlag(Paths.get("simple.txt"));
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
