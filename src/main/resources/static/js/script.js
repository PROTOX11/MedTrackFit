console.log("script loaded")

let currentTheme = getTheme();

document.addEventListener("DOMContentLoaded", () => {
    changeTheme();
});


function changeTheme() {
    document.querySelector("html").classList.add(currentTheme);

    const changeThemeButton = document.querySelector('#theme-change-button');
    changeThemeButton.addEventListener('click', (event) => {
        console.log("clicked")
        const oldTheme = currentTheme;
        if (currentTheme === "light") {
            currentTheme = "dark";
        } else {
            currentTheme = "light";
        }
        setTheme(currentTheme);
        
        document.querySelector("html").classList.remove(oldTheme);
        document.querySelector("html").classList.add(currentTheme);
        updateButtonText(currentTheme);
    })
}
function updateButtonText(theme) {

    const changeThemeButton = document.querySelector('#theme-change-button');

    changeThemeButton.querySelector("span").textContent = theme === "dark" ? "Light" : "Dark";

}
function setTheme(theme) {
    localStorage.setItem("theme", theme);
}
function getTheme() {
    let theme = localStorage.getItem("theme");
    return theme ? theme : "light";
}