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
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
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
            int flagCount = getFlagCount();
            List<String> fileNames = null;
            if (flagCount > 0) {
                fileNames = (List<String>) getFlag("files").getValue().orElseThrow(IllegalArgumentException::new);
            } else {
                fileNames = new ArrayList<>();
                fileNames.add("aes.key");
            }
            int count = 0;
            for (String fileName : fileNames) {
                AESEncrypter aesEncrypter = new AESEncrypter();
                aesEncrypter.generateKey();
                aesEncrypter.generateIv();
                aesEncrypter.savekey(Paths.get(fileName));
                count++;
            }
            String msg = String.format("%d AES keys has been generated.\nNOTE: Keys are not encrypted. "
                    + "Use RSA encryption if transferring key(s) to other parties.", count);
            System.out.println(msg);

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
