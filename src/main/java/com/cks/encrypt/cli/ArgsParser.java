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
        if (args.length == 0)
            throw new IllegalArgumentException();
        
        Command command = new Command();
        command.setCommand(args[0]);
        switch (args[0]) {
            case Command.CMD_ENCRYPT:
                switch (args.length) {
                    case 2:
                        command.addFlag(new Flag("filename", args[1]));
                        break;
                    case 4:
                        command.addFlag(new Flag("aes_key", args[1]));
                        command.addFlag(new Flag("rsa_key", args[2]));
                        command.addFlag(new Flag("filename", args[3])); 
                        break;
                    default:
                        throw new IllegalArgumentException();
                }
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
                        command.addFlag(new Flag("filename1", "private.key"));
                        break;
                    case 3:
                        command.addFlag(new Flag("filename1", args[1]));
                        command.addFlag(new Flag("filename2", args[2]));
                        break;
                    default:
                        throw new IllegalArgumentException();
                }
                break;
            default:
                throw new IllegalArgumentException();
        }
        return command;
    }
}
