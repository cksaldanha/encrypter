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

    private Map<String, Flag> flags = new HashMap<>();

    public Command() {
    }

    public Command(Flag... flags) {
        for (Flag flag : flags) {
            if (flag != null && !flag.equals("")) {
                this.flags.put(flag.getKey(), flag);
            }
        }
    }

    public int getFlagCount() {
        return flags.size();
    }

    public void addFlag(Flag flag) {
        flags.put(flag.getKey(), flag);
    }

    public Flag getFlag(String key) {
        if (!flags.containsKey(key)) {
            throw new IllegalArgumentException(String.format("Flag %s is not provided.", key));
        }

        return flags.get(key);
    }

    public String getFlag(String key, String defResult) {
        if (!flags.containsKey(key)) {
            return defResult;
        }

        return flags.get(key);
    }

    public abstract String getCommand();

    public abstract void execute();
}
