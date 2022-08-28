/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cks.encrypt.encryption;

import java.util.HashSet;
import java.util.Set;

/**
 *
 * @author colin.saldanha
 */
public class EncryptionAlgorithms {

    public static final String AES = "aes";
    public static final String RSA = "rsa";

    private static final Set<String> ALGORITHMS = new HashSet<>();

    static {
        ALGORITHMS.add(AES);
        ALGORITHMS.add(RSA);
    }

    public static boolean isAvailable(String algorithm) {
        return ALGORITHMS.contains(algorithm);
    }
}
