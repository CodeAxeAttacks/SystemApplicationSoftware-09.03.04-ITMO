const canvas = document.getElementById("graph"),
    ctx = canvas.getContext('2d');

canvas.width = 300
canvas.height = 300
let w = canvas.width,
    h = canvas.height;

const hatchWidth = 20 / 2;
const hatchGap = 56;

function redrawGraph(r) {
    ctx.clearRect(0, 0, w, h);

    ctx.lineWidth = 2;
    ctx.strokeStyle = 'black';

    // y axis
    ctx.beginPath();
    ctx.moveTo(w / 2, 0);
    ctx.lineTo(w / 2 - 10, 15);
    ctx.moveTo(w / 2, 0);
    ctx.lineTo(w / 2 + 10, 15);
    ctx.moveTo(w / 2, 0);
    ctx.lineTo(w / 2, h);
    ctx.stroke();
    ctx.closePath();

    // x axis
    ctx.beginPath();
    ctx.moveTo(w, h / 2);
    ctx.lineTo(w - 15, h / 2 - 10);
    ctx.moveTo(w, h / 2);
    ctx.lineTo(w - 15, h / 2 + 10);
    ctx.moveTo(w, h / 2);
    ctx.lineTo(0, h / 2);
    ctx.stroke();
    ctx.closePath();

    // hatches
    ctx.beginPath();
    ctx.moveTo(w / 2 - hatchWidth, h / 2 - hatchGap);
    ctx.lineTo(w / 2 + hatchWidth, h / 2 - hatchGap);
    ctx.moveTo(w / 2 - hatchWidth, h / 2 - hatchGap * 2);
    ctx.lineTo(w / 2 + hatchWidth, h / 2 - hatchGap * 2);
    ctx.moveTo(w / 2 - hatchWidth, h / 2 + hatchGap);
    ctx.lineTo(w / 2 + hatchWidth, h / 2 + hatchGap);
    ctx.moveTo(w / 2 - hatchWidth, h / 2 + hatchGap * 2);
    ctx.lineTo(w / 2 + hatchWidth, h / 2 + hatchGap * 2);
    ctx.moveTo(w / 2 - hatchGap, h / 2 - hatchWidth);
    ctx.lineTo(w / 2 - hatchGap, h / 2 + hatchWidth);
    ctx.moveTo(w / 2 - hatchGap * 2, h / 2 - hatchWidth);
    ctx.lineTo(w / 2 - hatchGap * 2, h / 2 + hatchWidth);
    ctx.moveTo(w / 2 + hatchGap, h / 2 - hatchWidth);
    ctx.lineTo(w / 2 + hatchGap, h / 2 + hatchWidth);
    ctx.moveTo(w / 2 + hatchGap * 2, h / 2 - hatchWidth);
    ctx.lineTo(w / 2 + hatchGap * 2, h / 2 + hatchWidth);
    ctx.stroke();
    ctx.closePath();

    // main figure
    ctx.fillStyle = 'rgba(44,106,220,0.33)';
    ctx.beginPath();

    // triangle in the second quarter
    ctx.moveTo(w / 2, h / 2);
    ctx.lineTo(w / 2 - hatchGap * 2, h / 2);
    ctx.lineTo(w / 2, h / 2 - hatchGap);

    // rectangle in the third quarter
    ctx.moveTo(w / 2, h / 2);
    ctx.lineTo(w / 2, h / 2 + hatchGap);
    ctx.lineTo(w / 2 - hatchGap * 2, h / 2 + hatchGap);
    ctx.lineTo(w / 2 - hatchGap * 2, h / 2);

    // semicircle in the fourth quarter
    ctx.arc(w / 2, h / 2, hatchGap * 2, 0, 0.5 * Math.PI, false);

    ctx.fill();
    ctx.strokeStyle = '#2313f3'
    ctx.stroke();
    ctx.closePath();

    const fontSize = hatchGap / 3.5
    ctx.fillStyle = 'black'

    ctx.font = `${fontSize * 1.4}px "Arial", sans-serif`;
    ctx.fillText('y', w / 2 - hatchWidth * 2.8, 15)
    ctx.fillText('x', w - 20, h / 2 - hatchWidth * 2.4)

    let label1, label2;
    if (isNaN(r)) {
        label1 = r + '/2'
        label2 = r
    } else {
        label1 = r / 2
        label2 = r
    }

    ctx.font = `${fontSize}px "Arial", sans-serif`;
    ctx.fillText(label1, w / 2 + hatchGap - 3, h / 2 + hatchWidth * 2.8);
    ctx.fillText(label2, w / 2 + hatchGap * 2 - 3, h / 2 + hatchWidth * 2.8);
    ctx.fillText('-' + label1, w / 2 - hatchGap - 7, h / 2 + hatchWidth * 2.8);
    ctx.fillText('-' + label2, w / 2 - hatchGap * 2 - 7, h / 2 + hatchWidth * 2.8);

    ctx.fillText(label1, w / 2 + hatchWidth * 2, h / 2 - hatchGap + 3);
    ctx.fillText(label2, w / 2 + hatchWidth * 2, h / 2 - hatchGap * 2 + 3);
    ctx.fillText('-' + label1, w / 2 + hatchWidth * 2, h / 2 + hatchGap + 3);
    ctx.fillText('-' + label2, w / 2 + hatchWidth * 2, h / 2 + hatchGap * 2 + 3);
}

redrawGraph('R');

function printDotsOnGraph(xCenter, yCenter, rValue, isHit, size) {
    if (parseFloat(size) === parseFloat(rValue)) {
        ctx.fillStyle = isHit ? '#00ff00' : '#8c00ff'
    } else {
        ctx.fillStyle = '#c0c0c0'
    }
    const scaledXCenter = xCenter / size;
    const scaledYCenter = yCenter / size;

    const x = w / 2 + scaledXCenter * hatchGap * 2;
    const y = h / 2 - scaledYCenter * hatchGap * 2;
    const radius = 3;
    ctx.beginPath();
    ctx.arc(x, y, radius, 0, 2 * Math.PI);
    ctx.fill();
    ctx.closePath();
}

function isNumberInArray(number, array) {
    return array.includes(number);
}

const startRange = 1.25;
const endRange = 4.75;
const step = 0.25;
const numberArray = [];
for (let i = startRange; i <= endRange; i += step) {
    numberArray.push(i.toString());
}

function updateDotsOnGraphFromTable() {
    const size = rInput.value;
    const tableRows = document.querySelectorAll('.main__table tbody tr');
    if (isNumberInArray(size, numberArray)) {
        redrawGraph(size);
    }
    tableRows.forEach(row => {
        if (row.childNodes.length > 1) {
            const children = row.children;
            printDotsOnGraph(children[0].innerHTML, children[1].innerHTML, children[2].innerHTML, children[3].firstChild.classList.contains('hit'), size);
        }
    });
}

canvas.addEventListener('click', (event) => {
    if (rValid) {
        messages.innerText = '';
        const canvasLeft = canvas.offsetLeft + canvas.clientLeft,
            canvasTop = canvas.offsetTop + canvas.clientTop;

        const x = event.pageX - canvasLeft,
            y = event.pageY - canvasTop;

        const xCenter = Math.round((x - w/2) / (hatchGap * (2/rInput.value))*1000)/1000,
            yCenter = Math.round((h/2 - y) / (hatchGap * (2/rInput.value))*1000)/1000;

        const oldX = xInput.value, oldY = yInput.value;
        yInput.value = yCenter;
        xInput.value = xCenter;
        document.getElementById('form:source').value = 'graph';

        submitBtn.onclick(undefined);

        xInput.value = oldX;
        yInput.value = oldY;
    } else {
        messages.innerText = "The R field cannot be empty!";
    }
})

submitBtn.addEventListener('click', () => {
    if (rValid) {
        document.getElementById('form:source').value = 'button';
    } else {
        messages.innerText = "The R field cannot be empty!";
    }
});

