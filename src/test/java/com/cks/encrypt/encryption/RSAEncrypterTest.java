/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cks.encrypt.encryption;

import com.cks.encrypt.encryption.RSAEncrypter.KeyType;
import com.cks.encrypt.io.FileIO;
import java.nio.charset.Charset;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.GeneralSecurityException;
import javax.xml.bind.DatatypeConverter;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author colin.saldanha
 */
public class RSAEncrypterTest {

    @Test
    public void testGenerateKeyPair_Simple1() throws Exception {
        RSAEncrypter encrypter = new RSAEncrypter();
        encrypter.generateKeyPair();
        byte[] privateKey = encrypter.getPrivateKey();
        byte[] publicKey = encrypter.getPublicKey();

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
}
