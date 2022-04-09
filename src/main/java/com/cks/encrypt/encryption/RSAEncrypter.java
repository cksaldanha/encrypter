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
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
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
    
    enum KeyType { PUBLIC, PRIVATE }
    
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
    
    public byte[] getPrivateKey() {
        return privateKey.getEncoded();
    }
    
    public byte[] getPublicKey() {
        return publicKey.getEncoded();
    }

    public byte[] encrypt(byte[] plainBytes, Path keyfile, KeyType keyType) throws GeneralSecurityException {
        try {
            byte[] keyBytes = FileIO.read(keyfile);
            byte[] decodedKeyBytes = Base64.getDecoder().decode(keyBytes);
            
            KeyFactory keyFactory = KeyFactory.getInstance(RSA);
            EncodedKeySpec keySpec = new X509EncodedKeySpec(decodedKeyBytes);
            Key key = keyType == KeyType.PUBLIC ? keyFactory.generatePublic(keySpec) : keyFactory.generatePrivate(keySpec);
            
            Cipher cipher = Cipher.getInstance(RSA);
            cipher.init(Cipher.ENCRYPT_MODE, key);
            return cipher.doFinal(plainBytes);
            
        } catch (IOException x) {
            LOGGER.severe("Could not read from key file.");
            LOGGER.fine(x.getMessage());
            throw new GeneralSecurityException("Could not read from key file.");
        }
    }

    public byte[] decrypt(byte[] cipherBytes, Path keyfile, KeyType keyType) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
