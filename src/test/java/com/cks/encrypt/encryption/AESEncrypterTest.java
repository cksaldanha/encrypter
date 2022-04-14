/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cks.encrypt.encryption;

import com.cks.encrypt.io.FileIO;
import java.lang.reflect.Field;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.Key;
import java.security.SecureRandom;
import java.security.spec.KeySpec;
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
    public void testLoadKey_Simple1() throws Exception {
        AESEncrypter aes = new AESEncrypter();
        aes.loadKey(Paths.get("aes_key.txt"), Paths.get("private.txt"), RSAEncrypter.KeyType.PRIVATE);
        Field key = AESEncrypter.class.getDeclaredField("keySpec");
        key.setAccessible(true);
        assertNotNull(key.get(aes));
        
        Field iv = AESEncrypter.class.getDeclaredField("ivSpec");
        iv.setAccessible(true);
        assertNotNull(key.get(aes));
    }

    @Test
    public void testSaveKey_Simple1() throws Exception {
        AESEncrypter aes = new AESEncrypter();
        aes.generateKey();
        aes.generateIv();
        aes.saveKey(Paths.get("aes_key.txt"), Paths.get("public.txt"), RSAEncrypter.KeyType.PUBLIC);
        assertTrue(Files.exists(Paths.get("aes_key.txt")));
    }

    @Test
    public void testEncrypt_Simple1() throws Exception {
        AESEncrypter aes = new AESEncrypter();
        aes.generateKey();
        aes.generateIv();
        byte[] cipherBytes = aes.encrypt("Simple text message".getBytes());
        assertNotNull(cipherBytes);
    }

    @Test
    public void testEncrypt_Simple2_withFile() throws Exception {
        AESEncrypter aes = new AESEncrypter();
        aes.generateKey();
        aes.generateIv();
        aes.saveKey(Paths.get("aes_key.txt"), Paths.get("public.txt"), RSAEncrypter.KeyType.PUBLIC);
        byte[] cipherBytes = aes.encrypt("Simple text message".getBytes());
        //write to file
        FileIO.write(Paths.get("simple.txt"), cipherBytes);
        assertNotNull(cipherBytes);
    }
}
