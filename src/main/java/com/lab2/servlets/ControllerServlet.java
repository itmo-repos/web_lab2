package com.lab2.servlets;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.ServletException;
import java.io.IOException;

import java.math.BigDecimal;

@WebServlet("/main")
public class ControllerServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
        long startTime = System.nanoTime();


        String x = req.getParameter("x_value");
        String y = req.getParameter("y_value");
        String r = req.getParameter("r_value");

        if (x == null || y == null || r == null) {
            req.getRequestDispatcher("/index.jsp").forward(req, resp);
            return;
        }

        try {
            int xInt = Integer.parseInt(x);
            BigDecimal yBD = new BigDecimal(y);
            BigDecimal rBD = new BigDecimal(r);

            req.setAttribute("x", xInt);
            req.setAttribute("y", yBD);
            req.setAttribute("r", rBD);
            req.setAttribute("startTime", startTime);

            req.getRequestDispatcher("/check").forward(req, resp);
        } catch (NumberFormatException e) {
            req.getRequestDispatcher("/index.jsp").forward(req, resp);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
        doGet(req, resp);
    }

}
