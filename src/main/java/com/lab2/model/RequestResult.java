package com.lab2.model;
import java.math.BigDecimal;

public class RequestResult {
    private final String date;
    private final double executionTime;
    private final boolean hit;
    private final BigDecimal x;
    private final BigDecimal y;
    private final BigDecimal r;
    
    public RequestResult(String date, double executionTime, boolean hit, BigDecimal x, BigDecimal y, BigDecimal r) {
        this.date = date;
        this.executionTime = executionTime;
        this.hit = hit;
        this.x = x;
        this.y = y;
        this.r = r;
    }
    
    public String getDate() { 
        return date; 
    }
    
    public double getExecutionTime() { 
        return executionTime;
    }

    public boolean isHit() { 
        return hit;
    }

    public BigDecimal getX() {
        return x; 
    }

    public BigDecimal getY() { 
        return y;
    }

    public BigDecimal getR() {
        return r;
    }
}