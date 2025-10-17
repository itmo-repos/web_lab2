function updateRValue(rValue) {
    localStorage.setItem("r_value", rValue);
    const rText = document.querySelector('.r-value');
    if (rText) {
        if (rValue) {
            rText.textContent = `R = ${rValue}`;
        } else {
            rText.textContent = 'R = ?';
        }
    }
}

function redrawChart() {
    const svg = document.getElementById("svg-chart");
    if (svg) {
        const rValue = localStorage.getItem("r_value");
        if (!rValue) {
            return;
        }

        fetch('/getPoints')
            .then(response => response.json())
            .then(points => {
                clearPoints();
                
                points.forEach((point_data) => {
                    const point = document.createElementNS("http://www.w3.org/2000/svg", "circle");
                    point.setAttribute("cx", point_data.x/rValue*120.0 + 150);
                    point.setAttribute("cy", 150 - point_data.y/rValue*120.0);
                    point.setAttribute("r", "2");
                    point.setAttribute("class", point_data.hit ? "point-hit" : "point-miss");
                    svg.appendChild(point);
                });
            })
            .catch(err => console.error('Ошибка загрузки точек:', err));
    }
}

function showFormError(message) {
    const sendBtn = document.getElementById('send_button');
    if (!sendBtn) return;
    let errorEl = document.getElementById('form_error');
    if (!errorEl) {
        errorEl = document.createElement('div');
        errorEl.id = 'form_error';
        errorEl.style.color = 'red';
        if (sendBtn.parentNode) {
            sendBtn.parentNode.insertBefore(errorEl, sendBtn.nextSibling);
        }
    }
    errorEl.textContent = message;
}

function clearFormError() {
    const errorEl = document.getElementById('form_error');
    if (errorEl) errorEl.remove();
}


function showYValueError(yRow, text) {
    const newRow = document.createElement('tr');
    newRow.id = 'y_error';
    newRow.classList.add('y_error');
    newRow.innerHTML = `<td>${text}</td>`;
    yRow.parentNode.insertBefore(newRow, yRow.nextSibling);
}


function addResultRow(date, executionTime, hit, x, y, r) {
    const tbody = document.querySelector('.results-table tbody');
    if (tbody) {
        // Проверяем, был ли пользователь внизу таблицы
        const tableContainer = tbody.closest('.table-container');
        const wasAtBottom = tableContainer && 
            (tableContainer.scrollTop + tableContainer.clientHeight >= tableContainer.scrollHeight - 10);
        
        const row = document.createElement('tr');
        row.innerHTML = `
            <td>${date}</td>
            <td>${executionTime.toFixed(2)} мкс</td>
            <td><input type="checkbox" ${hit ? 'checked' : ''} readonly></td>
            <td>${x}</td>
            <td>${parseFloat(y).toFixed(2)}</td>
            <td>${r}</td>
        `;
        tbody.appendChild(row);
        
        // Прокручиваем к концу, если пользователь был внизу
        if (wasAtBottom && tableContainer) {
            tableContainer.scrollTop = tableContainer.scrollHeight;
        }
    }
}

function clearTable() {
    const tbody = document.querySelector('.results-table tbody');
    if (tbody) {
        tbody.innerHTML = '';
    }
}

function clearPoints() {
    const svg = document.getElementById("svg-chart");
    if (svg) {
        const points = svg.querySelectorAll('circle');
        points.forEach(point => {
            point.remove();
        });
    }
}


function parseHtmlResult(htmlResponse) {
    try {
        const parser = new DOMParser();
        const doc = parser.parseFromString(htmlResponse, 'text/html');
        
        const resultTable = doc.querySelector('.result-table tbody');
        if (!resultTable) {
            console.error('Не удалось найти таблицу результата в HTML ответе');
            return null;
        }
        
        const rows = resultTable.querySelectorAll('tr');
        const result = {};
        
        rows.forEach(row => {
            const cells = row.querySelectorAll('td');
            if (cells.length >= 2) {
                const parameter = cells[0].textContent.trim();
                const value = cells[1].textContent.trim();
                
                switch (parameter) {
                    case 'Дата':
                        result.date = value;
                        break;
                    case 'X':
                        result.x = value;
                        break;
                    case 'Y':
                        result.y = value;
                        break;
                    case 'R':
                        result.r = value;
                        break;
                    case 'Время выполнения':
                        result.executionTime = parseFloat(value.replace(' мкс', ''));
                        break;
                    case 'Результат':
                        result.hit = value === 'ПОПАДАНИЕ';
                        break;
                }
            }
        });
        
        return result;
    } catch (error) {
        console.error('Ошибка при парсинге HTML ответа:', error);
        return null;
    }
}

function showResult(htmlResponse) {
    const result = parseHtmlResult(htmlResponse);
    if (result && result.date && result.x && result.y && result.r && result.executionTime !== undefined) {
        const svg = document.getElementById("svg-chart");
        const point = document.createElementNS("http://www.w3.org/2000/svg", "circle");

        point.setAttribute("cx", result.x/result.r*120.0 + 150);
        point.setAttribute("cy", 150 - result.y/result.r*120.0);
        point.setAttribute("r", "2");
        point.setAttribute("class", result.hit ? "point-hit" : "point-miss");

        svg.appendChild(point);

        addResultRow(result.date, result.executionTime, result.hit, result.x, result.y, result.r);
    } else {
        console.error('Не удалось извлечь данные результата из HTML ответа');
    }
}

function loadRValue () {
    const r = localStorage.getItem("r_value");
    const rSelect = document.getElementById('r_value');

    // Список валидных значений для R
    const validRValues = ['1.0', '1.5', '2.0', '2.5', '3.0'];

    // Проверка и восстановление значения R
    if (r === null || r === '' || !validRValues.includes(r)) {
        // Если в localStorage нет значения или оно невалидно, устанавливаем дефолтное "Выберите значение"
        if (rSelect) {
            rSelect.value = '';
        }
        const rText = document.querySelector('.r-value');
        if (rText) {
            rText.textContent = 'R = ?';
        }
        // Очищаем невалидное значение из localStorage
        if (r !== null && r !== '' && !validRValues.includes(r)) {
            localStorage.removeItem("r_value");
        }
    } else {
        updateRValue(r);
        document.getElementById('r_value').value = r;
    }
}


document.addEventListener('DOMContentLoaded', function() {

    loadRValue();
    redrawChart();

    const clearButton = document.getElementById('clear_points');
    if (clearButton) {
        clearButton.addEventListener('click', function() {
            if (confirm('Вы уверены, что хотите очистить все точки?')) {
                clearTable();
                clearPoints();
                fetch('/clear', {
                    method: 'POST',
                    headers: {
                        'Content-Type': 'application/x-www-form-urlencoded',
                    },
                })
                .then(response => response.text())
            }
        });
    }

    const svgChart = document.getElementById('svg-chart');
    if (svgChart) {
        svgChart.addEventListener('click', function(e) {

            const rValue = localStorage.getItem("r_value");
            if (!rValue) {
                showFormError('Выберите значение R, чтобы кликать по графику');
                return;
            }
            const r = parseFloat(rValue);

            clearFormError();

            const clientX = e.clientX;
            const clientY = e.clientY;
        
            const point = new DOMPoint(clientX, clientY);
        
            const ctm = svgChart.getScreenCTM();
        
            const svgPoint = point.matrixTransform(ctm.inverse());

            const x = (svgPoint.x - 150) / 120 * r;
            const y = (150 - svgPoint.y) / 120 * r;

            fetch('/main', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/x-www-form-urlencoded',
                },
                body: `x_value=${x}&y_value=${y}&r_value=${r}`,
            })
            .then(response => response.text())
            .then(data => {
                console.log(data);
                showResult(data);
            })
            .catch(error => {
                console.error('Error:', error);
            });
        });  
    }

    // Обработчик для select R
    const rSelect = document.getElementById('r_value');
    if (rSelect) {
        rSelect.addEventListener('change', function() {
            updateRValue(this.value);
            redrawChart();
        });
    }

    // Валидация поля Y
    const yInput = document.getElementById('y_value');
    if (yInput) {
        yInput.addEventListener('input', function() {
			const normalized = this.value.replace(',', '.');
			const errorRow = document.getElementById('y_error');
            if (errorRow) errorRow.remove();

			if (this.value === '') {	
				return;
			}

			if (!/^-?\d+(\.\d+)?$/.test(normalized)) {
                showYValueError(this.closest('tr'), "Y должно быть числом");
				return;
			}

			const parts = normalized.split('.');
			const integerPart = parts[0];
			const decimalPart = (parts[1] || '').padEnd(20, '0').slice(0, 20);
			const scaled = BigInt(integerPart + decimalPart);
			const minScaled = BigInt('-3' + '0'.repeat(20));
			const maxScaled = BigInt('5' + '0'.repeat(20));

            if (errorRow) errorRow.remove();

            if (parts[1] && parts[1].length > 20) {
                showYValueError(this.closest('tr'), "Максимальная точность Y: 20 знаков после запятой");
            } else if (scaled < minScaled || scaled > maxScaled) {
                showYValueError(this.closest('tr'), "Y должно быть числом от -3 до 5");
			}
        });
    }
    
});

const mainForm = document.getElementById('main_form');
if (mainForm) {
    mainForm.addEventListener('submit', function(e) {
        e.preventDefault();
        
        const xSelect = document.getElementById('x_value');
		const x = xSelect && xSelect.value ? parseInt(xSelect.value) : NaN;
		const yRaw = document.getElementById('y_value').value;
		const yNormalized = yRaw.replace(',', '.');
        const rSelect = document.getElementById('r_value');
        const r = rSelect && rSelect.value ? parseFloat(rSelect.value) : NaN;

		let yValid = true;
		if (yNormalized === '' || !/^-?\d+(\.\d+)?$/.test(yNormalized)) {
			yValid = false;
		} else {
			const parts = yNormalized.split('.');
			const integerPart = parts[0];
			const decimalPart = (parts[1] || '').padEnd(20, '0').slice(0, 20);
			const scaled = BigInt(integerPart + decimalPart);
			const minScaled = BigInt('-3' + '0'.repeat(20));
			const maxScaled = BigInt('5' + '0'.repeat(20));
			if (scaled < minScaled || scaled > maxScaled) yValid = false;
		}

		if (isNaN(x) || !yValid || isNaN(r) || document.getElementById('y_error')) {
            let errorMessage = '';
            if (isNaN(x)) errorMessage = 'Выберите значение X';
			else if (!yValid) errorMessage = 'Введите корректное значение Y';
            else if (isNaN(r)) errorMessage = 'Выберите значение R';
            else if (document.getElementById('y_error')) errorMessage = 'Исправьте ошибку в поле Y';
            
            showFormError(errorMessage);
            return;
        }
        
        clearFormError();

        e.target.submit();
        
    });
}
