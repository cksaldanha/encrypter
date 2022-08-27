/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cks.encrypt.cli;

import java.util.List;

/**
 *
 * @author colin.saldanha
 */
public class FileListFlag extends Flag {

    public FileListFlag(String key, List<String> fileNames) {
        super(key, fileNames);
    }
}
