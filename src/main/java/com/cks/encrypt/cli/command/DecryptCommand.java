/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cks.encrypt.cli.command;

import com.cks.encrypt.cli.flag.Flag;
import com.cks.encrypt.cli.flag.KeyValueFlag;
import com.cks.encrypt.encryption.AESEncrypter;
import com.cks.encrypt.encryption.EncryptionAlgorithms;
import com.cks.encrypt.encryption.RSAEncrypter;
import com.cks.encrypt.io.FileIO;
import java.io.IOException;
import java.nio.file.Path;
import java.security.GeneralSecurityException;
import java.util.List;

/**
 *
 * @author colin.saldanha
 */
public class DecryptCommand extends EncryptDecryptCommand {

    public DecryptCommand(Flag<?>... flags) {
        super(flags);
    }

    @Override
    public String getCommand() {
        return Command.CMD_DECRYPT;
    }

    @Override
    public void execute() {
        try {
            String mode = ((KeyValueFlag) getFlag("mode")).getValue().orElseThrow(IllegalArgumentException::new);
            Path keyFilePath = getKeyFilePath();
            List<Path> filePaths = super.getFilePathList();

            switch (mode) {
                case EncryptionAlgorithms.AES:
                    AESEncrypter aes = new AESEncrypter();
                    aes.loadKey(keyFilePath);

                    for (Path filePath : filePaths) {
                        byte[] plainBytes = aes.decrypt(FileIO.read(filePath));
                        Path decryptPath = FileIO.changeFileExtension(filePath, ".unsecure");
                        FileIO.write(decryptPath, plainBytes);
                        System.out.println(String.format("File %s has been decrypted.", decryptPath.getFileName().toString()));
                    }
                    break;

                case EncryptionAlgorithms.RSA:
                    RSAEncrypter rsa = new RSAEncrypter();
                    String type = (String) getFlag("type")
                            .getValue()
                            .orElseThrow(() -> new IllegalArgumentException("Must provide a type with RSA encryption"));

                    for (Path filePath : filePaths) {
                        byte[] plainBytes = rsa.decrypt(
                                FileIO.read(filePath),
                                keyFilePath,
                                type.equals("public") ? RSAEncrypter.KeyType.PUBLIC : RSAEncrypter.KeyType.PRIVATE
                        );
                        Path decryptPath = FileIO.changeFileExtension(filePath, ".unsecure");
                        FileIO.write(decryptPath, plainBytes);
                        System.out.println(String.format("File %s has been decrypted.", decryptPath.getFileName().toString()));
                    }
                    break;

                default:
                    throw new IllegalArgumentException("Unrecognized mode supplied.");
            }

        } catch (IOException x) {
            System.out.println("Error with I/O for encryption.");
            throw new IllegalArgumentException(x);

        } catch (GeneralSecurityException x) {
            System.out.println("Error with security.");
            throw new IllegalArgumentException(x);
        }
    }
}
