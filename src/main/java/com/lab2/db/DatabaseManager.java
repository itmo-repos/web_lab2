package com.lab2.db;

import java.io.*;
import java.nio.file.*;
import java.util.*;
import java.math.BigDecimal;
import java.math.MathContext;

import com.lab2.model.RequestResult;
import java.util.concurrent.CopyOnWriteArrayList;

public class DatabaseManager {
    private static final String DB_DIR = "/home/studs/s467669/lab2/db";
    private static final String TABLE_FILE = "table.txt";
    private static final String FILE_PATH = DB_DIR + "/" + TABLE_FILE;
    
    // Используем тот же MathContext, что и в Main.java
    private static final MathContext MATH_CONTEXT = new MathContext(50);

    public static synchronized void addResult(RequestResult result) {
        if (result == null) {
            return;
        }

        try {
            // Создаем директорию, если её нет
            checkDir();
            
            Path filePath = Paths.get(FILE_PATH);
            
            try (BufferedWriter writer = Files.newBufferedWriter(filePath, StandardOpenOption.CREATE, StandardOpenOption.APPEND)) {
                String line = formatResultToLine(result);
                writer.write(line);
                writer.newLine();
            }
        } catch (IOException e) {
        }
    }
    
    public static synchronized List<RequestResult> loadResults() {
        try {
            checkDir();
            
            Path filePath = Paths.get(FILE_PATH);
            if (!Files.exists(filePath)) {
                return new CopyOnWriteArrayList<RequestResult>();
            }
            
            List<RequestResult> results = new ArrayList<RequestResult>();
            
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
            
            return new CopyOnWriteArrayList<RequestResult>(results);
            
        } catch (IOException e) {
            return new CopyOnWriteArrayList<RequestResult>();
        }
    }
    
    public static synchronized void saveResults(List<RequestResult> results) {
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
    
    private static synchronized void checkDir() {
        try {
            Path dbDir = Paths.get(DB_DIR);
            if (!Files.exists(dbDir)) {
                Files.createDirectories(dbDir);
            }
        } catch (IOException e) {
        }
    }
    
    private static synchronized RequestResult parseResultFromLine(String line) {
        String[] parts = line.split("\\|");
        if (parts.length != 6) {
            return null;
        }
        
        try {
            String date = parts[0];
            double executionTime = Double.parseDouble(parts[1]);
            boolean hit = Boolean.parseBoolean(parts[2]);
            BigDecimal x = new BigDecimal(parts[3], MATH_CONTEXT);
            BigDecimal y = new BigDecimal(parts[4], MATH_CONTEXT);
            BigDecimal r = new BigDecimal(parts[5], MATH_CONTEXT);
            
            return new RequestResult(date, executionTime, hit, x, y, r);
        } catch (NumberFormatException e) {
            return null;
        }
    }
    
    private static synchronized String formatResultToLine(RequestResult result) {
        return String.format(java.util.Locale.US, "%s|%.2f|%s|%s|%s|%s",
            result.getDate(),
            result.getExecutionTime(),
            result.isHit(),
            result.getX().toPlainString(),
            result.getY().toPlainString(),
            result.getR().toPlainString()
        );
    }
}
