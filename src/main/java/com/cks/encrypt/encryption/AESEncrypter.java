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
        keyGen.init(256, secRand);
        keySpec = keyGen.generateKey();
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

    public void saveKey(Path aesKeyFile, Path rsaKeyFile, KeyType rsaKeyType) throws IOException, GeneralSecurityException {
        if (keySpec == null) {
            throw new GeneralSecurityException("No key found.");
        }

        if (ivSpec == null) {
            throw new GeneralSecurityException("No initialization vector found.");
        }

        RSAEncrypter rsa = new RSAEncrypter();
        Encoder encoder = Base64.getEncoder();
        byte[] keyBytes = keySpec.getEncoded();
        byte[] iv = ivSpec.getIV();
        byte[] data = new byte[ivSpec.getIV().length + keySpec.getEncoded().length];
        ArraysHelper.copyBytes(data, 0, iv, 0, iv.length);
        ArraysHelper.copyBytes(data, iv.length, keyBytes, 0, keyBytes.length);
        FileIO.write(aesKeyFile, encoder.encode(data)); //write to key file
        LOGGER.info("AES key has been encrypted, encoded and written to " + aesKeyFile.getFileName().toString());
    }

    public byte[] encrypt(byte[] plainBytes) throws GeneralSecurityException {
        return null;
    }

    public byte[] decrypt(byte[] cipherBytes) throws GeneralSecurityException {
        return null;
    }
}
