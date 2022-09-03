/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cks.encrypt.io;

import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import org.junit.Test;

/**
 *
 * @author colin.saldanha
 */
public class FileIOTest {
    

    @Test
    public void testWrite_Simple1() throws Exception {
        FileIO.write(Paths.get("simple.txt"), "This is a test".getBytes());
        Path path = Paths.get("simple.txt");
        assertTrue(Files.exists(path));
        Files.deleteIfExists(Paths.get("simple.txt"));
    }
    
    @Test
    public void testChangeFileExtension_Simple1() {
        Path sample = Paths.get("sample.txt");
        Path act = FileIO.changeFileExtension(sample, ".secure");
        assertEquals("sample.secure", act.toString());
    }
    @Test
    public void testChangeFileExtension_Simple2() {
        Path sample = Paths.get("sample.secure");
        Path act = FileIO.changeFileExtension(sample, ".pdf");
        assertEquals("sample.pdf", act.toString());
    }
}
