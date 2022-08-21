/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cks.encrypt.io;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 *
 * @author colin.saldanha
 */
public class FileIO {

    public static void write(Path path, byte[] bytes) throws IOException {
        try (OutputStream out = Files.newOutputStream(path)) {
            out.write(bytes);
        }
    }

    public static byte[] read(Path path) throws IOException {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        byte[] buff = new byte[1024];
        try (InputStream in = Files.newInputStream(path)) {
            int len = 0;
            while ((len = in.read(buff)) > 0) {
                bytes.write(buff, 0, len);
            }
        }
        return bytes.toByteArray();
    }

    public static Path changeFileExtension(Path path, String ext) {
        StringBuilder sb = new StringBuilder(path.getFileName().toString());
        sb.delete(sb.lastIndexOf("."), sb.length());
        sb.append(ext);
        return Paths.get(sb.toString());
    }
}
