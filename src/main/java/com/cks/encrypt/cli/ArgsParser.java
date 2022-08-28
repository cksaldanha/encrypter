/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cks.encrypt.cli;

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
        Command command = null;
        switch (args[0]) {
            case Command.CMD_ENCRYPT:
                if (args.length < 4) {
                    throw new IllegalArgumentException();
                }

                String aes_key = args[1];
                //Application.class uses a null checkk on aes_key + need the flag count
                command.addFlag(new Flag("aes_key", aes_key.equals("none") ? null : aes_key));

                command.addFlag(new Flag("rsa_key", args[2]));

                final String FILENAME = "filename";
                int fileCount = 0;
                for (int i = 3; i < args.length; i++) {
                    command.addFlag(new Flag(FILENAME + (i - 2), args[i]));
                    fileCount++;
                }
                command.setFileCount(fileCount);
                break;
            case Command.CMD_DECRYPT:
                switch (args.length) {
                    case 4:
                        command.addFlag(new Flag("aes_key", args[1]));
                        command.addFlag(new Flag("rsa_key", args[2]));
                        command.addFlag(new Flag("filename", args[3]));
                        break;
                    default:
                        throw new IllegalArgumentException();
                }
                break;
            case Command.CMD_RSA:
                switch (args.length) {
                    case 1:
                        //default filename values for RSA generation command
                        command.addFlag(new Flag("filename1", "public.key"));
                        command.addFlag(new Flag("filename2", "private.key"));
                        break;
                    case 3:
                        command.addFlag(new Flag("filename1", args[1]));
                        command.addFlag(new Flag("filename2", args[2]));
                        break;
                    default:
                        throw new IllegalArgumentException();
                }
                break;
            case Command.CMD_AES:
                command = new AESCommand();
                FlagParser flagParser = new AESFlagParser();
                switch (args.length) {
                    case 1:
                        //default filename for aes_key
                        command.addFlag(new Flag("file", "aes.key"));
                        break;
                    case 2:
                        Flag flag = flagParser.parseFlag(args[1]);
                        command.addFlag(flag);
                        break;
                    default:
                        throw new IllegalArgumentException("Too many arguments for aes command");
                }
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
