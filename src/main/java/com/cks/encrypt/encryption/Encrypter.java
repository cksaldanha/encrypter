/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cks.encrypt.encryption;

import java.nio.file.Path;

/**
 *
 * @author colin.saldanha
 */
public interface Encrypter {
    byte[] encrypt(byte[] plainBytes, Path keyFile) throws Exception;
    byte[] decrypt(byte[] cipherBytes, Path keyFile) throws Exception;
}
