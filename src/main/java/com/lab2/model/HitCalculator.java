package com.lab2.model;

import java.math.BigDecimal;
import java.math.MathContext;



public class HitCalculator {

    private static final MathContext MATH_CONTEXT = new MathContext(50);

    private HitCalculator() {
    }

    public static boolean checkHit(int x, BigDecimal yBD, BigDecimal rBD) {
        BigDecimal xBD = new BigDecimal(x, MATH_CONTEXT);
        BigDecimal zero = BigDecimal.ZERO;
        BigDecimal half = new BigDecimal("0.5", MATH_CONTEXT);
        
        if (x >= 0 && yBD.compareTo(zero) >= 0) {
            // Первая четверть
            BigDecimal rHalf = rBD.multiply(half, MATH_CONTEXT);
            
            return xBD.compareTo(rHalf) <= 0 && yBD.compareTo(rBD) <= 0;
        } else if (x <= 0 && yBD.compareTo(zero) >= 0) {
            // Вторая четверть
            BigDecimal rHalf = rBD.multiply(half, MATH_CONTEXT);
            BigDecimal negX = xBD.negate();

            BigDecimal maxY = rHalf.subtract(negX.multiply(half, MATH_CONTEXT), MATH_CONTEXT);

            return yBD.compareTo(maxY) <= 0;
        } else if (x <= 0 && yBD.compareTo(zero) <= 0) {
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
