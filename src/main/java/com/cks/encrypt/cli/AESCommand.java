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
import java.util.HashSet;
import java.util.Set;

/**
 *
 * @author colin.saldanha
 */
public class AESCommand extends Command {

    public static final Set<String> VALID_FLAG_KEYS;

    static {
        VALID_FLAG_KEYS = new HashSet<>();
        VALID_FLAG_KEYS.add("files");
    }

    public AESCommand() {
        super();
    }

    public AESCommand(Flag... flags) {
        super(flags);
    }

    @Override
    public void execute() {
        try {
            AESEncrypter aesEncrypter = new AESEncrypter();
            aesEncrypter.generateKey();
            aesEncrypter.generateIv();
            String filePath = this.getFlag("filename");
            aesEncrypter.savekey(Paths.get(filePath));
            System.out.println("AES key has been generated. \nNOTE: Key is not encrypted. "
                    + "Use RSA encryption if transferring key to other parties.");

        } catch (IOException x) {
            System.out.println("Error writing key.");
            throw new IllegalArgumentException(x);

        } catch (GeneralSecurityException x) {
            System.out.println("Error generating AES key.");
            throw new IllegalArgumentException(x);
        }
    }

    @Override
    boolean validateFlag(Flag flag) {
        return VALID_FLAG_KEYS.contains(flag.getKey());
    }

    @Override
    public String getCommand() {
        return "aes";
    }
}
