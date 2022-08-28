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
    public void testParseArgs_Simple1() {
        Command command = ArgsParser.parseArgs(new String[] {"encrypt", "none", "rsa_key", "C:\\users\\colin.saldanha\\test.pdf"});
        assertNotNull(command);
        assertEquals(command.getCommand(), "encrypt");
        assertTrue(command.getFlagCount() == 3);
    }
    @Test
    public void testParseArgs_Simple2() {
        Command command = ArgsParser.parseArgs(new String[] {"encrypt", "aes_key_path", "rsa_key_path", "C:\\users\\colin.saldanha\\test.pdf"});
        assertNotNull(command);
        assertEquals(command.getCommand(), "encrypt");
        assertTrue(command.getFlagCount() == 3);
    }
    @Test
    public void testParseArgs_Simple3() {
        Command command = ArgsParser.parseArgs(new String[] {"decrypt", "aes_key_path", "rsa_key_path", "C:\\users\\colin.saldanha\\test.pdf"});
        assertNotNull(command);
        assertEquals(command.getCommand(), "decrypt");
        assertTrue(command.getFlagCount() == 3);
    }
    @Test
    public void testParseArgs_Simple4() {
        Command command = ArgsParser.parseArgs(new String[] {"rsa", "filepath1", "filepath2"});
        assertNotNull(command);
        assertEquals(command.getCommand(), "rsa");
        assertTrue(command.getFlagCount() == 2);
    }
    @Test
    public void testParseArgs_Simple5() {
        Command command = ArgsParser.parseArgs(new String[] {"encrypt", "none", "rsa_key_path", "filepath1", "filepath2", "filepath3", "filepath4"});
        assertNotNull(command);
        assertEquals(command.getCommand(), "encrypt");
        assertTrue(command.getFlagCount() == 6);
    }

    @Test
    public void testFormCommandLine_withAesCommandAndSpaces() {
        String[] commandArgs = new String[]{"aes  ", "--key=value", "file.key "};
        String expected = "aes --key=value file.key\n";
        String actual = ArgsParser.getCommandLine(commandArgs);
        assertEquals(expected, actual);
    }
}
