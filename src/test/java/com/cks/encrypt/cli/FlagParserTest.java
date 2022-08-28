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
    public void testParseKeyValueFlags_singleKeyValueFlag() {
        FlagParser parser = new FlagParser();
        Set<Flag> actual = parser.parseKeyValueFlags("aes --first=first.Values");
        Set<Flag> expected = new HashSet<>();
        expected.add(new Flag("first", "first.Values"));
        assertEquals(expected, actual);
    }

    @Test
    public void testParseKeyValueFlags_multipleKeyValueFlags() {
        FlagParser parser = new FlagParser();
        Set<Flag> actual = parser.parseKeyValueFlags("aes --first=first.Values --second=secondValue");
        Set<Flag> expected = new HashSet<>();
        expected.add(new Flag("first", "first.Values"));
        expected.add(new Flag("second", "secondValue"));
        assertEquals(expected, actual);
    }

    @Test
    public void testParseKeyValueFlags_multipleKeyValueFlagsWithSpaces() {
        FlagParser parser = new FlagParser();
        Set<Flag> actual = parser.parseKeyValueFlags("aes  --first=first.Values   --second=secondValue  ");
        Set<Flag> expected = new HashSet<>();
        expected.add(new Flag("first", "first.Values"));
        expected.add(new Flag("second", "secondValue"));
        assertEquals(expected, actual);
    }

    @Test
    public void testParseKeyOnlyFlags_singleKeyOnlyFlag() {
        FlagParser parser = new FlagParser();
        Set<Flag> actual = parser.parseKeyOnlyFlags("aes --first");
        Set<Flag> expected = new HashSet<>();
        expected.add(new Flag("first", null));
        assertEquals(expected, actual);
    }

    @Test
    public void testParseKeyOnlyFlags_multipleKeyOnlyFlags() {
        FlagParser parser = new FlagParser();
        Set<Flag> actual = parser.parseKeyOnlyFlags("aes --first --second --third");
        Set<Flag> expected = new HashSet<>();
        expected.add(new Flag("first", null));
        expected.add(new Flag("second", null));
        expected.add(new Flag("third", null));
        assertEquals(expected, actual);
    }

    @Test
    public void testParseKeyOnlyFlags_multipleKeyOnlyFlagsWithSpaces() {
        FlagParser parser = new FlagParser();
        Set<Flag> actual = parser.parseKeyOnlyFlags("aes  --first   --second  --third  ");
        Set<Flag> expected = new HashSet<>();
        expected.add(new Flag("first", null));
        expected.add(new Flag("second", null));
        expected.add(new Flag("third", null));
        assertEquals(expected, actual);
    }

    @Test
    public void testParseFlags_singleKeyValueFlag() {
        FlagParser parser = new FlagParser();
        Set<Flag> actual = parser.parseFlags("aes --first=first.Values");
        Set<Flag> expected = new HashSet<>();
        expected.add(new Flag("first", "first.Values"));
        assertEquals(expected, actual);
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
    public void testParseFlags_mixedFlagsNoLineTerminater() {
        FlagParser parser = new FlagParser();
        Set<Flag> actual = parser.parseFlags("aes --first=first.Values  --third --second=secondValue --fourth");
        Set<Flag> expected = new HashSet<>();
        expected.add(new Flag("first", "first.Values"));
        expected.add(new Flag("second", "secondValue"));
        expected.add(new Flag("third", null));
        expected.add(new Flag("fourth", null));
        assertEquals(expected, actual);
    }
}
