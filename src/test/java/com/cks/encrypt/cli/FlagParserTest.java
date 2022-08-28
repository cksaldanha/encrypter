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
        Set<Flag> actual = FlagParser.parseKeyValueFlags("aes --first=first.Values");
        Set<Flag> expected = new HashSet<>();
        expected.add(new Flag("first", "first.Values"));
        assertEquals(expected, actual);
    }

    @Test
    public void testParseKeyValueFlags_multipleKeyValueFlags() {
        Set<Flag> actual = FlagParser.parseKeyValueFlags("aes --first=first.Values --second=secondValue");
        Set<Flag> expected = new HashSet<>();
        expected.add(new Flag("first", "first.Values"));
        expected.add(new Flag("second", "secondValue"));
        assertEquals(expected, actual);
    }

    @Test
    public void testParseKeyValueFlags_multipleKeyValueFlagsWithSpaces() {
        Set<Flag> actual = FlagParser.parseKeyValueFlags("aes  --first=first.Values   --second=secondValue  ");
        Set<Flag> expected = new HashSet<>();
        expected.add(new Flag("first", "first.Values"));
        expected.add(new Flag("second", "secondValue"));
        assertEquals(expected, actual);
    }

    @Test
    public void testParseKeyOnlyFlags_singleKeyOnlyFlag() {
        Set<Flag> actual = FlagParser.parseKeyOnlyFlags("aes --first");
        Set<Flag> expected = new HashSet<>();
        expected.add(new Flag("first", null));
        assertEquals(expected, actual);
    }

    @Test
    public void testParseKeyOnlyFlags_multipleKeyOnlyFlags() {
        Set<Flag> actual = FlagParser.parseKeyOnlyFlags("aes --first --second --third");
        Set<Flag> expected = new HashSet<>();
        expected.add(new Flag("first", null));
        expected.add(new Flag("second", null));
        expected.add(new Flag("third", null));
        assertEquals(expected, actual);
    }

    @Test
    public void testParseKeyOnlyFlags_multipleKeyOnlyFlagsWithSpaces() {
        Set<Flag> actual = FlagParser.parseKeyOnlyFlags("aes  --first   --second  --third  ");
        Set<Flag> expected = new HashSet<>();
        expected.add(new Flag("first", null));
        expected.add(new Flag("second", null));
        expected.add(new Flag("third", null));
        assertEquals(expected, actual);
    }

    @Test
    public void testParseFlags_singleKeyValueFlag() {
        Set<Flag> actual = FlagParser.parseFlags("aes --first=first.Values");
        Set<Flag> expected = new HashSet<>();
        expected.add(new Flag("first", "first.Values"));
        assertEquals(expected, actual);
    }

    @Test
    public void testParseFlags_multipleKeyValueFlags() {
        Set<Flag> actual = FlagParser.parseFlags("aes --first=first.Values --second=secondValue    ");
        Set<Flag> expected = new HashSet<>();
        expected.add(new Flag("first", "first.Values"));
        expected.add(new Flag("second", "secondValue"));
        assertEquals(expected, actual);
    }

    @Test
    public void testParseFlags_mixedFlags() {
        Set<Flag> actual = FlagParser.parseFlags("aes --first=first.Values  --third --second=secondValue    ");
        Set<Flag> expected = new HashSet<>();
        expected.add(new Flag("first", "first.Values"));
        expected.add(new Flag("second", "secondValue"));
        expected.add(new Flag("third", null));
        assertEquals(expected, actual);
    }

    @Test
    public void testParseFlags_mixedFlagsNoLineTerminater() {
        Set<Flag> actual = FlagParser.parseFlags("aes --first=first.Values  --third --second=secondValue --fourth");
        Set<Flag> expected = new HashSet<>();
        expected.add(new Flag("first", "first.Values"));
        expected.add(new Flag("second", "secondValue"));
        expected.add(new Flag("third", null));
        expected.add(new Flag("fourth", null));
        assertEquals(expected, actual);
    }
}
