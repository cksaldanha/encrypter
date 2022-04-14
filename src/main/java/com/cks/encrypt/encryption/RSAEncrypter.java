/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cks.encrypt.encryption;

import com.cks.encrypt.io.FileIO;
import java.io.IOException;
import java.nio.file.Path;
import java.security.GeneralSecurityException;
import java.security.Key;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.EncodedKeySpec;
import java.security.spec.KeySpec;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import java.util.Base64.Encoder;
import java.util.logging.Logger;
import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;

/**
 *
 * @author colin.saldanha
 */
public class RSAEncrypter {

    private static final Logger LOGGER = Logger.getLogger("com.cks");
    private static final String RSA = "RSA";
    
    private PrivateKey privateKey;
    private PublicKey publicKey;
    
    public enum KeyType { PUBLIC, PRIVATE }
    
    public void generateKeyPair() throws NoSuchAlgorithmException {
        try {
            KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance(RSA);
            keyPairGenerator.initialize(2048);
            KeyPair keyPair = keyPairGenerator.generateKeyPair();
            privateKey = keyPair.getPrivate();
            LOGGER.info("PrivateKeyClass: " + privateKey.getClass().getName());
            publicKey = keyPair.getPublic();
            LOGGER.info("PublicKeyClass: " + privateKey.getClass().getName());
            
            LOGGER.fine("Private: " + DatatypeConverter.printHexBinary(privateKey.getEncoded()));
            LOGGER.fine("Public: " + DatatypeConverter.printHexBinary(publicKey.getEncoded()));
            
        } catch (NoSuchAlgorithmException x) {
            LOGGER.severe(x.getMessage());
            throw new RuntimeException("Could not use RSA Algorithm: " + x.getMessage());
        }
    }
    
    public Key getPrivateKey() {
        return privateKey;
    }
    
    public Key getPublicKey() {
        return publicKey;
    }
    
    public void savePrivateKey(Path filepath) throws IOException {
        saveKeyToFile(getPrivateKey(), filepath);
    }
    
    public void savePublicKey(Path filepath) throws IOException {
        saveKeyToFile(getPublicKey(), filepath);
    }
    
    private void saveKeyToFile(Key key, Path filepath) throws IOException {
        byte[] keyBytes = key.getEncoded();
        Encoder encoder = Base64.getEncoder();
        FileIO.write(filepath, encoder.encode(keyBytes));
    }

    public byte[] encrypt(byte[] plainBytes, Path keyFile, KeyType keyType) throws GeneralSecurityException {
        try {
            Key key = extractKey(keyFile, keyType);
            Cipher cipher = Cipher.getInstance(RSA);
            cipher.init(Cipher.ENCRYPT_MODE, key);
            return cipher.doFinal(plainBytes);
            
        } catch (IOException x) {
            LOGGER.severe("Could not read from key file.");
            LOGGER.fine(x.getMessage());
            throw new GeneralSecurityException("Could not read from key file.");
        }
    }

    private Key extractKey(Path keyFile, KeyType keyType) throws IOException, GeneralSecurityException {

        byte[] keyBytes = FileIO.read(keyFile);
        byte[] decodedKeyBytes = Base64.getDecoder().decode(keyBytes);

        KeyFactory keyFactory = KeyFactory.getInstance(RSA);
        Key key = null;
        if (keyType == KeyType.PUBLIC) {
            EncodedKeySpec keySpec = new X509EncodedKeySpec(decodedKeyBytes);
            key = keyFactory.generatePublic(keySpec);
        } else {
            EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(decodedKeyBytes);
            key = keyFactory.generatePrivate(keySpec);
        }

        return key;
    }

    public byte[] decrypt(byte[] cipherBytes, Path keyFile, KeyType keyType) throws GeneralSecurityException {
        try {
            Key key = extractKey(keyFile, keyType);

            Cipher cipher = Cipher.getInstance(RSA);
            cipher.init(Cipher.DECRYPT_MODE, key);
            return cipher.doFinal(cipherBytes);

        } catch (IOException x) {
            LOGGER.severe("Could not read from key file.");
            LOGGER.fine(x.getMessage());
            throw new GeneralSecurityException("Could not read from key file.");
        }
    }
}
