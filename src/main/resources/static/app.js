var LAST_SCAN_URL = '/last_scan';
var lastScanTimestamp = 0;
var total = 0;
var current = 0;
var recipes = [];
var components = [];
var container = document.getElementById("container");
var toRender = [];

function logInfo(text) {
    //console.log('Info: ' + text);
}

function logError(text) {
    //console.log('Error: ' + text);
}

function getLoad(url, callback) {
    logInfo('Loading ' + url);
    let xhr = new XMLHttpRequest();
    xhr.open('GET', url);
    xhr.send();
    xhr.onload = function() {
        if (xhr.status != 200) {
            logError('Ошибка ${xhr.status}: ${xhr.statusText}');
        } else {
            callback(xhr.response);
        }
    };
    xhr.onerror = function() {
        logError("Запрос не удался");
    };
}

function renderLastTimestamp() {
    var container = document.getElementById("timestamp"),
        dateFormat = new Date(lastScanTimestamp);
    container.innerHTML = "Последнее обновление: " + dateFormat.getHours() + ":" + dateFormat.getMinutes();
}

function renderProgress() {
    var line = document.getElementById("progress-bar-line"),
        percentsProgress = current / total * 100;
    line.style = "width: " + percentsProgress + "%";
    line.innerHTML = current + "/" + total;
}

function setLastScan(timestamp) {
    logInfo('Last scan: ' + timestamp);
    var newLastScanTimestamp = parseInt(timestamp * 1000),
        timeChanged = newLastScanTimestamp != lastScanTimestamp;
    if (timeChanged) {
        loadPrices();
    }
    lastScanTimestamp = newLastScanTimestamp;
    if (timeChanged) {
        renderLastTimestamp();
    }
}

function loadComponents(callback) {
    getLoad(
        '/components',
        function (newComponents) {
            setComponents(newComponents);
            loadRecipes(callback);
        }
    );
}

function clearContainer() {
    container.innerHTML = "";
}

function calculateDataToRender() {
    var recipeContainer, currentComponentPrices, profit;
    toRender = [];
    for (i in recipes) {
        recipeContainer = {};
        recipe = recipes[i];
        recipeContainer['name'] = recipe['name'];
        recipeContainer['components'] = [];
        for (k in recipe['components']) {
            currentComponent = recipe['components'][k];
            currentComponentPrices = [];
            for (j in prices) {
                price = prices[j];
                if (price['name'] == currentComponent['name']) {
                    currentComponentPrices[currentComponentPrices.length] = price;
                }
            }
            currentComponentPrices.sort(
                function(a, b) {
                    return a['price'] - b['price'];
                }
            );
            recipeContainer['components'][recipeContainer['components'].length] = {
                'name': currentComponent['name'],
                'russianName': currentComponent['russianName'],
                'prices': currentComponentPrices
            };
        }
        profit = 4000;
        for (i in recipeContainer['components']) {
            currentComponent = recipeContainer['components'][i];
            if (currentComponent['prices'].length > 0) {
                profit -= currentComponent['prices'][0]['price'] * 200;
            }
        }
        recipeContainer['profit'] = profit;
        toRender[toRender.length] = recipeContainer;
    }
    toRender.sort(
        function(a, b) {
            return b['profit'] - a['profit'];
        }
    );
}

function renderPrices() {
    clearContainer();
    calculateDataToRender();
    var renderContainer, renderContainerBody, renderContainerTitle, buttons, row,
        button, col, colCollapse, colCollapseBody, collapseName, pricesContainer, priceContainer,
        counter = 0;
    for (i in toRender) {
        recipe = toRender[i];
        renderContainer = document.createElement('div');
        renderContainer.setAttribute('class', 'card bottom_margin');
        renderContainerBody = document.createElement('div');
        renderContainerBody.setAttribute('class', 'card-body');
        renderContainer.appendChild(renderContainerBody);
        renderContainerTitle = document.createElement('h5');
        renderContainerTitle.setAttribute('class', 'card-title');
        renderContainerTitle.innerHTML = recipe['name'] + ' Выгода: ' + recipe['profit'];
        renderContainerBody.appendChild(renderContainerTitle);
        buttons = document.createElement('p');
        row = document.createElement('div');
        row.setAttribute('class', 'row');
        for (j in recipe['components']) {
            component = recipe['components'][j];
            collapseName = 'collapse' + counter
            button = document.createElement('a');
            button.setAttribute('class', 'btn btn-primary right_margin');
            button.setAttribute('data-bs-toggle', 'collapse');
            button.setAttribute('href', '#' + collapseName);
            button.setAttribute('role', 'button');
            button.setAttribute('aria-expanded', 'false');
            button.setAttribute('aria-controls', collapseName);
            button.innerHTML = component['russianName'];
            buttons.appendChild(button);
            col = document.createElement('div');
            col.setAttribute('class', 'col');
            colCollapse = document.createElement('div');
            colCollapse.setAttribute('class', 'collapse multi-collapse');
            colCollapse.setAttribute('id', collapseName);
            colCollapseBody = document.createElement('div');
            colCollapseBody.setAttribute('class', 'card card-body');
            pricesContainer = document.createElement('ul');
            pricesContainer.setAttribute('class', 'list-group');
            console.log(component['prices']);
            for (k in component['prices']) {
                prices = component['prices'][k];
                priceContainer = document.createElement('li');
                priceContainer.setAttribute('class', 'list-group-item');
                priceContainer.innerHTML = prices['price'] + '\t- ' + prices['location'] + ' ' + prices['guild'] + ' ' + prices['time'];
                pricesContainer.appendChild(priceContainer);
            }
            colCollapseBody.appendChild(pricesContainer);
            colCollapse.appendChild(colCollapseBody);
            col.appendChild(colCollapse);
            row.appendChild(col);
            counter += 1;
        }
        renderContainerBody.appendChild(buttons);
        renderContainerBody.appendChild(row);
        container.appendChild(renderContainer);
    }
}

function setComponents(newComponents) {
    components = JSON.parse(newComponents);
}

function loadRecipes(callback) {
    getLoad(
        '/recipes',
        function (newRecipes) {
            setRecipes(newRecipes);
            callback();
        }
    );
}

function setRecipes(newRecipes) {
    recipes = JSON.parse(newRecipes);
}

function setTotal(newTotal) {
    total = newTotal;
    renderProgress();
}

function setCurrent(newCurrent) {
    current = newCurrent;
    renderProgress();
}

function setPrices(newPrices) {
    prices = JSON.parse(newPrices);
    renderPrices();
}

function loadPrices() {
    getLoad('/prices', setPrices);
}

function update() {
    getLoad('/last_scan', setLastScan);
    getLoad('/total', setTotal);
    getLoad('/current', setCurrent);
}

function init() {
    loadComponents(
        function () {
            setInterval(update, 1000);
        }
    );
}