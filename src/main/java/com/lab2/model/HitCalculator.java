package com.lab2.model;

import java.math.BigDecimal;
import java.math.MathContext;



public class HitCalculator {

    private static final MathContext MATH_CONTEXT = new MathContext(50);

    private HitCalculator() {
    }

    public static boolean checkHit(BigDecimal xBD, BigDecimal yBD, BigDecimal rBD) {
        BigDecimal half = new BigDecimal("0.5", MATH_CONTEXT);
        
        if (xBD.signum() >= 0 && yBD.signum() >= 0) {
            // Первая четверть
            BigDecimal rHalf = rBD.multiply(half, MATH_CONTEXT);
            
            return xBD.compareTo(rHalf) <= 0 && yBD.compareTo(rBD) <= 0;
        } else if (xBD.signum() <= 0 && yBD.signum() >= 0) {
            // Вторая четверть
            BigDecimal rHalf = rBD.multiply(half, MATH_CONTEXT);
            BigDecimal negX = xBD.negate();

            BigDecimal maxY = rHalf.subtract(negX.multiply(half, MATH_CONTEXT), MATH_CONTEXT);

            return yBD.compareTo(maxY) <= 0;
        } else if (xBD.signum() <= 0 && yBD.signum() <= 0) {
            // Третья четверть
            BigDecimal xSquared = xBD.multiply(xBD, MATH_CONTEXT);
            BigDecimal ySquared = yBD.multiply(yBD, MATH_CONTEXT);
            BigDecimal rSquared = rBD.multiply(rBD, MATH_CONTEXT);
            BigDecimal sum = xSquared.add(ySquared, MATH_CONTEXT);

            return sum.compareTo(rSquared) <= 0;
        } else {
            // Четвертая четверть: всегда false
            return false;
        }
    }
    
    
}
