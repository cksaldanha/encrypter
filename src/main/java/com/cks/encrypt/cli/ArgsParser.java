/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cks.encrypt.cli;

import java.util.Set;

/**
 *
 * @author colin.saldanha
 */
public class ArgsParser {

    public static Command parseArgs(String[] args) {
        if (args.length == 0) {
            throw new IllegalArgumentException();
        }
        String commandLine = getCommandLine(args);
        Set<Flag> flags = FlagParser.parseFlags(commandLine);
        Command command = null;
        switch (args[0]) {
            case Command.CMD_ENCRYPT:
                command = new EncryptCommand(flags.stream().toArray(Flag[]::new));
                break;
            case Command.CMD_DECRYPT:
                command = new DecryptCommand(flags.stream().toArray(Flag[]::new));
                break;
            case Command.CMD_RSA:
                command = new RSACommand(flags.stream().toArray(Flag[]::new));
                break;
            case Command.CMD_AES:
                command = new AESCommand(flags.stream().toArray(Flag[]::new));
                break;
            default:
                throw new IllegalArgumentException();
        }
        return command;
    }

    public static String getCommandLine(String[] args) {
        StringBuilder sb = new StringBuilder();
        for (String arg : args) {
            sb.append(arg.trim()).append(' ');
        }
        sb.deleteCharAt(sb.length() - 1);
        sb.append('\n');
        return sb.toString();
    }
}
