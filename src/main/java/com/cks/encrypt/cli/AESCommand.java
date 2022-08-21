/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cks.encrypt.cli;

import com.cks.encrypt.encryption.AESEncrypter;

import java.io.IOException;
import java.nio.file.Paths;
import java.security.GeneralSecurityException;

/**
 *
 * @author colin.saldanha
 */
public class AESCommand extends Command {

    @Override
    public void execute() {
        try {
            AESEncrypter aesEncrypter = new AESEncrypter();
            aesEncrypter.generateKey();
            aesEncrypter.generateIv();
            String filePath = this.getFlag("filename");
            aesEncrypter.savekey(Paths.get(filePath));
            System.out.println("AES key has been generated. NOTE: Key is not encrypted. \n"
                    + "Use RSA encryption if transferring key to other parties.");

        } catch (IOException x) {
            System.out.println("Error writing key.");
            throw new IllegalArgumentException(x);

        } catch (GeneralSecurityException x) {
            System.out.println("Error generating AES key.");
            throw new IllegalArgumentException(x);
        }
    }
}
