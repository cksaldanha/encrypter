/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cks.encrypt.cli;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 *
 * @author colin.saldanha
 */
public class FilesFlag extends Flag<List<Path>> {

    private static final String FILE_NAME_FLAG_KEY = "files";

    public FilesFlag(List<Path> filePaths) {
        super(FILE_NAME_FLAG_KEY, filePaths);
    }

    public FilesFlag(Path... rest) {
        super(FILE_NAME_FLAG_KEY, new ArrayList<>());
        Arrays.stream(rest).forEach(this::add);
    }

    public void add(Path filePath) {
        List<Path> filePaths = getValue().orElseThrow(() -> new IllegalStateException("Cannot have a null file path list."));
        filePaths.add(filePath);
    }

    public int count() {
        List<Path> filePaths = getValue().orElseThrow(() -> new IllegalStateException("Cannot have a null file path list."));
        return filePaths.size();
    }
}
