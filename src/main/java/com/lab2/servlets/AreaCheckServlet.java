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
import com.lab2.model.HitCalculator;
import com.lab2.db.DatabaseManager;
import com.lab2.model.RequestResult;
import com.lab2.view.HtmlGenerator;



@WebServlet("/check")
public class AreaCheckServlet extends HttpServlet {


    private boolean validateParameters(BigDecimal yBD, BigDecimal rBD, int xInt, HttpServletResponse resp) {
        if (yBD.compareTo(new BigDecimal("-3")) < 0 || yBD.compareTo(new BigDecimal("5")) > 0) {
            String errorPage = HtmlGenerator.generateErrorPage("Ошибка валидации", "Y должно быть числом от -3 до 5");
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

        if (xInt < -4 || xInt > 4) {
            String errorPage = HtmlGenerator.generateErrorPage("Ошибка валидации", "X должно быть числом от -4 до 4");
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
        int xInt;
        try {
            yBD = (BigDecimal) req.getAttribute("y");
            rBD = (BigDecimal) req.getAttribute("r");
            xInt = (int) req.getAttribute("x");
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


        if (!validateParameters(yBD, rBD, xInt, resp)) {
            return;
        }


        boolean hit = HitCalculator.checkHit(xInt, yBD, rBD);
        long endTime = System.nanoTime();

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
        RequestResult result = new RequestResult(dateFormat.format(new Date()), (endTime - startTime) / 1000.0, hit, xInt, yBD, rBD);
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
