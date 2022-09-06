/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cks.encrypt.cli;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author colin.saldanha
 */
public abstract class Command {

    public static final String CMD_ENCRYPT = "encrypt";
    public static final String CMD_DECRYPT = "decrypt";
    public static final String CMD_RSA = "rsa";
    public static final String CMD_AES = "aes";

    protected Map<String, Flag<?>> flags = new HashMap<>();

    public Command() {
    }

    public Command(Flag<?>... flags) {
        for (Flag<?> flag : flags) {
            if (!validateFlag(flag)) {
                throw new IllegalArgumentException(String.format("Invalid flag: %s", flag.getKey()));
            }
            this.flags.put(flag.getKey(), flag);
        }
    }

    public int getFlagCount() {
        return flags.size();
    }

    public void addFlag(Flag<?> flag) {
        if (validateFlag(flag)) {
            flags.put(flag.getKey(), flag);
        }
    }

    public boolean containsFlag(String key) {
        return flags.containsKey(key);
    }

    public Flag<?> getFlag(String key) {
        if (!flags.containsKey(key)) {
            throw new IllegalArgumentException(String.format("Flag %s is not provided.", key));
        }

        return flags.get(key);
    }

    abstract boolean validateFlag(Flag<?> flag);

    public abstract String getCommand();

    public abstract void execute();
}
