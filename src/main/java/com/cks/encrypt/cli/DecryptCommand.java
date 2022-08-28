/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cks.encrypt.cli;

import com.cks.encrypt.encryption.EncryptionAlgorithms;
import java.util.HashSet;
import java.util.Set;

/**
 *
 * @author colin.saldanha
 */
public class DecryptCommand extends Command {

    private static final Set<String> VALID_FLAG_KEYS;

    static {
        VALID_FLAG_KEYS = new HashSet<>();
        VALID_FLAG_KEYS.add("mode");
        VALID_FLAG_KEYS.add("type");
        VALID_FLAG_KEYS.add("files");
    }

    public DecryptCommand(Flag... flags) {
        super(flags);
        if (getFlag("mode").equals(EncryptionAlgorithms.RSA)) {
            if (!containsFlag("type") || !getFlag("type").getValue().isPresent()) {
                throw new IllegalArgumentException("Must provide type argument with RSA encryption");
            }
            String type = (String) getFlag("type").getValue().get();
            if (!type.equals("public") && !type.equals("private")) {
                throw new IllegalArgumentException("Invalid type argument for RSA encryption");
            }
        }
    }

    @Override
    public String getCommand() {
        return Command.CMD_DECRYPT;
    }

    @Override
    boolean validateFlag(Flag flag) {
        return VALID_FLAG_KEYS.contains(flag.getKey());
    }

    @Override
    public void execute() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
