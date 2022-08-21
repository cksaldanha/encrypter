/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cks.encrypt.cli;

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
        command.addFlag(new Flag("filename", "aes.key"));
        command.execute();
        assertNotNull(Paths.get("aes.key"));
        Files.deleteIfExists(Paths.get("aes.key"));
    }

    @Test
    public void testExecute_whenNoFileNameFlag() {
        try {
            Command command = new AESCommand();
            command.execute();
            fail("Exception not thrown");
        } catch (Exception x) {
        }
    }
}
