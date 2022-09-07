/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cks.encrypt;

import com.cks.encrypt.cli.ArgsParser;
import com.cks.encrypt.cli.command.Command;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.logging.*;

/**
 *
 * @author colin.saldanha
 */
public class Application {

    /*
    date
    warn/info
    class
    method
    message
     */
    private static final String ONE_LINE_FORM = "[%s] [%-7s] [%-20s] [%-20s] : %s%n";
    private static final Logger LOGGER = Logger.getLogger("com.cks");

    public static void main(String[] args) {
        //setup logging
        ConsoleHandler consHandler = new ConsoleHandler();
        consHandler.setLevel(Level.OFF);
        consHandler.setFormatter(new SimpleFormatter() {
            @Override
            public String format(LogRecord record) {
                Instant instant = Instant.ofEpochMilli(record.getMillis());
                LocalDateTime ldt = LocalDateTime.ofInstant(instant, ZoneId.systemDefault());
                DateTimeFormatter dtFormatter = DateTimeFormatter.ISO_DATE_TIME;

                return String.format(
                        ONE_LINE_FORM,
                        dtFormatter.format(ldt),
                        record.getLevel(),
                        record.getSourceClassName(),
                        record.getSourceMethodName(),
                        record.getMessage()
                );
            }
        });
        LOGGER.addHandler(consHandler);
        LOGGER.setLevel(Level.OFF);
        LOGGER.setUseParentHandlers(false);
        LOGGER.info("Application started.");

        //Add command line logic
        try {
            try {
                Command command = ArgsParser.parseArgs(args);
                command.execute();

            } catch (Exception x) {
                System.out.println(x.getMessage());
                throw new IllegalArgumentException(x);
            }

        } catch (IllegalArgumentException x) {
            usage();
            LOGGER.severe(x.getMessage());
        }
    }

    public static void usage() {
        System.out.printf("java encrypter <command> <arguments>\n");
        System.out.printf("\tCommands:\n");
        System.out.printf("\t\tencrypt <--mode=aes | rsa> <--keypath=keyFileName> [--type=public | private] <file> [additional files]\n");
        System.out.printf("\t\tdecrypt <--mode=aes | rsa> <--keypath=keyFileName> [--type=public | private] <file> [additional files]\n");
        System.out.printf("\t\taes [keyFileName]\n");
        System.out.printf("\t\trsa [--public=keyFileName --private=keyFileName]\n");
    }
}
