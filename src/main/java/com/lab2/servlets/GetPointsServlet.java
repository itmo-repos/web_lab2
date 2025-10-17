package com.lab2.servlets;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.inject.Inject;
import java.io.IOException;

import com.lab2.model.ResultsCollectionBean;

@WebServlet("/getPoints")
public class GetPointsServlet extends HttpServlet {

    @Inject
    private ResultsCollectionBean resultsCollection;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");
        String json = resultsCollection.getPointsAsJson();
        resp.getWriter().write(json);
    }
}


