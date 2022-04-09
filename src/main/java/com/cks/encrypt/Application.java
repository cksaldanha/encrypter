/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cks.encrypt;

import com.cks.encrypt.encryption.RSAEncrypter;
import com.cks.encrypt.io.FileIO;
import java.io.IOException;
import java.nio.file.Paths;
import java.security.GeneralSecurityException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Base64;
import java.util.Base64.Encoder;
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
        consHandler.setLevel(Level.ALL);
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
        LOGGER.setLevel(Level.ALL);
        LOGGER.setUseParentHandlers(false);
        LOGGER.info("Application started.");
        
//        LOGGER.info("Generating keys");
//        generateKeyFiles();
//        LOGGER.info("Keys created.");
        
    }
    
    public static void generateKeyFiles() {
        RSAEncrypter rsa = new RSAEncrypter();
        try {
            rsa.generateKeyPair();
            Encoder encoder = Base64.getEncoder();
            byte[] privateKeyBytes = rsa.getPrivateKey();
            byte[] privateKeyBytesEncoded = encoder.encode(privateKeyBytes);
            
            byte[] publicKeyBytes = rsa.getPublicKey();
            byte[] publicKeyBytesEncoded = encoder.encode(publicKeyBytes);
            
            FileIO.write(Paths.get("private.txt"), privateKeyBytesEncoded);
            FileIO.write(Paths.get("public.txt"), publicKeyBytesEncoded);
            
        } catch (GeneralSecurityException x) {
            LOGGER.severe(x.getMessage());
        } catch (IOException x) {
            LOGGER.severe(x.getMessage());
        }
        
    }
}
