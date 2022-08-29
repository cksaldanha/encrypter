/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cks.encrypt.encryption;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 *
 * @author colin.saldanha
 */
public class EncryptionAlgorithmsTest {

    @Test
    public void testIsAvailable_withValidAES() {
        assertTrue(EncryptionAlgorithms.isAvailable("aes"));
    }

    @Test
    public void testIsAvailable_withValidRSA() {
        assertTrue(EncryptionAlgorithms.isAvailable("aes"));
    }

    @Test
    public void testIsAvailable_withInvalidAlgorithm() {
        assertFalse(EncryptionAlgorithms.isAvailable("DES"));
    }

}
