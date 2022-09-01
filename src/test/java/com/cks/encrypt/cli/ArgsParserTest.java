/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cks.encrypt.cli;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author colin.saldanha
 */
public class ArgsParserTest {

    @Test
    public void testParseArgs_encryptWithAESAndSingleFileFullPath() {
        Command command = ArgsParser.parseArgs(new String[]{"encrypt", "--mode=aes", "--keypath=aes.key", "C:\\temp\\test.pdf"});
        assertNotNull(command);
        assertEquals(command.getCommand(), "encrypt");
        assertTrue(command.getFlagCount() == 3);
    }

    @Test
    public void testParseArgs_encryptWithAESAndMultipleFilesRelativePath() {
        Command command = ArgsParser.parseArgs(new String[]{"encrypt", "--mode=aes", "--keypath=aes.key", "file1.pdf", "file2.pdf", "file3.pdf"});
        assertNotNull(command);
        assertEquals(command.getCommand(), "encrypt");
        assertTrue(command.getFlagCount() == 3);
    }

    @Test
    public void testParseArgs_decryptWithRSAAndSingleFileRelativePath() {
        Command command = ArgsParser.parseArgs(new String[]{"decrypt", "--mode=rsa", "--keypath=rsaPublic.key", "--type=public", "file1.pdf"});
        assertNotNull(command);
        assertEquals(command.getCommand(), "decrypt");
        assertTrue(command.getFlagCount() == 4);
    }

    @Test
    public void testParseArgs_rsaWithKeyFiles() {
        Command command = ArgsParser.parseArgs(new String[]{"rsa", "--public=filepath1", "--private=filepath2"});
        assertNotNull(command);
        assertEquals(command.getCommand(), "rsa");
        assertTrue(command.getFlagCount() == 2);
    }

    @Test
    public void testParseArgs_aesWithNoKeyFile() {
        Command command = ArgsParser.parseArgs(new String[]{"aes"});
        assertNotNull(command);
        assertEquals(command.getCommand(), "aes");
        assertTrue(command.getFlagCount() == 0);
    }

    @Test
    public void testParseArgs_rsaWithFilenamesNoPeriod() {
        String[] args = new String[]{"rsa", "--public=rsaPublic", "--private=rsaPrivate"};
        Command command = ArgsParser.parseArgs(args);
        assertEquals("rsa", command.getCommand());
        assertTrue(command.getFlagCount() == 2);
        assertTrue(command.getFlag("public").getValue().isPresent());
        assertTrue(command.getFlag("private").getValue().isPresent());
    }

    @Test
    public void testParseArgs_rsaWithFilenamesWithPeriods() {
        String[] args = new String[]{"rsa", "--public=rsaPublic.key", "--private=rsaPrivate.key"};
        Command command = ArgsParser.parseArgs(args);
        assertEquals("rsa", command.getCommand());
        assertTrue(command.getFlagCount() == 2);
        assertTrue(command.getFlag("public").getValue().isPresent());
        assertTrue(command.getFlag("private").getValue().isPresent());
    }

    @Test
    public void testFormCommandLine_withAesCommandAndSpaces() {
        String[] commandArgs = new String[]{"aes  ", "--key=value", "file.key "};
        String expected = "aes --key=value file.key\n";
        String actual = ArgsParser.getCommandLine(commandArgs);
        assertEquals(expected, actual);
    }
}
