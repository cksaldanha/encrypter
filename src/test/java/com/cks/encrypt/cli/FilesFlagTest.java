/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cks.encrypt.cli;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;
import org.junit.Test;

import static org.junit.Assert.*;
import org.junit.Ignore;

/**
 *
 * @author colin.saldanha
 */
public class FilesFlagTest {

    @Test
    public void testAdd_withSingleFilePath() {
        FilesFlag flag = new FilesFlag();
        Path file1 = Paths.get("c:\\temp\\file1.txt");
        flag.add(file1);
        Optional<List<Path>> opt = flag.getValue();
        assertNotNull(opt);
        assertTrue(opt.isPresent());
        List<Path> filePaths = opt.get();
        assertEquals(file1, filePaths.get(0));
    }

    @Ignore
    public void testSize_withMultipleFiles() {
        Path file1 = Paths.get("file1");
        Path file2 = Paths.get("file2");
        Path file3 = Paths.get("file3");
        FilesFlag flag = new FilesFlag(file1, file2, file3);
        assertEquals(3, flag.size());
    }

}
