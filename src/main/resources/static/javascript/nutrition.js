"use strict";

// let ingredients = ['flour 1 cup ', ' 1 sugar cup', 'cup 1 milk'];
function calculateNutrition(ingredients){
    let totalcals = 0;
    let totalfibre = 0;
    let totalcarbs = 0;
    let totalprotein = 0;


// let ingredient = '2 cups milk';
// let nutrition = edamamCall(ingredient);
// nutrition.then(function (result) {
//     console.log(result);
//     console.log(result.totalNutrients.PROCNT.quantity + result.totalNutrients.PROCNT.unit + ' ' + result.totalNutrients.PROCNT.label);
//     console.log(result.calories + ' calories');
// })
    ingredients.forEach(ingredient => {
        let nutrition = edamamCall(ingredient);
        nutrition.then(function (result) {
            console.log(result);
            totalcals += result.calories;
            totalprotein += result.totalNutrients.PROCNT.quantity;
            // totalfibre += result.totalNutrients.
            $('#nutrition').html(
                `<h4 type="text/html" th:field="*{nutrition}">Calories: ${totalcals}</h4>
<h4>Protein: ${totalprotein}</h4>
<h4>Fibre: ${totalfibre}</h4>`
            )
        })
    })

}
async function edamamCall(ingr){
    return await fetch(`https://api.edamam.com/api/nutrition-data?app_id=${USERID}&app_key=${APIKEY}&ingr=${ingr}`).then(response => response.json()).then(data => data)
}
let ingredientList = "";


const submit = document.querySelector('#submit');
submit.addEventListener('click', (event) => {
    event.preventDefault();
    ingredientList =  document.querySelector('#ingredientList').value
    let ingredients = ingredientList.split(',');
    calculateNutrition(ingredients);
})



