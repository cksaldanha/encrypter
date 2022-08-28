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
    private static final Pattern KEY_ONLY_PATTERN = Pattern.compile("--(?<key>[\\w-]+)(?=\\s)");
    private static final Pattern NO_KEY_PATTERN = Pattern.compile("(?<=\\s)[\\w]");

    public Set<Flag> parseFlags(String line) {
        Set<Flag> flags = new HashSet<>();

        Set<Flag> keyValueFlags = parseKeyValueFlags(line);
        Set<Flag> keyOnlyFlags = parseKeyOnlyFlags(line);
        Flag fileNameFlag = parseFileNamesFlag(line);

        combineFlags(keyValueFlags, flags);
        combineFlags(keyOnlyFlags, flags);
        if (fileNameFlag != null) {
            flags.add(fileNameFlag);
        }

        return flags;
    }

    private void combineFlags(Set<Flag> source, Set<Flag> destination) {
        for (Flag flag : source) {
            if (destination.contains(flag)) {
                throw new IllegalArgumentException(String.format("Ambigious flag: {%s}", flag.getKey()));
            }
            destination.add(flag);
        }
    }

    private String addLineTerminator(String line) {
        if (!line.endsWith("\n")) {
            line = line.concat("\n");
        }
        return line;
    }

    public Set<Flag> parseKeyValueFlags(String line) {
        line = addLineTerminator(line);
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
        line = addLineTerminator(line);
        Matcher matcher = KEY_ONLY_PATTERN.matcher(line);
        Set<Flag> flags = new HashSet<>();
        while (matcher.find()) {
            String key = matcher.group("key");
            flags.add(new Flag(key, null));
        }
        return flags;
    }

    public Flag parseFileNamesFlag(String line) {
        line = addLineTerminator(line);
        Matcher matcher = NO_KEY_PATTERN.matcher(line);
        List<String> fileNames = new ArrayList<>();
        while (matcher.find()) {
            String fileName = matcher.group();
            fileNames.add(fileName);
        }
        return fileNames.size() > 0 ? new Flag("files", fileNames) : null;
    }
}
