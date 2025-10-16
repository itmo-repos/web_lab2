
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
    }
}

function clearTable() {
    const tbody = document.querySelector('.results-table tbody');
    if (tbody) {
        tbody.innerHTML = '';
    }
}


document.addEventListener('DOMContentLoaded', function() {
    const r = localStorage.getItem("r_value");
    const x = localStorage.getItem("x_value");
    const rSelect = document.getElementById('r_value');
    const xSelect = document.getElementById('x_value');
    
    // Список валидных значений для R
    const validRValues = ['1.0', '1.5', '2.0', '2.5', '3.0'];
    // Список валидных значений для X
    const validXValues = ['-4', '-3', '-2', '-1', '0', '1', '2', '3', '4'];

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

    // Проверка и восстановление значения X
    if (x === null || x === '' || !validXValues.includes(x)) {
        if (xSelect) {
            xSelect.value = '';
        }
        // Очищаем невалидное значение из localStorage
        if (x !== null && x !== '' && !validXValues.includes(x)) {
            localStorage.removeItem("x_value");
        }
    } else {
        if (xSelect) {
            xSelect.value = x;
        }
    }

    const clearButton = document.getElementById('clear_table');
    if (clearButton) {
        clearButton.addEventListener('click', function() {
            if (confirm('Вы уверены, что хотите очистить таблицу?')) {
                clearTable();
            }
        });
    }

    // Обработчик для select R
    if (rSelect) {
        rSelect.addEventListener('change', function() {
            updateRValue(this.value);
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
