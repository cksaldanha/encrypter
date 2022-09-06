/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cks.encrypt.cli;

import java.util.Optional;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 *
 * @author colin.saldanha
 */
public class KeyOnlyFlagTest {

    @Test
    public void testGetValue_withKeyOnlyInput() {
        KeyOnlyFlag flag = new KeyOnlyFlag("encode");
        try {
            Optional<?> value = flag.getValue();
            fail("Expected an exception to be thrown");
        } catch (UnsupportedOperationException x) {
        }
    }

    @Test
    public void testGetValue_withFlagTypeUsed() {
        Flag<?> flag = new KeyOnlyFlag("encode");
        try {
            Optional<?> value = flag.getValue();
            fail("Expected an exception to be thrown");
        } catch (UnsupportedOperationException x) {
        }
    }
}
