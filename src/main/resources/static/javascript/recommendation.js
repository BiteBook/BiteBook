"use strict";
/*


const searchForm = document.querySelector('#form')
const searchInput = document.querySelector('#search')
const resultsList = document.querySelector('#results')

searchForm.addEventListener('submit', (e) => {
    e.preventDefault();
    searchRecipes().then(r =>{} )
})

async function searchRecipes(){
    const searchValue = searchInput.value.trim();
    const response = await fetch(`https://api.edamam.com/api/nutrition-data?app_id=${USERID}&app_key=${APIKEY}&ingr=${ingr}`).then(response => response.json()).then(data => data)
    displayRecipes(data.hits);
}
let ingredientList = "";

function displayRecipes(recipes) {
    let html = '';
    recipes.forEach((recipe) => {
        html += `
        <div>
            <img src="${recipe.recipe.image}" alt="${recipe.recipe.label}">
            <h3>${recipe.recipe.label}</h3>
            <ul>
                ${recipe.recipe.ingredientLines.map(ingredient => `<li>${ingredient}</li>`).join('')}
            </ul>
            <a href="${recipe.recipe.url}" target="_blank">View Recipe</a>
        </div> 
        `
    })
    resultsList.innerHTML = html;
}




*/
