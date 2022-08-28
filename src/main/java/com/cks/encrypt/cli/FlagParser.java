/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cks.encrypt.cli;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author colin.saldanha
 */
public class FlagParser {

    private static final Logger LOGGER = Logger.getLogger("com.cks");
    private static final Pattern KEY_VALUE_PATTERN = Pattern.compile("--(?<key>[\\w-]+)(?==(?<value>[\\w-\\.]+\\s*))");
    private static final Pattern KEY_ONLY_PATTERN = Pattern.compile("--(?<key>[\\w-]+)(?=\\s*)");
    private static final Pattern NO_KEY_PATTERN = Pattern.compile("(?<=\\s)[\\w]");

    public Set<Flag> parseFlags(String line) {
        Set<Flag> flags = new HashSet<>();

        Set<Flag> keyValueFlags = parseFlagSet(KEY_VALUE_PATTERN, line);
        Set<Flag> keyOnlyFlags = parseFlagSet(KEY_ONLY_PATTERN, line);

        flags.addAll(keyValueFlags);
        for (Flag flag : keyOnlyFlags) {
            if (!flags.contains(flag)) {
                flags.add(flag);
            }
        }
        return flags;
    }

    public Set<Flag> parseKeyValueFlags(String line) {
        Matcher matcher = KEY_VALUE_PATTERN.matcher(line);
        Set<Flag> flags = new HashSet<>();
        while (matcher.find()) {
            String key = matcher.group("key");
            String value = matcher.group("value");
            flags.add(new Flag(key, value));
        }
        return flags;
    }

    public Set<Flag> parseKeyOnlyFlags(String line) {
        Matcher matcher = KEY_ONLY_PATTERN.matcher(line);
        Set<Flag> flags = new HashSet<>();
        while (matcher.find()) {
            String key = matcher.group("key");
            flags.add(new Flag(key, null));
        }
        return flags;
    }

    public Flag parseFileNamesFlag(String line) {
        Matcher matcher = NO_KEY_PATTERN.matcher(line);
        List<String> fileNames = new ArrayList<>();
        while (matcher.find()) {
            String fileName = matcher.group();
            fileNames.add(fileName);
        }
        return fileNames.size() > 0 ? new Flag("files", fileNames) : null;
    }

    public Set<Flag> parseFlagSet(Pattern pattern, String line) {
        Matcher matcher = pattern.matcher(line);
        Set<Flag> flags = new HashSet<>();
        while (matcher.find()) {
            String match = matcher.group();
            Flag flag = parseFlag(match);
            flags.add(flag);
        }
        return flags;
    }

    public Flag parseFlag(String line) {
        int start = line.indexOf("--");
        int mid = line.indexOf("=");
        String key = null;
        String value = null;
        if (mid > 0) {
            key = line.substring(start + 2, mid);
            value = line.substring(mid + 1);
        } else {
            key = line.substring(start + 2);
        }
        Flag flag = new Flag(key, value);
        if (!validateFlag(flag)) {
            throw new IllegalArgumentException(String.format("%s is not a valid flag", flag.getKey()));
        }
        return flag;
    }

    public abstract boolean validateFlag(Flag flag);
}
