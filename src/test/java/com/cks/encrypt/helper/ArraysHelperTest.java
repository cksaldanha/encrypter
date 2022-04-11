/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cks.encrypt.helper;
import com.cks.encrypt.helper.ArraysHelper;

import java.nio.charset.Charset;
import java.util.Arrays;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author colin.saldanha
 */
public class ArraysHelperTest {
    
    @Test
    public void testCopyBytes_Simple1() {
        byte[] sample = "This is a sample string".getBytes(Charset.forName("utf-8"));
        byte[] copy = new byte[sample.length];
        
        ArraysHelper.copyBytes(copy, 0, sample, 0, sample.length);
        assertTrue(Arrays.equals(copy, sample));
    }
}
