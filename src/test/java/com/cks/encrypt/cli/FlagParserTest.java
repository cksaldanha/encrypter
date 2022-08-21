/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cks.encrypt.cli;

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
    public void testParseFlags_singleKeyValueFlag() {
        FlagParser parser = new FlagParser();
        Set<Flag> actual = parser.parseFlags("aes --first=first.Values");
        Set<Flag> expected = new HashSet<>();
        expected.add(new Flag("first", "first.Values"));
        assertEquals(actual, expected);
    }

    @Test
    public void testParseFlags_multipleKeyValueFlags() {
        FlagParser parser = new FlagParser();
        Set<Flag> actual = parser.parseFlags("aes --first=first.Values --second=secondValue    ");
        Set<Flag> expected = new HashSet<>();
        expected.add(new Flag("first", "first.Values"));
        expected.add(new Flag("second", "secondValue"));
        assertEquals(expected, actual);
    }

    @Test
    public void testParseFlags_mixedFlags() {
        FlagParser parser = new FlagParser();
        Set<Flag> actual = parser.parseFlags("aes --first=first.Values  --third --second=secondValue    ");
        Set<Flag> expected = new HashSet<>();
        expected.add(new Flag("first", "first.Values"));
        expected.add(new Flag("second", "secondValue"));
        expected.add(new Flag("third", null));
        assertEquals(expected, actual);
    }

    @Test
    public void testParseFlag_keyValueArgument() {
        FlagParser parser = new FlagParser();
        Flag actual = parser.parseFlag("--flag=value");
        Flag expected = new Flag("flag", "value");
        assertEquals(expected, actual);
    }

    @Test
    public void testParseFlag_keyOnlyArgument() {
        FlagParser parser = new FlagParser();
        Flag actual = parser.parseFlag("--flag");
        Flag expected = new Flag("flag", null);
        assertEquals(expected, actual);
    }
}
