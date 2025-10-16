package com.lab2.servlets;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.ServletException;
import java.io.IOException;

import com.lab2.db.DatabaseManager;
import com.lab2.model.RequestResult;
import java.util.ArrayList;


@WebServlet("/clear")
public class ClearServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
        DatabaseManager.saveTable(new ArrayList<RequestResult>());
        resp.setContentType("text/html; charset=UTF-8");
        resp.setCharacterEncoding("UTF-8");
        resp.getWriter().write("Таблица очищена");
    }

}
