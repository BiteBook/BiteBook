"use strict";


const searchForm = document.querySelector('#form')
const searchInput = document.querySelector('#search')
const resultsList = document.querySelector('#results')

searchForm.addEventListener('submit', (e) => {
    e.preventDefault();
    searchRecipes().then(r =>{} )
})

async function searchRecipes(){
    const searchValue = searchInput.value.trim();
    const response = await fetch(`https://api.spoonacular.com/recipes/716429/information?includeSimilar=true.?apiKey=${APIKEY}&query=${searchValue}`).then(response => response.json());
    displayRecipes(response.results);
}
function displayRecipes(recipes) {
    let html = '';
    recipes.forEach((recipe) => {
        const imageUrl = recipe.image ? recipe.image : 'placeholder-image.jpg';
        const recipeUrl = recipe.sourceUrl ? recipe.sourceUrl : '#'; // Provide a default URL if missing
        html += `
      <div>
        <img src="${imageUrl}" alt="${recipe.title}">
        <h3>${recipe.title}</h3>
        <ul>
          ${recipe.extendedIngredients.map(ingredient => `<li>${ingredient.original}</li>`).join('')}
        </ul>
        <a href="${recipeUrl}" target="_blank">View Recipe</a>
      </div>
     
    `;
    });
    resultsList.innerHTML = html;
}






