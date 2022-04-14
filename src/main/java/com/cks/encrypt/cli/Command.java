/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cks.encrypt.cli;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author colin.saldanha
 */
public class Command {
    
    public static final String CMD_ENCRYPT = "encrypt";
    public static final String CMD_DECRYPT = "decrypt";
    public static final String CMD_RSA = "rsa";
    
    private String command;
    private Map<String, String> flags = new HashMap<>();
    
    public Command() {
    }
    
    public Command(String command, Flag... flags) {
        this.command = command.toLowerCase();
        for (Flag flag : flags) {
            if (flag != null && !flag.equals(""))
                this.flags.put(flag.getKey(), flag.getValue());
        }
    }
    
    public void setCommand(String command) {
        this.command = command;
    }
    
    public String getCommand() {
        return command;
    }
    
    public int getFlagCount() {
        return flags.size();
    }
    
    public void addFlag(Flag flag) {
        flags.put(flag.getKey(), flag.getValue());
    }
    
    public String getFlag(String key) {
        if (!flags.containsKey(key))
            throw new IllegalArgumentException(String.format("Flag %s is not provided.", key));
        
        return flags.get(key);
    }
    
    public String getFlag(String key, String defResult) {
        if (!flags.containsKey(key))
            return defResult;
        
        return flags.get(key);
    }
}
