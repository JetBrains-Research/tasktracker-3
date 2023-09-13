let taskLanguages = %s
let languageSelector = document.getElementById("language")
let taskSelector = document.getElementById("tasks")

changeLanguage()

taskSelector.addEventListener("change", changeLanguage)

function changeLanguage(){
    let selectedTask = taskSelector.options[taskSelector.selectedIndex].text;
    languageSelector.innerHTML = "";
    taskLanguages[selectedTask].forEach(language => {
        let languageOption = document.createElement("option")
        languageOption.value = language
        languageOption.text = language
        languageSelector.appendChild(languageOption)
    });
}