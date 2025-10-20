package com.lab2.model;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Named;

import java.util.ArrayList;
import java.util.List;

import com.lab2.db.DatabaseManager;


@Named("results_collection")
@ApplicationScoped
public class ResultsCollectionBean {

    public List<RequestResult> getResults() {
        return DatabaseManager.loadResults();
    }

    public void addResult(RequestResult result) {
        DatabaseManager.addResult(result);
    }

    public String getPointsAsJson() {
        List<RequestResult> results = getResults();

        if (results == null || results.isEmpty()) {
            return "[]";
        }

        StringBuilder json = new StringBuilder();
        json.append("[");
        for (int i = 0; i < results.size(); i++) {
            RequestResult result = results.get(i);
            json.append("{");
            json.append("\"x\": ").append(result.getX());
            json.append(",\"y\": ").append(result.getY());
            json.append(",\"r\": ").append(result.getR());
            json.append(",\"hit\": ").append(result.isHit());
            json.append("}");
            if (i < results.size() - 1) {
                json.append(",");
            }
        }
        json.append("]");
        return json.toString();
    }

    public void clearCollection() {
        DatabaseManager.saveResults(new ArrayList<>());
    }

}
