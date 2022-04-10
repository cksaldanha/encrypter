/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cks.encrypt.helper;

/**
 *
 * @author colin.saldanha
 */
public class ArraysHelper {

    private void copyBytes(byte[] dst, int start, byte[] src, int offset, int len) {
        if ((dst.length - start) < len)
            throw new IllegalArgumentException("Source bytes length are too long for destination");

        for (int i = 0; i < len; i++)
            dst[start + i] = src[offset + i];
    }
}
