package com.lab2.db;

import java.io.*;
import java.nio.file.*;
import java.util.*;
import java.math.BigDecimal;
import java.math.MathContext;

import com.lab2.model.RequestResult;

public class DatabaseManager {
    private static final String DB_DIR = "db";
    private static final String TABLE_FILE = "table.txt";
    private static final String FILE_PATH = DB_DIR + "/" + TABLE_FILE;
    
    // Используем тот же MathContext, что и в Main.java
    private static final MathContext MATH_CONTEXT = new MathContext(50);

    public static void addResult(RequestResult result) {
        List<RequestResult> results = loadTable();
        results.add(result);
        saveTable(results);
    }
    
    public static List<RequestResult> loadTable() {
        try {
            checkDir();
            
            Path filePath = Paths.get(FILE_PATH);
            if (!Files.exists(filePath)) {
                return new ArrayList<>();
            }
            
            List<RequestResult> results = new ArrayList<>();
            
            try (BufferedReader reader = Files.newBufferedReader(filePath)) {
                String line;
                while ((line = reader.readLine()) != null) {
                    if (line.trim().isEmpty()) {
                        continue; // Пропускаем пустые строки
                    }
                    
                    try {
                        RequestResult result = parseResultFromLine(line);
                        if (result != null) {
                            results.add(result);
                        }
                    } catch (Exception e) {
                        // Пропускаем некорректные строки
                    }
                }
            }
            
            return results;
            
        } catch (IOException e) {
            return new ArrayList<>();
        }
    }
    
    public static void saveTable(List<RequestResult> results) {
        if (results == null) {
            return;
        }
        
        try {
            // Создаем директорию, если её нет
            checkDir();
            
            Path filePath = Paths.get(FILE_PATH);
            
            try (BufferedWriter writer = Files.newBufferedWriter(filePath, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING)) {
                for (RequestResult result : results) {
                    String line = formatResultToLine(result);
                    writer.write(line);
                    writer.newLine();
                }
            }
            
        } catch (IOException e) {
        }
    }
    
    private static void checkDir() {
        try {
            Path dbDir = Paths.get(DB_DIR);
            if (!Files.exists(dbDir)) {
                Files.createDirectories(dbDir);
            }
        } catch (IOException e) {
        }
    }
    
    private static RequestResult parseResultFromLine(String line) {
        String[] parts = line.split("\\|");
        if (parts.length != 6) {
            return null;
        }
        
        try {
            String date = parts[0];
            double executionTime = Double.parseDouble(parts[1]);
            boolean hit = Boolean.parseBoolean(parts[2]);
            int x = Integer.parseInt(parts[3]);
            BigDecimal y = new BigDecimal(parts[4], MATH_CONTEXT);
            BigDecimal r = new BigDecimal(parts[5], MATH_CONTEXT);
            
            return new RequestResult(date, executionTime, hit, x, y, r);
        } catch (NumberFormatException e) {
            return null;
        }
    }
    
    private static String formatResultToLine(RequestResult result) {
        return String.format(java.util.Locale.US, "%s|%.2f|%s|%d|%s|%s",
            result.getDate(),
            result.getExecutionTime(),
            result.isHit(),
            result.getX(),
            result.getY().toPlainString(),
            result.getR().toPlainString()
        );
    }
}
