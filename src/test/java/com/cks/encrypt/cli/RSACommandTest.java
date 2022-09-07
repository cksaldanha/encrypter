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
public class RSACommandTest {

    @Test
    public void testExecute_whenValidFileNameFlagsProvided() throws Exception {
        Flag publicFlag = new KeyValueFlag("public", "publicKey");
        Flag privateFlag = new KeyValueFlag("private", "privateKey");
        RSACommand command = new RSACommand(publicFlag, privateFlag);
        command.execute();
        assertNotNull(Paths.get("publicKey"));
        assertNotNull(Paths.get("privateKey"));
        Files.deleteIfExists(Paths.get("publicKey"));
        Files.deleteIfExists(Paths.get("privateKey"));
    }

    @Test
    public void testExecute_whenNoFileNameFlagsProvided() throws Exception {
        RSACommand command = new RSACommand();
        command.execute();
        assertNotNull(Paths.get("rsaPublic.key"));
        assertNotNull(Paths.get("rsaPrivate.key"));
        Files.deleteIfExists(Paths.get("rsaPublic.key"));
        Files.deleteIfExists(Paths.get("rsaPrivate.key"));
    }

}
