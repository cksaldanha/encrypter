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
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author colin.saldanha
 */
public class DecryptCommandTest {

    @BeforeClass
    public static void GenerateKeysAndCipherFiles() throws Exception {
        AESCommand aesCommand = new AESCommand();
        aesCommand.execute();

        RSACommand rsaCommand = new RSACommand();
        rsaCommand.execute();

        try (Writer writer = Files.newBufferedWriter(Paths.get("simple.txt"))) {
            writer.write("This is a sample sentence");
        }

        Flag files = new FilesFlag(Paths.get("simple.txt"));

        //aes cipher file generation
        Flag mode = new KeyValueFlag("mode", "aes");
        Flag keypath = new KeyValueFlag("keypath", "aes.key");
        EncryptCommand encryptCommand = new EncryptCommand(mode, keypath, files);
        encryptCommand.execute();
        Files.move(Paths.get("simple.secure"), Paths.get("simpleAES.secure"));

        //rsa public cipher file generation
        mode = new KeyValueFlag("mode", "rsa");
        keypath = new KeyValueFlag("keypath", "rsaPublic.key");
        Flag type = new KeyValueFlag("type", "public");
        encryptCommand = new EncryptCommand(mode, keypath, type, files);
        encryptCommand.execute();
        Files.move(Paths.get("simple.secure"), Paths.get("simpleRSAPublic.secure"));

        //rsa private cipher file generation
        keypath = new KeyValueFlag("keypath", "rsaPrivate.key");
        type = new KeyValueFlag("type", "private");
        encryptCommand = new EncryptCommand(mode, keypath, type, files);
        encryptCommand.execute();
        Files.move(Paths.get("simple.secure"), Paths.get("simpleRSAPrivate.secure"));
    }

    @AfterClass
    public static void DeleteKeysAndPlainFile() throws Exception {
        Files.deleteIfExists(Paths.get("aes.key"));
        Files.deleteIfExists(Paths.get("rsaPublic.key"));
        Files.deleteIfExists(Paths.get("rsaPrivate.key"));
        Files.deleteIfExists(Paths.get("simple.txt"));
        Files.deleteIfExists(Paths.get("simpleAES.secure"));
        Files.deleteIfExists(Paths.get("simpleRSAPublic.secure"));
        Files.deleteIfExists(Paths.get("simpleRSAPrivate.secure"));
    }

    @Test
    public void testGetCommand_noFlagsProvided() {
        try {
            Command command = new DecryptCommand();
            assertEquals("decrypt", command.getCommand());
            fail("Exception was supposed to occur");
        } catch (Exception x) {
        }
    }

    @Test
    public void testGetCommand_withProperAESFlag() {
        Flag mode = new KeyValueFlag("mode", "aes");
        Command command = new DecryptCommand(new Flag[]{mode});
        assertEquals("decrypt", command.getCommand());
    }

    @Test
    public void testGetCommand_withProperRSAFlagsPublic() {
        Flag mode = new KeyValueFlag("mode", "rsa");
        Flag publicFlag = new KeyValueFlag("type", "public");
        Command command = new DecryptCommand(new Flag[]{mode, publicFlag});
        assertEquals("decrypt", command.getCommand());
    }

    @Test
    public void testGetCommand_withProperRSAFlagsPrivate() {
        Flag mode = new KeyValueFlag("mode", "rsa");
        Flag publicFlag = new KeyValueFlag("type", "private");
        Command command = new DecryptCommand(new Flag[]{mode, publicFlag});
        assertEquals("decrypt", command.getCommand());
    }

    @Test
    public void testExecute_withAESmode() throws Exception {
        //Arrange
        try {
            Flag mode = new KeyValueFlag("mode", "aes");
            Flag keypath = new KeyValueFlag("keypath", "aes.key");
            Flag files = new FilesFlag(Paths.get("simpleAES.secure"));
            Command command = new DecryptCommand(mode, keypath, files);

            //Action
            command.execute();

            //Assert
            assertTrue(Files.exists(Paths.get("simpleAES.unsecure")));
            assertTrue(Files.size(Paths.get("simpleAES.unsecure")) > 0);

            byte[] plainBytes = Files.readAllBytes(Paths.get("simpleAES.unsecure"));
            String actual = new String(plainBytes);
            assertEquals("This is a sample sentence", actual);

        } finally {
            Files.deleteIfExists(Paths.get("simpleAES.unsecure"));
        }
    }

    @Test
    public void testExecute_withRSAmodePublic() throws Exception {
        //Arrange
        try {
            Flag mode = new KeyValueFlag("mode", "rsa");
            Flag keypath = new KeyValueFlag("keypath", "rsaPublic.key");
            Flag type = new KeyValueFlag("type", "public");
            Flag files = new FilesFlag(Paths.get("simpleRSAPrivate.secure"));
            Command command = new DecryptCommand(mode, keypath, type, files);

            //Action
            command.execute();

            //Assert
            assertTrue(Files.exists(Paths.get("simpleRSAPrivate.unsecure")));
            assertTrue(Files.size(Paths.get("simpleRSAPrivate.unsecure")) > 0);

            byte[] plainBytes = Files.readAllBytes(Paths.get("simpleRSAPrivate.unsecure"));
            String actual = new String(plainBytes);
            assertEquals("This is a sample sentence", actual);

        } finally {
            Files.deleteIfExists(Paths.get("simpleRSAPrivate.unsecure"));
        }
    }

    @Test
    public void testExecute_withRSAmodePrivate() throws Exception {
        //Arrange
        try {
            Flag mode = new KeyValueFlag("mode", "rsa");
            Flag keypath = new KeyValueFlag("keypath", "rsaPrivate.key");
            Flag type = new KeyValueFlag("type", "private");
            Flag files = new FilesFlag(Paths.get("simpleRSAPublic.secure"));
            Command command = new DecryptCommand(mode, keypath, type, files);

            //Action
            command.execute();

            //Assert
            assertTrue(Files.exists(Paths.get("simpleRSAPublic.unsecure")));
            assertTrue(Files.size(Paths.get("simpleRSAPublic.unsecure")) > 0);

            byte[] plainBytes = Files.readAllBytes(Paths.get("simpleRSAPublic.unsecure"));
            String actual = new String(plainBytes);
            assertEquals("This is a sample sentence", actual);

        } finally {
            Files.deleteIfExists(Paths.get("simpleRSAPublic.unsecure"));
        }
    }
}
