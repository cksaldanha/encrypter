/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cks.encrypt.encryption;

import com.cks.encrypt.cli.AESCommand;
import com.cks.encrypt.cli.Command;
import java.lang.reflect.Field;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author colin.saldanha
 */
public class AESEncrypterTest {
    
    @Test
    public void testGenerateKey_Simple1() throws Exception {
        AESEncrypter aes = new AESEncrypter();
        aes.generateKey();
        Field field = AESEncrypter.class.getDeclaredField("keySpec");
        field.setAccessible(true);
        assertNotNull(field.get(aes));
    }

    @Test
    public void testGenerateIv_Simple1() throws Exception {
        AESEncrypter aes = new AESEncrypter();
        aes.generateIv();
        Field field = AESEncrypter.class.getDeclaredField("ivSpec");
        field.setAccessible(true);
        assertNotNull(field.get(aes));
    }

    @Test
    public void testLoadKey_withSingleKeyFile() throws Exception {
        //Arrange
        Command command = new AESCommand();
        command.execute();  //generates an 'aes.key' file

        AESEncrypter aes = new AESEncrypter();
        //Act
        aes.loadKey(Paths.get("aes.key"));
        //Assert
        Field key = AESEncrypter.class.getDeclaredField("keySpec");
        key.setAccessible(true);
        assertNotNull(key.get(aes));
        
        Field iv = AESEncrypter.class.getDeclaredField("ivSpec");
        iv.setAccessible(true);
        assertNotNull(iv.get(aes));
        //Clean
        Files.deleteIfExists(Paths.get("aes.key"));
    }

    @Test
    public void testSaveKey_NoRSA_NoEncoding() throws Exception {
        AESEncrypter aes = new AESEncrypter();
        aes.generateKey();
        aes.generateIv();
        Path keyFilePath = Paths.get("aes_key");
        aes.savekey(keyFilePath);
        assertTrue(Files.exists(keyFilePath));
        Files.deleteIfExists(keyFilePath);
    }

    @Test
    public void testEncrypt_Simple1() throws Exception {
        AESEncrypter aes = new AESEncrypter();
        aes.generateKey();
        aes.generateIv();
        byte[] cipherBytes = aes.encrypt("Simple text message".getBytes());
        assertNotNull(cipherBytes);
    }
}
