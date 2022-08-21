/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cks.encrypt.encryption;

import com.cks.encrypt.encryption.RSAEncrypter.KeyType;
import com.cks.encrypt.helper.ArraysHelper;
import com.cks.encrypt.io.FileIO;
import java.util.logging.Logger;
import java.io.IOException;
import java.nio.file.Path;
import java.security.GeneralSecurityException;
import java.security.Key;
import java.security.SecureRandom;
import java.util.Arrays;
import java.util.Base64;
import java.util.Base64.Decoder;
import java.util.Base64.Encoder;
import java.util.logging.Level;
import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

/**
 *
 * @author colin.saldanha
 */
public class AESEncrypter {

    private static final String AES = "AES";
    private static final String CIPHER_ALGO = "AES/CBC/PKCS5Padding";

    private static final Logger LOGGER = Logger.getLogger("com.cks");

    private Key keySpec;
    private IvParameterSpec ivSpec;

    public void generateKey() throws GeneralSecurityException {
        KeyGenerator keyGen = KeyGenerator.getInstance(AES);
        SecureRandom secRand = new SecureRandom();
        keyGen.init(128, secRand);
        keySpec = keyGen.generateKey();
    }

    public void generateIv() throws GeneralSecurityException {
        SecureRandom secRand = new SecureRandom();
        byte[] iv = new byte[16];
        secRand.nextBytes(iv);
        ivSpec = new IvParameterSpec(iv);
    }

    public void loadKey(Path aesKeyFile) throws IOException, GeneralSecurityException {
        byte[] data = FileIO.read(aesKeyFile);
        byte[] ivBytes = Arrays.copyOfRange(data, 0, 16);
        byte[] keyBytes = Arrays.copyOfRange(data, 16, data.length);
        Arrays.fill(data, (byte) 0);
        ivSpec = new IvParameterSpec(ivBytes);
        keySpec = new SecretKeySpec(keyBytes, AES);
        LOGGER.log(Level.INFO, "AES key has been loaded");
    }

    public void loadKey(Path aesKeyFile, Path rsaKeyFile, KeyType rsaKeyType) throws IOException, GeneralSecurityException {
        RSAEncrypter rsa = new RSAEncrypter();
        Decoder decoder = Base64.getDecoder();
        byte[] data = rsa.decrypt(decoder.decode(FileIO.read(aesKeyFile)), rsaKeyFile, rsaKeyType);
        byte[] iv = Arrays.copyOfRange(data, 0, 16);
        byte[] key = Arrays.copyOfRange(data, 16, data.length);
        Arrays.fill(data, (byte) 0);
        ivSpec = new IvParameterSpec(iv);
        keySpec = new SecretKeySpec(key, AES);
    }

    public void savekey(Path aesKeyFile) throws IOException, GeneralSecurityException {
        byte[] data = createKeyDataArray();
        FileIO.write(aesKeyFile, data);
        LOGGER.log(Level.INFO, "AES key has been saved to {0}.", aesKeyFile.getFileName().toString());
    }

    public void saveKey(Path aesKeyFile, Path rsaKeyFile, KeyType rsaKeyType) throws IOException, GeneralSecurityException {
        RSAEncrypter rsa = new RSAEncrypter();
        Encoder encoder = Base64.getEncoder();
        byte[] data = createKeyDataArray();
        FileIO.write(aesKeyFile, encoder.encode(rsa.encrypt(data, rsaKeyFile, rsaKeyType))); //write to key file
        LOGGER.log(Level.INFO, "AES key has been encrypted, encoded and written to {0}", aesKeyFile.getFileName().toString());
    }

    private byte[] createKeyDataArray() throws IOException, GeneralSecurityException {
        if (keySpec == null) {
            throw new GeneralSecurityException("No key found.");
        }

        if (ivSpec == null) {
            throw new GeneralSecurityException("No initialization vector found.");
        }

        byte[] keyBytes = keySpec.getEncoded();
        byte[] ivBytes = ivSpec.getIV();
        byte[] data = new byte[(ivBytes.length + keyBytes.length)];
        ArraysHelper.copyBytes(data, 0, ivBytes, 0, ivBytes.length);
        ArraysHelper.copyBytes(data, ivBytes.length, keyBytes, 0, keyBytes.length);
        return data;
    }

    public byte[] encrypt(byte[] plainBytes) throws GeneralSecurityException {
        LOGGER.info("Starting encryption...");
        Cipher cipher = Cipher.getInstance(CIPHER_ALGO);
        cipher.init(Cipher.ENCRYPT_MODE, keySpec, ivSpec);
        return cipher.doFinal(plainBytes);
    }

    public byte[] decrypt(byte[] cipherBytes) throws GeneralSecurityException {
        LOGGER.info("Starting decryption...");
        Cipher cipher = Cipher.getInstance(CIPHER_ALGO);
        cipher.init(Cipher.DECRYPT_MODE, keySpec, ivSpec);
        return cipher.doFinal(cipherBytes);
    }
}
