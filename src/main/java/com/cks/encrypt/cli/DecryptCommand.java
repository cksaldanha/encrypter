/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cks.encrypt.cli;

import com.cks.encrypt.encryption.AESEncrypter;
import com.cks.encrypt.encryption.EncryptionAlgorithms;
import com.cks.encrypt.encryption.RSAEncrypter;
import com.cks.encrypt.io.FileIO;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.GeneralSecurityException;
import java.util.List;

/**
 *
 * @author colin.saldanha
 */
public class DecryptCommand extends EncryptDecryptCommand {

    public DecryptCommand(Flag... flags) {
        super(flags);
    }

    @Override
    public String getCommand() {
        return Command.CMD_DECRYPT;
    }

    @Override
    public void execute() {
        try {
            String mode = (String) getFlag("mode").getValue().orElseThrow(IllegalArgumentException::new);
            Path keyFilePath = getKeyFilePath();
            List<String> fileNames = getFileList();

            switch (mode) {
                case EncryptionAlgorithms.AES:
                    AESEncrypter aes = new AESEncrypter();
                    aes.loadKey(keyFilePath);

                    for (String fileName : fileNames) {
                        Path fileNamePath = Paths.get(fileName);
                        byte[] plainBytes = aes.decrypt(FileIO.read(fileNamePath));
                        Path decryptPath = FileIO.changeFileExtension(fileNamePath, ".unsecure");
                        FileIO.write(decryptPath, plainBytes);
                        System.out.println(String.format("File %s has been decrypted.", decryptPath.getFileName().toString()));
                    }
                    break;

                case EncryptionAlgorithms.RSA:
                    RSAEncrypter rsa = new RSAEncrypter();
                    String type = (String) getFlag("type")
                            .getValue()
                            .orElseThrow(() -> new IllegalArgumentException("Must provide a type with RSA encryption"));

                    for (String fileName : fileNames) {
                        Path fileNamePath = Paths.get(fileName);
                        byte[] plainBytes = rsa.decrypt(
                                FileIO.read(fileNamePath),
                                keyFilePath,
                                type.equals("public") ? RSAEncrypter.KeyType.PUBLIC : RSAEncrypter.KeyType.PRIVATE
                        );
                        Path decryptPath = FileIO.changeFileExtension(fileNamePath, ".unsecure");
                        FileIO.write(decryptPath, plainBytes);
                    }
                    break;

                default:
                    throw new IllegalArgumentException("Unrecognized mode supplied.");
            }

        } catch (IOException x) {
            System.out.println("Error with I/O for encrypttion.");
            throw new IllegalArgumentException(x);

        } catch (GeneralSecurityException x) {
            System.out.println("Error with security.");
            throw new IllegalArgumentException(x);
        }
    }
}
