/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cks.encrypt.cli;

import com.cks.encrypt.encryption.RSAEncrypter;
import java.io.IOException;
import java.nio.file.Paths;
import java.security.GeneralSecurityException;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Logger;

/**
 *
 * @author colin.saldanha
 */
public class RSACommand extends Command {

    private static final Logger LOGGER = Logger.getLogger("com.cks");

    public static final Set<String> VALID_FLAG_KEYS;

    static {
        VALID_FLAG_KEYS = new HashSet<>();
        VALID_FLAG_KEYS.add("public");
        VALID_FLAG_KEYS.add("private");
    }

    public RSACommand() {
    }

    public RSACommand(Flag<?>... flags) {
        super(flags);
    }

    @Override
    public void execute() {
        try {
            String publicFileName = null;
            String privateFileName = null;

            if (getFlagCount() == 2) {
                publicFileName = (String) getFlag("public").getValue().orElseThrow(IllegalArgumentException::new);
                privateFileName = (String) getFlag("private").getValue().orElseThrow(IllegalArgumentException::new);
            } else {
                publicFileName = "rsaPublic.key";
                privateFileName = "rsaPrivate.key";
            }

            RSAEncrypter rsa = new RSAEncrypter();
            rsa.generateKeyPair();
            rsa.savePrivateKey(Paths.get(privateFileName));
            rsa.savePublicKey(Paths.get(publicFileName));

            String msg = "RSA public and private keys have been generated.\nNOTE: Keep private key secure and do not share it.";
            System.out.println(msg);

        } catch (IOException x) {
            System.out.println("Error writing keys.");
            LOGGER.severe(x.getMessage());
            throw new IllegalArgumentException(x);

        } catch (GeneralSecurityException x) {
            System.out.println("Error generating RSA keys.");
            LOGGER.severe(x.getMessage());
            throw new IllegalArgumentException(x);
        }
    }

    @Override
    boolean validateFlag(Flag<?> flag) {
        return VALID_FLAG_KEYS.contains(flag.getKey());
    }

    @Override
    public String getCommand() {
        return "rsa";
    }
}
