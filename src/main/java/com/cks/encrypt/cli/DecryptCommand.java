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
public class DecryptCommand extends Command {

    public DecryptCommand(Flag... flags) {
        super(flags);
    }

    @Override
    public String getCommand() {
        return Command.CMD_DECRYPT;
    }

    @Override
    public void execute() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}