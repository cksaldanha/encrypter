/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cks.encrypt.cli;

import java.util.HashSet;
import java.util.Set;

/**
 *
 * @author colin.saldanha
 */
public class AESFlagParser extends FlagParser {

    private final static Set<String> VALID_FLAG_KEYS = new HashSet<>();

    static {
        VALID_FLAG_KEYS.add("file");
    }

    @Override
    public boolean validateFlag(Flag flag) {
        return VALID_FLAG_KEYS.contains(flag.getKey());
    }
}
