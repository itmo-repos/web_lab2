<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Lab2</title>
    <link rel="stylesheet" href="css/chart.css">
    <link rel="stylesheet" href="css/main.css">
</head>
<body>
    <table cellpadding="0" cellspacing="0" width="100%" class="main-table">
        <tr>
            <th colspan=2 class="page-header">
                Тарасов Савелий Дмитриевич, P3206<br>
                Лабораторная работа №2, вариант 753823
            </th>
        </tr>
        <tr>
            <td width="60%" valign="top">
                <svg width="600" height="600" viewBox="-5 -5 310 310" class="svg-chart">
                    <text x="5" y="5" fill="black" class="r-value">R = ?</text>

                    <!-- body -->

                    <path d="M 150 150 L 150 270 A 120 120 0 0 1 30 150 L 150 150" class="graph-body" />
                    <rect x="150" y="30" width="60" height="120" class="graph-body"/>
                    <polygon points="150,150 150,90 30,150" class="graph-body"/>

                    <!-- Y -->
                    <line x1="150" y1="300" x2="150" y2="0" stroke="black"/>
                    <line x1="145" y1="10" x2="150" y2="0" stroke="black"/>
                    <line x1="155" y1="10" x2="150" y2="0" stroke="black"/>

                    <line x1="155" y1="270" x2="145" y2="270" stroke="black"/>
                    <line x1="155" y1="210" x2="145" y2="210" stroke="black"/>
                    <line x1="155" y1="90" x2="145" y2="90" stroke="black"/>                    
                    <line x1="155" y1="30" x2="145" y2="30" stroke="black"/>

                    <text x="158" y="30" fill="black" class="y-axis-label">R</text>
                    <text x="158" y="90" fill="black" class="y-axis-label">R/2</text>
                    <text x="158" y="210" fill="black" class="y-axis-label">-R/2</text>
                    <text x="158" y="270" fill="black" class="y-axis-label">-R</text>

                    <!-- X -->
                    <line x1="0" y1="150" x2="300" y2="150" stroke="black"/>
                    <line x1="290" y1="155" x2="300" y2="150" stroke="black"/>
                    <line x1="290" y1="145" x2="300" y2="150" stroke="black"/>


                    <line x1="270" y1="155" x2="270" y2="145" stroke="black"/>
                    <line x1="210" y1="155" x2="210" y2="145" stroke="black"/>
                    <line x1="90" y1="155" x2="90" y2="145" stroke="black"/>
                    <line x1="30" y1="155" x2="30" y2="145" stroke="black"/>

                    <text x="30" y="142" fill="black" class="x-axis-label">-R</text>
                    <text x="90" y="142" fill="black" class="x-axis-label">-R/2</text>
                    <text x="210" y="142" fill="black" class="x-axis-label">R/2</text>
                    <text x="270" y="142" fill="black" class="x-axis-label">R</text>

                    <!-- axes names -->
                    <text x="290" y="140" fill="black" class="axis-label">x</text>
                    <text x="160" y="10" fill="black" class="axis-label">y</text>
                </svg>
            </td>
            <td valign="top">
            <table border="0" cellpadding="5" cellspacing="0">
                <tr>
                    <td width="40%">
                        <!-- Таблица установщиков X, Y, R с тремя строками -->
                        <form id="main_form" action="/main" method="POST">
                            <table border="0" cellpadding="5" cellspacing="0">
                                            <tr>
                                                <td>
                                                    <strong>Значение для X:</strong>
                                                    <br>
                                                    <select id="x_value" name="x_value">
                                                        <option value="">Выберите значение</option>
                                                        <option value="-4">-4</option>
                                                        <option value="-3">-3</option>
                                                        <option value="-2">-2</option>
                                                        <option value="-1">-1</option>
                                                        <option value="0">0</option>
                                                        <option value="1">1</option>
                                                        <option value="2">2</option>
                                                        <option value="3">3</option>
                                                        <option value="4">4</option>
                                                    </select>
                                                </td>
                                            </tr>
                                            <tr>
                                                <td>
                                                    <strong>Значение для Y [-3; 5]:</strong>
                                                    <br>
                                                    <input type="text" id="y_value" name="y_value"/>
                                                </td>
                                            </tr>
                                            <tr>
                                                <td>
                                                    <strong>Установить значение R:</strong>
                                                    <br>
                                                    <select id="r_value" name="r_value">
                                                        <option value="">Выберите значение</option>
                                                        <option value="1.0">1.0</option>
                                                        <option value="1.5">1.5</option>
                                                        <option value="2.0">2.0</option>
                                                        <option value="2.5">2.5</option>
                                                        <option value="3.0">3.0</option>
                                                    </select>
                                                </td>
                                            </tr>
                                            <tr>
                                                <td>
                                                    <strong>Отправить данные:</strong>
                                                    <br>
                                                    <input type="submit" id="send_button" value="Отправить" />
                                                </td>
                                            </tr>
                                        </table>
                        </td>
                    </form>
                </tr>
                <tr>
                    <td>
                        <div class="table-container">
                            <table class="results-table">
                                <thead>
                                    <tr>
                                        <th>Дата</th>
                                        <th>Время работы</th>
                                        <th>Результат</th>
                                        <th>X</th>
                                        <th>Y</th>
                                        <th>R</th>
                                    </tr>
                                </thead>
                                <tbody>
                                    <!-- Строки с результатами -->
                                </tbody>
                            </table>
                        </div>
                    </td>
                </tr>
                <tr>
                    <td>
                        <input type="button" id="clear_table" value="Очистить таблицу"/>
                    </td>
                </tr>
            </table>
            </td>
        </tr>
    </table>
    <script src="script.js"></script>
</body>
</html>
