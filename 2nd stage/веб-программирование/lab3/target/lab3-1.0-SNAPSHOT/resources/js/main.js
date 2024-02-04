window.addEventListener('load', function() {
    const sliderHandleR = document.querySelector('#rSlider .ui-slider-handle');
    const sliderHandleX = document.querySelector('#xSlider .ui-slider-handle');

    if (sliderHandleX) {
        sliderHandleX.style.left = '50%';
    }
    if (sliderHandleR) {
        sliderHandleR.style.left = '50%';
    }
});

let xValid = false, yValid = false, rValid = false;

const messages = document.getElementById('messagesC');
const messagesS = document.getElementById('messagesC');


let xInput = document.getElementById('form:X');
const xSpan = document.getElementById('form:XValue');


let yInput = document.getElementById('form:Y');
yInput.addEventListener('input', () => {
    yValid = false;
    let yValue = yInput.value.trim();
    const isValid = /^(-?[1-3]|[0-4])(?:[\.,]\d+)?$/.test(yValue);
    yValue = parseFloat(yInput.value.trim().replace(',', '.'));

    if (!isNaN(yValue)) {
        yValid = isValid;
    } else {
        yValid = false;
        messages.innerText = 'Invalid value of Y (a number in the range (-5;3))';
        toggleSubmitBtn();
    }

    if (!yValid) {
        messages.innerText = 'Invalid value of Y (a number in the range (-3;5))';
        toggleSubmitBtn();
    } else {
        yInput.value = yInput.value.replace(',', '.');
        messages.innerText = '';
        $("#messagesS").empty();
        toggleSubmitBtn();
    }
});


let rInput = document.getElementById('form:R');
const rSpan = document.getElementById('form:RValue');

const observerR = new MutationObserver(function (mutationsList) {
    for (let mutation of mutationsList) {
        rValid = false;
        if (mutation.type === 'childList') {
            const rValue = rSpan.textContent.trim();
            const hasLetters = /[a-zA-Z]/.test(rValue);
            const rValueFloat = parseFloat(rValue);

            const minLimit = BigInt('1250');
            const maxLimit = BigInt('3750');
            if (!isNaN(rValueFloat)) {
                const rValueBigInt = BigInt(Math.round(rValueFloat * 1000));
                rValid = !hasLetters && rValueBigInt >= minLimit && rValueBigInt <= maxLimit && rValueBigInt % BigInt('250') === BigInt('0');
            } else {
                rValid = false;
                messages.innerText = 'Invalid value of R';
                toggleSubmitBtn();
            }
            if (rValid) {
                messages.innerText = '';
                $("#messagesS").empty();
                rInput.value = rSpan.textContent;
                redrawGraph(rInput.value);
                updateDotsOnGraphFromTable();
                toggleSubmitBtn();
            } else {
                messages.innerText = 'Invalid value of R';
                toggleSubmitBtn();
            }
        } else {
            messages.innerText = 'Invalid value of R';
            rValid = false;
            toggleSubmitBtn();
        }
    }
});



const observerX = new MutationObserver(function (mutationsList) {
    for (let mutation of mutationsList) {
        xValid = false;
        if (mutation.type === 'childList') {
            const xValue = xSpan.textContent.trim();
            const hasLetters = /[a-zA-Z]/.test(xValue);
            const xValueFloat = parseFloat(xValue);

            const minLimit = BigInt('-490');
            const maxLimit = BigInt('490');
            if (!isNaN(xValueFloat)) {
                const xValueBigInt = BigInt(Math.round(xValueFloat * 100));
                xValid = !hasLetters && xValueBigInt >= minLimit && xValueBigInt <= maxLimit && xValueBigInt % BigInt('10') === BigInt('0');
            } else {
                xValid = false;
                messages.innerText = 'Invalid value of X';
                toggleSubmitBtn();
            }
            if (xValid) {
                messages.innerText = '';
                $("#messagesS").empty();
                xInput.value = xSpan.textContent;
                toggleSubmitBtn();
            } else {
                messages.innerText = 'Invalid value of X';
                toggleSubmitBtn();
            }
        } else {
            messages.innerText = 'Invalid value of X';
            xValid = false;
            toggleSubmitBtn();
        }
    }
});


observerR.observe(rSpan, { childList: true });
observerX.observe(xSpan, { childList: true });

window.addEventListener('load', function () {
    xSpan.value = '0';
    xInput.value = xSpan.value;
    if (xInput.value !== null) {
        xValid = true;
    }
});
window.addEventListener('load', function () {
    rSpan.value = '2.5';
    rInput.value = rSpan.value;
    if (rInput.value !== null) {
        redrawGraph(rInput.value);
        updateDotsOnGraphFromTable();
        rValid = true;
    }
});

const submitBtn = document.getElementById('form:submitBtn');
function toggleSubmitBtn() {
    submitBtn.disabled = !(xValid && yValid && rValid)
}

xInput.value = '';
yInput.value = '';
rInput.value = '';
toggleSubmitBtn()


