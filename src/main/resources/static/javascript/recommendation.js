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
    const response = await fetch(`https://api.edamam.com/api/recipes/v2/0123456789abcdef0123456789abcdef?app_id=YOUR_APP_ID&app_key=YOUR_APP_KEY&type=public
${APIKEY}&query=${searchValue}`).then(response => response.json());
    displayRecipes(response.results);
}
function displayRecipes(recipes) {

}







