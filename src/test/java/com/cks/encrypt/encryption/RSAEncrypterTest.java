/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cks.encrypt.encryption;

import com.cks.encrypt.encryption.RSAEncrypter.KeyType;
import java.lang.reflect.Field;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.GeneralSecurityException;
import java.security.Key;
import java.security.SecureRandom;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Before;
import org.mockito.Mockito;
import static org.mockito.Mockito.*;

/**
 *
 * @author colin.saldanha
 */
public class RSAEncrypterTest {
    
    Key keyMock;
    
    byte[] keyStub;
    
    @Before
    public void setup() {
        keyMock = mock(Key.class);
        keyStub = "sample key bytes".getBytes();
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
    public void testEncrypt_Simple1() throws GeneralSecurityException {
        Path keyFile = Paths.get("public.txt");
        RSAEncrypter encrypter = new RSAEncrypter();
        String msg = "This is a test message";
        byte[] plainBytes = msg.getBytes();
        byte[] cipherBytes = encrypter.encrypt(plainBytes, keyFile, KeyType.PUBLIC);
        assertNotNull(cipherBytes);
        assertNotEquals(cipherBytes, plainBytes);
    }

    @Test
    public void testDecrypt_Simple1() throws Exception {
        Path keyFile = Paths.get("public.txt");
        RSAEncrypter encrypter = new RSAEncrypter();
        String msg = "This is a test message";
        byte[] plainBytes = msg.getBytes(Charset.defaultCharset());
        byte[] cipherBytes = encrypter.encrypt(plainBytes, keyFile, KeyType.PUBLIC);

        byte[] decryptBytes = encrypter.decrypt(cipherBytes, Paths.get("private.txt"), KeyType.PRIVATE);
        assertEquals(new String(plainBytes), new String(decryptBytes));
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
