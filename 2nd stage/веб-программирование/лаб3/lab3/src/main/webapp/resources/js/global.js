// Burger menu
const iconMenu = document.querySelector('.menu__icon');
iconMenu.addEventListener('click', () => {
    iconMenu.classList.toggle('_active');
    document.querySelector('.header__title').classList.toggle('_active');
    document.querySelector('.menu__body').classList.toggle('_active');
    document.body.classList.toggle('_lock');
})