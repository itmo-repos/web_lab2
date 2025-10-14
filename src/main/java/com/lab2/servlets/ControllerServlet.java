package com.lab2.servlets;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.ServletException;
import java.io.IOException;

import java.math.BigDecimal;
import com.lab2.model.HitCalculator;


@WebServlet("/")
public class ControllerServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
        String x = req.getParameter("x_value");
        String y = req.getParameter("y_value");
        String r = req.getParameter("r_value");

        if (x == null || y == null || r == null) {
            req.getRequestDispatcher("/index.jsp").forward(req, resp);
            return;
        }

        try {
            int xInt = Integer.parseInt(x);
            BigDecimal yBigDecimal = new BigDecimal(y);
            BigDecimal rBigDecimal = new BigDecimal(r);

            if (yBigDecimal.compareTo(new BigDecimal("-3")) < 0 || yBigDecimal.compareTo(new BigDecimal("5")) > 0) {
                resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                resp.getWriter().write("Y должно быть числом от -3 до 5");
                return;
            }

            if (rBigDecimal.compareTo(new BigDecimal("1")) < 0 || rBigDecimal.compareTo(new BigDecimal("3")) > 0) {
                resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                resp.getWriter().write("R должно быть числом от 1 до 3");
                return;
            }

            if (xInt < -4 || xInt > 4) {
                resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                resp.getWriter().write("X должно быть числом от -4 до 4");
                return;
            }

            boolean hit = HitCalculator.checkHit(xInt, yBigDecimal, rBigDecimal);

            resp.setContentType("application/json");
            resp.setCharacterEncoding("UTF-8");
            resp.getWriter().write(hit ? "true" : "false");
        } catch (NumberFormatException e) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            resp.getWriter().write("X, Y и R должны быть числами");
        }
        
    }
}
