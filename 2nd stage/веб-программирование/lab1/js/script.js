let X, Y, R;
const GRAPH_WIDTH = 300;
const GRAPH_HEIGHT = 300;
const yTextField = document.getElementById("Y-text");

document.addEventListener("DOMContentLoaded", function () {
    document.getElementById("clear-button").addEventListener("click", clearButton);
    document.getElementById("submit-button").addEventListener("click", function (e) {
        e.preventDefault();
        setR();
        if (!checkY()) return;
        setX();
        changePoint();
        fetch("php/check.php?x=" + X + "&y=" + Y + "&r=" + R)
            .then(response => response.text())
            .then(response => document.getElementById("check").innerHTML = response);
    });
});

function checkY(){
    let Y_text = document.getElementById("Y-text");
    Y = Y_text.value.replace(",", ".");
    if (Y.trim() === ""){
        Y_text.setCustomValidity("Заполните поле");
        return false;
    } else if (!isFinite(Y)){
        Y_text.setCustomValidity("Должно быть число!");
        return false;
    } else if (Y > 5 || Y < -3){
        Y_text.setCustomValidity("Вы вышли за диапазон [-3; 5]!");
        return false;
    }
    Y_text.setCustomValidity("");
    return true;
}

function setR() {
    const selectedR = document.querySelector('input[name="r"]:checked');
    if (selectedR) {
        R = selectedR.value;
    }
}

function setX() {
    document.querySelectorAll(".x-button").forEach(button => {
        button.addEventListener("click", function() {
            X = this.getAttribute("data-value");
            changePoint();
        });
    });

}

function calculateX(x, r){
    return x / r * 100 + GRAPH_WIDTH / 2;
}

function calculateY(y, r){
    return GRAPH_HEIGHT / 2 - y / r * 100;
}

const clearButton = function (e){
    e.preventDefault();
    fetch("php/clear_table.php")
        .then(response => response.text())
        .then(response => document.getElementById("check").innerHTML = response)
};

function changePoint(){
    let point = $("#point");
    let formData = new FormData(document.getElementById("coordinates-form"));
    Y = formData.get("y").replace(",", ".");
    //R = formData.get("r").replace(",", ".");
    const xGraph = calculateX(X, R), yGraph = calculateY(Y, R);

    point.attr({
        cx: xGraph,
        cy: yGraph,
        visibility: "visible"
    });
}

document.querySelectorAll(".x-button").forEach(button => {
    button.addEventListener("click", function() {
        X = this.getAttribute("data-value");
        document.getElementById("X-input").value = X;
        changePoint();
    });
});


yTextField.addEventListener("change", e => {
    changePoint();
});
