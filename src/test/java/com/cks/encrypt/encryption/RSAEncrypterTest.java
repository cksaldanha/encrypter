/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cks.encrypt.encryption;

import com.cks.encrypt.encryption.RSAEncrypter.KeyType;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.Key;
import org.junit.AfterClass;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.BeforeClass;
import static org.mockito.Mockito.*;

/**
 *
 * @author colin.saldanha
 */
public class RSAEncrypterTest {
    
    Key keyMock;
    
    byte[] keyStub;

    @BeforeClass
    public static void GenerateKeyFiles() throws Exception {
        RSAEncrypter encrypter = new RSAEncrypter();
        encrypter.generateKeyPair();
        encrypter.savePrivateKey(Paths.get("private.key"));
        encrypter.savePublicKey(Paths.get("public.key"));
    }

    @Before
    public void setup() {
        keyMock = mock(Key.class);
        keyStub = "sample key bytes".getBytes();
    }

    @AfterClass
    public static void RemoveKeyFiles() throws Exception {
        Files.deleteIfExists(Paths.get("private.key"));
        Files.deleteIfExists(Paths.get("public.key"));
    }

    @Test
    public void testGenerateKeyPair_Simple1() throws Exception {
        RSAEncrypter encrypter = new RSAEncrypter();
        encrypter.generateKeyPair();
        byte[] privateKey = encrypter.getPrivateKey().getEncoded();
        byte[] publicKey = encrypter.getPublicKey().getEncoded();

        assertNotNull(privateKey);
        assertNotNull(publicKey);
    }

    @Test
    public void testEncrypt_withPublicKey() throws Exception {
        RSAEncrypter encrypter = new RSAEncrypter();

        String msg = "This is a test message";
        byte[] plainBytes = msg.getBytes();
        byte[] cipherBytes = encrypter.encrypt(plainBytes, Paths.get("public.key"), KeyType.PUBLIC);
        assertNotNull(cipherBytes);
        assertNotEquals(cipherBytes, plainBytes);
        assertNotEquals(cipherBytes.length, plainBytes.length);
    }

    @Test
    public void testEncrypt_withPrivateKey() throws Exception {
        RSAEncrypter encrypter = new RSAEncrypter();
        String msg = "This is a test message";
        byte[] plainBytes = msg.getBytes();
        byte[] cipherBytes = encrypter.encrypt(plainBytes, Paths.get("private.key"), KeyType.PRIVATE);
        assertNotNull(cipherBytes);
        assertNotEquals(cipherBytes, plainBytes);
        assertNotEquals(cipherBytes.length, plainBytes.length);
    }

    @Test
    public void testDecrypt_withPublicKey() throws Exception {
        //Arrange
        RSAEncrypter encrypter = new RSAEncrypter();
        String msg = "This is a plain message";
        byte[] msgBytes = msg.getBytes();
        byte[] cipherBytes = encrypter.encrypt(msgBytes, Paths.get("private.key"), KeyType.PRIVATE);
        //Act
        byte[] plainBytes = encrypter.decrypt(cipherBytes, Paths.get("public.key"), KeyType.PUBLIC);
        //Assert
        assertNotNull(plainBytes);
        assertEquals(msg, new String(plainBytes));
        assertEquals(plainBytes.length, msgBytes.length);
    }

    @Test
    public void testDecrypt_withPrivateKey() throws Exception {
        //Arrange
        RSAEncrypter encrypter = new RSAEncrypter();
        String msg = "This is a plain message";
        byte[] msgBytes = msg.getBytes();
        byte[] cipherBytes = encrypter.encrypt(msgBytes, Paths.get("public.key"), KeyType.PUBLIC);
        //Act
        byte[] plainBytes = encrypter.decrypt(cipherBytes, Paths.get("private.key"), KeyType.PRIVATE);
        //Assert
        assertNotNull(plainBytes);
        assertEquals(msg, new String(plainBytes));
        assertEquals(plainBytes.length, msgBytes.length);
    }
    @Test
    public void testSavePublicKey_Simple1() throws Exception {
        Path testFilePath = Paths.get("testFilePath.txt");
        if (Files.exists(testFilePath))
            fail(String.format("File %s already exists. Cannot test.", testFilePath.getFileName().toString()));
        
        RSAEncrypter rsa = spy(RSAEncrypter.class);
        when(keyMock.getEncoded()).thenReturn(keyStub);
        when(rsa.getPublicKey()).thenReturn(keyMock);
        rsa.savePublicKey(testFilePath);
        assertTrue(Files.exists(testFilePath));
        assertTrue(Files.size(testFilePath) > 0);
        Files.delete(testFilePath);  //remove after assertion
    }
    @Test
    public void testSavePrivateKey_Simple1() throws Exception {
        Path testFilePath = Paths.get("testFilePath.txt");
        if (Files.exists(testFilePath))
            fail(String.format("File %s already exists. Cannot test.", testFilePath.getFileName().toString()));
        
        RSAEncrypter rsa = spy(RSAEncrypter.class);
        when(keyMock.getEncoded()).thenReturn(keyStub);
        when(rsa.getPrivateKey()).thenReturn(keyMock);
        rsa.savePrivateKey(testFilePath);
        assertTrue(Files.exists(testFilePath));
        assertTrue(Files.size(testFilePath) > 0);
        Files.delete(testFilePath);  //remove after assertion
    }
}
