/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cks.encrypt;

import com.cks.encrypt.encryption.AESEncrypter;
import com.cks.encrypt.encryption.RSAEncrypter;
import com.cks.encrypt.encryption.RSAEncrypter.KeyType;
import com.cks.encrypt.io.FileIO;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.GeneralSecurityException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Base64;
import java.util.Base64.Encoder;
import java.util.logging.*;

/**
 *
 * @author colin.saldanha
 */
public class Application {

    /*
    date
    warn/info
    class
    method
    message
     */
    private static final String ONE_LINE_FORM = "[%s] [%-7s] [%-20s] [%-20s] : %s%n";
    private static final Logger LOGGER = Logger.getLogger("com.cks");

    public static void main(String[] args) {
        //setup logging
        ConsoleHandler consHandler = new ConsoleHandler();
        consHandler.setLevel(Level.ALL);
        consHandler.setFormatter(new SimpleFormatter() {
            @Override
            public String format(LogRecord record) {
                Instant instant = Instant.ofEpochMilli(record.getMillis());
                LocalDateTime ldt = LocalDateTime.ofInstant(instant, ZoneId.systemDefault());
                DateTimeFormatter dtFormatter = DateTimeFormatter.ISO_DATE_TIME;

                return String.format(
                        ONE_LINE_FORM,
                        dtFormatter.format(ldt),
                        record.getLevel(),
                        record.getSourceClassName(),
                        record.getSourceMethodName(),
                        record.getMessage()
                );
            }
        });
        LOGGER.addHandler(consHandler);
        LOGGER.setLevel(Level.ALL);
        LOGGER.setUseParentHandlers(false);
        LOGGER.info("Application started.");

//        LOGGER.fine("Encrypting the test pdf");
//        encryptFile(Paths.get("test.pdf"), Paths.get("aes.key"));
//        LOGGER.fine("Encryption done.");
//        try {
//            Thread.sleep(2000); //sleep 2s
//        } catch (InterruptedException x) { }
        Path encryptedFile = Paths.get("test.pdf.secure");
        LOGGER.fine("Decrypting file " + encryptedFile.getFileName().toString());
        decryptFile(encryptedFile);
        LOGGER.fine("Decryption complete.");
        LOGGER.fine("Exiting.");

//        LOGGER.info("Generating keys");
//        generateKeyFiles();
//        LOGGER.info("Keys created.");
    }

    public static void encryptFile(Path file) {
        try {
            Path temp = Paths.get(file.getFileName().toString() + ".secure");
            byte[] plainBytes = FileIO.read(file);
            AESEncrypter aes = new AESEncrypter();
            aes.generateKey();
            aes.generateIv();
            byte[] cipherBytes = aes.encrypt(plainBytes);
            FileIO.write(temp, cipherBytes);
            aes.saveKey(Paths.get("aes.key"), Paths.get("public.txt"), KeyType.PUBLIC);

        } catch (IOException x) {
            LOGGER.severe("IO error:" + x.getMessage());
        } catch (GeneralSecurityException x) {
            LOGGER.severe("Security Error: " + x.getMessage());
        }
    }

    public static void encryptFile(Path file, Path keyFile) {
        try {
            Path temp = Paths.get(file.getFileName().toString() + ".secure");
            byte[] plainBytes = FileIO.read(file);
            AESEncrypter aes = new AESEncrypter();
            aes.loadKey(keyFile, Paths.get("private.txt"), KeyType.PRIVATE);
            byte[] cipherBytes = aes.encrypt(plainBytes);
            FileIO.write(temp, cipherBytes);

        } catch (IOException x) {
            LOGGER.severe("IO error:" + x.getMessage());
        } catch (GeneralSecurityException x) {
            LOGGER.severe("Security Error: " + x.getMessage());
        }
    }

    public static void decryptFile(Path file) {
        try {
            String path = file.getFileName().toString();
            Path decrypt = Paths.get("decrypt/" + path.substring(0, path.lastIndexOf(".")));
            byte[] cipherBytes = FileIO.read(file);
            AESEncrypter aes = new AESEncrypter();
            aes.loadKey(Paths.get("aes.key"), Paths.get("private.txt"), KeyType.PRIVATE);
            byte[] plainBytes = aes.decrypt(cipherBytes);
            FileIO.write(decrypt, plainBytes);

        } catch (IOException x) {
            LOGGER.severe("IO error: " + x.getMessage());
        } catch (GeneralSecurityException x) {
            LOGGER.severe("Security error: " + x.getMessage());
        }
    }

    public static void generateRSAKeyFiles() {
        RSAEncrypter rsa = new RSAEncrypter();
        try {
            rsa.generateKeyPair();
            Encoder encoder = Base64.getEncoder();
            byte[] privateKeyBytes = rsa.getPrivateKey();
            byte[] privateKeyBytesEncoded = encoder.encode(privateKeyBytes);

            byte[] publicKeyBytes = rsa.getPublicKey();
            byte[] publicKeyBytesEncoded = encoder.encode(publicKeyBytes);

            FileIO.write(Paths.get("private.txt"), privateKeyBytesEncoded);
            FileIO.write(Paths.get("public.txt"), publicKeyBytesEncoded);

        } catch (GeneralSecurityException x) {
            LOGGER.severe(x.getMessage());
        } catch (IOException x) {
            LOGGER.severe(x.getMessage());
        }
    }
}
