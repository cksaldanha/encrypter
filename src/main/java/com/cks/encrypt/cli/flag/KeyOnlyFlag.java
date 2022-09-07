/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cks.encrypt.cli.flag;

import java.util.Optional;

/**
 *
 * @author colin.saldanha
 */
public class KeyOnlyFlag extends Flag<Object> {

    public KeyOnlyFlag(String key) {
        super(key, null);
    }

    @Override
    public Optional<Object> getValue() {
        throw new UnsupportedOperationException();
    }
}
