/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cks.encrypt.cli;

import java.nio.file.Paths;
import java.util.HashSet;
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
    private static final Pattern KEY_VALUE_PATTERN = Pattern.compile("--(?<key>[\\w-]+)(?==(?<value>[.[^\\s]]+(?=\\s*)))");
    private static final Pattern KEY_ONLY_PATTERN = Pattern.compile("--(?<key>[\\w-]+)(?=\\s)");
    private static final Pattern NO_KEY_PATTERN = Pattern.compile("(?<=\\s)[^\\s*][^-][^-][.[^\\s]]+(?=\\s)");

    public static Set<Flag<?>> parseFlags(String line) {
        Set<Flag<?>> flags = new HashSet<>();

        Set<KeyValueFlag> keyValueFlags = parseKeyValueFlags(line);
        Set<KeyOnlyFlag> keyOnlyFlags = parseKeyOnlyFlags(line);
        FilesFlag filesFlag = parseFileNamesFlag(line);

        combineFlags(flags, keyValueFlags);
        combineFlags(flags, keyOnlyFlags);
        if (filesFlag != null) {
            flags.add(filesFlag);
        }

        return flags;
    }

    private static <T extends Flag<?>> void combineFlags(Set<Flag<?>> list, Set<T> additional) {
        for (Flag<?> flag : additional) {
            if (list.contains(flag)) {
                throw new IllegalArgumentException(String.format("Ambigious flag: {%s}", flag.getKey()));
            }
            list.add(flag);
        }
    }

    private static String addLineTerminator(String line) {
        if (!line.endsWith("\n")) {
            line = line.concat("\n");
        }
        return line;
    }

    public static Set<KeyValueFlag> parseKeyValueFlags(String line) {
        line = addLineTerminator(line);
        Matcher matcher = KEY_VALUE_PATTERN.matcher(line);
        Set<KeyValueFlag> flags = new HashSet<>();
        while (matcher.find()) {
            String key = matcher.group("key");
            String value = matcher.group("value");
            flags.add(new KeyValueFlag(key, value));
        }
        return flags;
    }

    public static Set<KeyOnlyFlag> parseKeyOnlyFlags(String line) {
        line = addLineTerminator(line);
        Matcher matcher = KEY_ONLY_PATTERN.matcher(line);
        Set<KeyOnlyFlag> flags = new HashSet<>();
        while (matcher.find()) {
            String key = matcher.group("key");
            flags.add(new KeyOnlyFlag(key));
        }
        return flags;
    }

    public static FilesFlag parseFileNamesFlag(String line) {
        line = addLineTerminator(line);
        Matcher matcher = NO_KEY_PATTERN.matcher(line);
        FilesFlag filesFlag = new FilesFlag();
        while (matcher.find()) {
            String fileName = matcher.group();
            filesFlag.add(Paths.get(fileName));
        }
        return filesFlag.size() > 0 ? filesFlag : null;
    }
}
