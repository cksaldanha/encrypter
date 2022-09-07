/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cks.encrypt.cli;

import com.cks.encrypt.cli.flag.FilesFlag;
import com.cks.encrypt.cli.flag.Flag;
import com.cks.encrypt.cli.flag.KeyValueFlag;
import com.cks.encrypt.encryption.EncryptionAlgorithms;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 *
 * @author colin.saldanha
 */
public abstract class EncryptDecryptCommand extends Command {

    private static final Set<String> VALID_FLAG_KEYS;

    static {
        VALID_FLAG_KEYS = new HashSet<>();
        VALID_FLAG_KEYS.add("mode");
        VALID_FLAG_KEYS.add("keypath");
        VALID_FLAG_KEYS.add("type");
        VALID_FLAG_KEYS.add("files");
    }

    public EncryptDecryptCommand(Flag<?>... flags) {
        super(flags);
        validateRSAFlags();
    }

    private void validateRSAFlags() {
        if (this.flags.get("mode").getKey().equals(EncryptionAlgorithms.RSA)) {
            if (!this.flags.containsKey("type")) {
                throw new IllegalArgumentException("Must provide type argument with RSA encryption");
            }
            KeyValueFlag typeFlag = (KeyValueFlag) this.flags.get("type");
            String type = typeFlag.getValue().orElseThrow(() -> new IllegalArgumentException("Cannot have null value for type flag"));
            if (!type.equals("public") && !type.equals("private")) {
                throw new IllegalArgumentException("Invalid type argument for RSA encryption");
            }
        }
    }

    @Override
    boolean validateFlag(Flag<?> flag) {
        return VALID_FLAG_KEYS.contains(flag.getKey());
    }

    public Path getKeyFilePath() {
        if (!flags.containsKey("keypath")) {
            throw new IllegalArgumentException("No keypath flag was specified.");
        }
        KeyValueFlag keypathFlag = (KeyValueFlag) flags.get("keypath");
        String keyFile = keypathFlag
                .getValue()
                .orElseThrow(() -> new IllegalArgumentException("Invalid key file path specified."));
        return Paths.get(keyFile);
    }

    public List<Path> getFilePathList() {
        if (!flags.containsKey("files")) {
            throw new IllegalArgumentException("No files flag is available.");
        }
        FilesFlag filesFlag = (FilesFlag) flags.get("files");
        List<Path> filePathList = filesFlag.getValue().orElseThrow(() -> new IllegalArgumentException("Invalid file list supplied"));
        return Collections.unmodifiableList(filePathList);
    }
}
