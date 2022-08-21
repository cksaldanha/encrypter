/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cks.encrypt.cli;

import com.cks.encrypt.encryption.AESEncrypter;
import com.cks.encrypt.encryption.RSAEncrypter;
import com.cks.encrypt.io.FileIO;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.logging.Logger;

/**
 *
 * @author colin.saldanha
 */
public class RSACommand extends Command {

    private static final Logger LOGGER = Logger.getLogger("com.cks");

    @Override
    public void execute() {
        try {
            String aesKeyFlag = getFlag("aes_key");
            Path rsaKeyPath = Paths.get(getFlag("rsa_key"));

            AESEncrypter aes = new AESEncrypter();
            if (aesKeyFlag == null) {   //generate new key and save
                aes.generateKey();
                aes.generateIv();
                aes.saveKey(Paths.get("aes.key"), rsaKeyPath, RSAEncrypter.KeyType.PUBLIC);
                LOGGER.info("New AES Key generated and saved");
            } else {                    //load key
                Path aesKeyPath = Paths.get(aesKeyFlag);
                aes.loadKey(aesKeyPath, rsaKeyPath, RSAEncrypter.KeyType.PRIVATE);
                LOGGER.info("AES loaded");
            }
            final String FILENAME = "filename";
            for (int i = 0; i < getFileCount(); i++) {
                Path filePath = Paths.get(getFlag(FILENAME + (i + 1)));
                byte[] cipherBytes = aes.encrypt(FileIO.read(filePath));
                Path encryptPath = FileIO.changeFileExtension(filePath, ".secure");
                LOGGER.info(String.format("Encryption of %s started.", encryptPath.getFileName().toString()));
                FileIO.write(encryptPath, cipherBytes);
                LOGGER.info(String.format("Encryption of %s complete.", encryptPath.getFileName().toString()));
                System.out.println(String.format("File %s has been encrypted.", encryptPath.getFileName().toString()));
            }
        } catch (Exception x) {
            x.printStackTrace();
            LOGGER.severe(x.getMessage());
            throw new IllegalArgumentException(x);
        }
    }
}
