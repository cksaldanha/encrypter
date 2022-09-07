/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cks.encrypt.cli.flag;

import com.cks.encrypt.cli.flag.FlagParser;
import com.cks.encrypt.cli.flag.FilesFlag;
import com.cks.encrypt.cli.flag.KeyValueFlag;
import com.cks.encrypt.cli.flag.KeyOnlyFlag;
import com.cks.encrypt.cli.flag.Flag;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.Set;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 *
 * @author colin.saldanha
 */
public class FlagParserTest {

    @Test
    public void testParseKeyValueFlags_singleKeyValueFlag() {
        Set<?> actual = FlagParser.parseKeyValueFlags("aes --first=first.Values");
        Set<Flag<?>> expected = new HashSet<>();
        expected.add(new KeyValueFlag("first", "first.Values"));
        assertEquals(expected, actual);
    }

    @Test
    public void testParseKeyValueFlags_multipleKeyValueFlags() {
        Set<?> actual = FlagParser.parseKeyValueFlags("aes --first=first.Values --second=secondValue");
        Set<Flag<?>> expected = new HashSet<>();
        expected.add(new KeyValueFlag("first", "first.Values"));
        expected.add(new KeyValueFlag("second", "secondValue"));
        assertEquals(expected, actual);
    }

    @Test
    public void testParseKeyValueFlags_multipleKeyValueFlagsWithSpaces() {
        Set<?> actual = FlagParser.parseKeyValueFlags("aes  --first=first.Values   --second=secondValue  ");
        Set<Flag<?>> expected = new HashSet<>();
        expected.add(new KeyValueFlag("first", "first.Values"));
        expected.add(new KeyValueFlag("second", "secondValue"));
        assertEquals(expected, actual);
    }

    @Test
    public void testParseKeyValueFlags_multipleKeyValueFlagsWithLineTerminator() {
        Set<?> actual = FlagParser.parseKeyValueFlags("aes --first=first.Values --second=secondValue\n");
        Set<Flag<?>> expected = new HashSet<>();
        expected.add(new KeyValueFlag("first", "first.Values"));
        expected.add(new KeyValueFlag("second", "secondValue"));
        assertEquals(expected, actual);
    }

    @Test
    public void testParseKeyOnlyFlags_singleKeyOnlyFlag() {
        Set<?> actual = FlagParser.parseKeyOnlyFlags("aes --first");
        Set<Flag<?>> expected = new HashSet<>();
        expected.add(new KeyOnlyFlag("first"));
        assertEquals(expected, actual);
    }

    @Test
    public void testParseKeyOnlyFlags_multipleKeyOnlyFlags() {
        Set<?> actual = FlagParser.parseKeyOnlyFlags("aes --first --second --third");
        Set<Flag<?>> expected = new HashSet<>();
        expected.add(new KeyOnlyFlag("first"));
        expected.add(new KeyOnlyFlag("second"));
        expected.add(new KeyOnlyFlag("third"));
        assertEquals(expected, actual);
    }

    @Test
    public void testParseKeyOnlyFlags_multipleKeyOnlyFlagsWithSpaces() {
        Set<?> actual = FlagParser.parseKeyOnlyFlags("aes  --first   --second  --third  ");
        Set<Flag<?>> expected = new HashSet<>();
        expected.add(new KeyOnlyFlag("first"));
        expected.add(new KeyOnlyFlag("second"));
        expected.add(new KeyOnlyFlag("third"));
        assertEquals(expected, actual);
    }

    @Test
    public void testParseFlags_singleKeyValueFlag() {
        Set<?> actual = FlagParser.parseFlags("aes --first=first.Values");
        Set<Flag<?>> expected = new HashSet<>();
        expected.add(new KeyValueFlag("first", "first.Values"));
        assertEquals(expected, actual);
    }

    @Test
    public void testParseFlags_multipleKeyValueFlags() {
        Set<?> actual = FlagParser.parseFlags("aes --first=first.Values --second=secondValue    ");
        Set<Flag<?>> expected = new HashSet<>();
        expected.add(new KeyValueFlag("first", "first.Values"));
        expected.add(new KeyValueFlag("second", "secondValue"));
        assertEquals(expected, actual);
    }

    @Test
    public void testParseFlags_mixedFlags() {
        Set<?> actual = FlagParser.parseFlags("aes --first=first.Values  --third --second=secondValue    ");
        Set<Flag<?>> expected = new HashSet<>();
        expected.add(new KeyValueFlag("first", "first.Values"));
        expected.add(new KeyValueFlag("second", "secondValue"));
        expected.add(new KeyOnlyFlag("third"));
        assertEquals(expected, actual);
    }

    @Test
    public void testParseFlags_mixedFlagsNoLineTerminater() {
        Set<?> actual = FlagParser.parseFlags("aes --first=first.Values  --third --second=secondValue --fourth");
        Set<Flag<?>> expected = new HashSet<>();
        expected.add(new KeyValueFlag("first", "first.Values"));
        expected.add(new KeyValueFlag("second", "secondValue"));
        expected.add(new KeyOnlyFlag("third"));
        expected.add(new KeyOnlyFlag("fourth"));
        assertEquals(expected, actual);
    }

    @Test
    public void testParseFlags_withSingleFileName() {
        Set<?> actual = FlagParser.parseFlags("aes file1");
        Set<Flag<?>> expected = new HashSet<>();
        expected.add(new FilesFlag(Paths.get("file1")));
        assertEquals(expected, actual);
    }

    @Test
    public void testParseFlags_withSingleFileNameAndLineTeriminator() {
        Set<?> actual = FlagParser.parseFlags("aes file1\n");
        Set<Flag<?>> expected = new HashSet<>();
        FilesFlag flag = new FilesFlag(Paths.get("file1"));
        expected.add(flag);
        assertEquals(expected, actual);
    }

    @Test
    public void testParseFlags_withSingleFileNameAndRandomSpaces() {
        Set<?> actual = FlagParser.parseFlags("aes   file1   ");
        Set<Flag<?>> expected = new HashSet<>();
        FilesFlag flag = new FilesFlag(Paths.get("file1"));
        expected.add(flag);
        assertEquals(expected, actual);
    }

    @Test
    public void testParseFlags_withMultipleFileNames() {
        Set<?> actual = FlagParser.parseFlags("encrypt file1 file2");
        Set<Flag<?>> expected = new HashSet<>();
        FilesFlag flag = new FilesFlag(Paths.get("file1"), Paths.get("file2"));
        expected.add(flag);
        assertEquals(expected, actual);
    }

    @Test
    public void testParseFlags_withMultipleFileNamesAndLineTerminator() {
        Set<?> actual = FlagParser.parseFlags("encrypt file1 file2\n");
        Set<Flag<?>> expected = new HashSet<>();
        FilesFlag flag = new FilesFlag(Paths.get("file1"), Paths.get("file2"));
        expected.add(flag);
        assertEquals(expected, actual);
    }

    @Test
    public void testParseFlags_withMultipleFileNamesAndRandomSpaces() {
        Set<?> actual = FlagParser.parseFlags("encrypt   file1   file2 \n");
        Set<Flag<?>> expected = new HashSet<>();
        FilesFlag flag = new FilesFlag(Paths.get("file1"), Paths.get("file2"));
        expected.add(flag);
        assertEquals(expected, actual);
    }

    @Test
    public void testParseFlags_withFlagsAndFilePath() {
        Set<?> actual = FlagParser.parseFlags("encrypt --mode=aes --keypath=/test/aes.key /test/file1.txt");
        Set<Flag<?>> expected = new HashSet<>();
        expected.add(new KeyValueFlag("mode", "aes"));
        expected.add(new KeyValueFlag("keypath", "/test/aes.key"));
        FilesFlag filesFlag = new FilesFlag(Paths.get("/test/file1.txt"));
        expected.add(filesFlag);
        assertEquals(expected, actual);
    }
}
