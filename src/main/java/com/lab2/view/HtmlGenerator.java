package com.lab2.view;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Locale;

import com.lab2.model.RequestResult;

public class HtmlGenerator {
    private HtmlGenerator() {
    }

    public static String generateResultPage(RequestResult result) {
        StringBuilder html = new StringBuilder();

        int x = result.getX();
        BigDecimal y = result.getY();
        BigDecimal r = result.getR();
        double executionTime = result.getExecutionTime();
        boolean hit = result.isHit();
        
        html.append("<!DOCTYPE html>\n");
        html.append("<html>\n");
        html.append("<head>\n");
        html.append("    <meta charset=\"UTF-8\">\n");
        html.append("    <title>Результат проверки</title>\n");
        html.append("    <link rel=\"stylesheet\" href=\"css/result_page.css\">\n");
        html.append("</head>\n");
        html.append("<body>\n");
        html.append("    <div class=\"result-page\">\n");
        html.append("        <h1 class=\"result-header\">Результат проверки</h1>\n");
        
        // Таблица с параметрами и результатом
        html.append("        <table class=\"result-table\">\n");
        html.append("            <thead>\n");
        html.append("                <tr>\n");
        html.append("                    <th>Параметр</th>\n");
        html.append("                    <th>Значение</th>\n");
        html.append("                </tr>\n");
        html.append("            </thead>\n");
        html.append("            <tbody>\n");
        html.append("                <tr>\n");
        html.append("                    <td>X</td>\n");
        html.append("                    <td class=\"result-value\">").append(x).append("</td>\n");
        html.append("                </tr>\n");
        html.append("                <tr>\n");
        html.append("                    <td>Y</td>\n");
        html.append("                    <td class=\"result-value\">").append(y.setScale(3, RoundingMode.HALF_UP)).append("</td>\n");
        html.append("                </tr>\n");
        html.append("                <tr>\n");
        html.append("                    <td>R</td>\n");
        html.append("                    <td class=\"result-value\">").append(r.setScale(3, RoundingMode.HALF_UP)).append("</td>\n");
        html.append("                </tr>\n");
        html.append("                <tr>\n");
        html.append("                    <td>Время выполнения</td>\n");
        html.append("                    <td class=\"result-value\">").append(String.format(Locale.US, "%.2f", executionTime)).append(" мкс</td>\n");
        html.append("                </tr>\n");
        html.append("                <tr>\n");
        html.append("                    <td>Результат</td>\n");
        html.append("                    <td class=\"hit-result ").append(hit ? "hit-true" : "hit-false").append("\">");
        html.append(hit ? "ПОПАДАНИЕ" : "НЕ ПОПАДАНИЕ");
        html.append("                    </td>\n");
        html.append("                </tr>\n");
        html.append("            </tbody>\n");
        html.append("        </table>\n");
        
        // Ссылка на главную страницу
        html.append("        <div class=\"link-container\">\n");
        html.append("            <a href=\"/\" class=\"back-link\">Вернуться к форме</a>\n");
        html.append("        </div>\n");
        
        html.append("    </div>\n");
        html.append("</body>\n");
        html.append("</html>");
        
        return html.toString();
    }
    
    public static String generateErrorPage(String errorMessage, String details) {
        StringBuilder html = new StringBuilder();
        
        html.append("<!DOCTYPE html>\n");
        html.append("<html>\n");
        html.append("<head>\n");
        html.append("    <meta charset=\"UTF-8\">\n");
        html.append("    <title>Ошибка</title>\n");
        html.append("    <link rel=\"stylesheet\" href=\"css/result_page.css\">\n");
        html.append("</head>\n");
        html.append("<body>\n");
        html.append("    <div class=\"error-page\">\n");
        html.append("        <h1 class=\"error-header\">Ошибка</h1>\n");
        html.append("        <div class=\"error-message\">").append(errorMessage).append("</div>\n");
        if (details != null && !details.isEmpty()) {
            html.append("        <div class=\"error-details\">").append(details).append("</div>\n");
        }
        html.append("        <div class=\"link-container\">\n");
        html.append("            <a href=\"/\" class=\"back-link\">Вернуться к форме</a>\n");
        html.append("        </div>\n");
        html.append("    </div>\n");
        html.append("</body>\n");
        html.append("</html>");
        
        return html.toString();
    }
}
