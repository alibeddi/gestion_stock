package com.polytech.gestionstock.util;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Random;

/**
 * Utility class for generating various types of identifiers
 */
public class IdGenerator {
    
    private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("yyyyMMdd");
    private static final Random RANDOM = new Random();
    
    /**
     * Generates a unique devis number in format: DEV-YYYYMMDD-XXXX
     * where XXXX is a random 4-digit number
     */
    public static String generateDevisNumber() {
        String dateStr = LocalDateTime.now().format(DATE_FORMAT);
        String randomNum = String.format("%04d", RANDOM.nextInt(10000));
        return "DEV-" + dateStr + "-" + randomNum;
    }
    
    /**
     * Generates a unique client account number in format: CLI-YYYYMMDD-XXXX
     */
    public static String generateClientAccountNumber() {
        String dateStr = LocalDateTime.now().format(DATE_FORMAT);
        String randomNum = String.format("%04d", RANDOM.nextInt(10000));
        return "CLI-" + dateStr + "-" + randomNum;
    }
    
    /**
     * Generates a random alpha-numeric string of specified length
     */
    public static String generateRandomString(int length) {
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < length; i++) {
            int index = RANDOM.nextInt(chars.length());
            sb.append(chars.charAt(index));
        }
        return sb.toString();
    }
} 