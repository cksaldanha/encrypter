/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cks.encrypt;

import com.cks.encrypt.cli.ArgsParser;
import com.cks.encrypt.cli.Command;
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

        //Add command line logic
        try {
            Command command = ArgsParser.parseArgs(args);
            try {
                //process command
                switch (command.getCommand()) {
                    case Command.CMD_ENCRYPT:
                        if (command.getFlagCount() == 2) {  //create new AES key and save with public RSA
                            AESEncrypter aes = new AESEncrypter();
                            aes.generateKey();
                            aes.generateIv();
                            Path file = Paths.get(command.getFlag("filename"));
                            Path key = Paths.get("aes.key");
                            byte[] cipherBytes = aes.encrypt(FileIO.read(file));
                            Path newfile = FileIO.changeFileExtension(file, ".secure");
                            FileIO.write(newfile, cipherBytes);
                            System.out.println("File has been encrypted.");
                            aes.saveKey(key, Paths.get(command.getFlag("rsa_key")), KeyType.PUBLIC);
                        }
                        break;
                    case Command.CMD_RSA:
                        Path rsaPublicKeyFile = Paths.get(command.getFlag("filename1", "public.key"));
                        Path rsaPrivateKeyFile = Paths.get(command.getFlag("filename2", "private.key"));
                        RSAEncrypter rsa = new RSAEncrypter();
                        rsa.generateKeyPair();
                        rsa.savePrivateKey(rsaPrivateKeyFile);
                        rsa.savePublicKey(rsaPublicKeyFile);
                        LOGGER.fine("RSA key pair have been generated");
                        System.out.println("RSA key pair has been generated. Keep private key safe.");
                        break;

                }
            } catch (Exception x) {
                x.printStackTrace();
                System.out.println(x.getMessage());
                throw new IllegalArgumentException(x);
            }

        } catch (IllegalArgumentException x) {
            usage();
            LOGGER.severe(x.getMessage());
        }
    }

    public static void usage() {
        System.out.printf("java encrypter <command> <arguments>\n");
        System.out.printf("\tCommands:\n");
        System.out.printf("\t\tencrypt [aes key file] [rsa key file] <filename>\n");
        System.out.printf("\t\tdecrypt <aes key file> <rsa key file> <filename>\n");
        System.out.printf("\t\trsa [public key file] [private key file]\n");
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
            byte[] privateKeyBytes = rsa.getPrivateKey().getEncoded();
            byte[] privateKeyBytesEncoded = encoder.encode(privateKeyBytes);

            byte[] publicKeyBytes = rsa.getPublicKey().getEncoded();
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
