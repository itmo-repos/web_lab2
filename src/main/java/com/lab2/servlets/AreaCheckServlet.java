package com.lab2.servlets;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.ServletException;
import jakarta.servlet.RequestDispatcher;
import java.io.IOException;
import java.util.Date;
import java.text.SimpleDateFormat;

import java.math.BigDecimal;
import java.math.MathContext;
import com.lab2.model.HitCalculator;
import com.lab2.db.DatabaseManager;
import com.lab2.model.RequestResult;
import com.lab2.view.HtmlGenerator;



@WebServlet("/check")
public class AreaCheckServlet extends HttpServlet {

    private static final MathContext MATH_CONTEXT = new MathContext(50);

    private boolean validateParameters(BigDecimal yBD, BigDecimal rBD, BigDecimal xBD, HttpServletResponse resp) {
        if (xBD.abs(MATH_CONTEXT).compareTo(rBD) > 0) {
            String errorPage = HtmlGenerator.generateErrorPage("Ошибка валидации", "X не должно превышать R");
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            resp.setContentType("text/html; charset=UTF-8");
            resp.setCharacterEncoding("UTF-8");
            try {
                resp.getWriter().write(errorPage);
            } catch (IOException e) {   
            }
            
            return false;
        }

        if (yBD.abs(MATH_CONTEXT).subtract(rBD.multiply(new BigDecimal("1.5", MATH_CONTEXT), MATH_CONTEXT)).signum() > 0) {
            String errorPage = HtmlGenerator.generateErrorPage("Ошибка валидации", "Y не должно превышать 1.5R");
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            resp.setContentType("text/html; charset=UTF-8");
            resp.setCharacterEncoding("UTF-8");
            try {
                resp.getWriter().write(errorPage);
            } catch (IOException e) {   
            }
            
            return false;
        }

        if (rBD.compareTo(new BigDecimal("1")) < 0 || rBD.compareTo(new BigDecimal("3")) > 0) {
            String errorPage = HtmlGenerator.generateErrorPage("Ошибка валидации", "R должно быть числом от 1 до 3");
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            resp.setContentType("text/html; charset=UTF-8");
            resp.setCharacterEncoding("UTF-8");
            try {
                resp.getWriter().write(errorPage);
            } catch (IOException e) {
            }

            return false;
        }

        return true;
    }
    
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
        // Проверяем, был ли запрос перенаправлен через forward
        if (req.getAttribute(RequestDispatcher.FORWARD_REQUEST_URI) == null) {
            resp.sendError(404);
            return;
        }
        
        BigDecimal yBD;
        BigDecimal rBD;
        BigDecimal xBD;
        try {
            yBD = (BigDecimal) req.getAttribute("y");
            rBD = (BigDecimal) req.getAttribute("r");
            xBD = (BigDecimal) req.getAttribute("x");
        } catch (Exception e) {
            String errorPage = HtmlGenerator.generateErrorPage("Ошибка параметров", "Y, R и X должны быть установлены");
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            resp.setContentType("text/html; charset=UTF-8");
            resp.setCharacterEncoding("UTF-8");
            resp.getWriter().write(errorPage);
            return;
        }

        long startTime;
        if (req.getAttribute("startTime") == null) {
            startTime = System.nanoTime();
        } else {
            startTime = (long) req.getAttribute("startTime");
        }


        if (!validateParameters(yBD, rBD, xBD, resp)) {
            return;
        }


        boolean hit = HitCalculator.checkHit(xBD, yBD, rBD);
        long endTime = System.nanoTime();

        SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss dd.MM.yyyy");
        RequestResult result = new RequestResult(dateFormat.format(new Date()), (endTime - startTime) / 1000.0, hit, xBD, yBD, rBD);
        DatabaseManager.addResult(result);

        String htmlPage = HtmlGenerator.generateResultPage(result);
        resp.setContentType("text/html; charset=UTF-8");
        resp.setCharacterEncoding("UTF-8");
        resp.getWriter().write(htmlPage);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
        doGet(req, resp);
    }
    
}
