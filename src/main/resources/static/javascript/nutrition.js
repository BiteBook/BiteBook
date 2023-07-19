"use strict";

// let ingredients = ['flour 1 cup ', ' 1 sugar cup', 'cup 1 milk'];
function calculateNutrition(ingrediants){
    let totalcals = 0;
    let totalfats = 0;
    let totalcarbs = 0;
    let totalprotein = 0;

    ingrediants.forEach(ingrediant => {
        let nutrition = edamamCall(ingrediant);
        nutrition.then(function (result) {
            console.log(result.calories);
            totalcals += result.calories;
            console.log(result.totalNutrients.PROCNT.quantity)
            totalprotein += result.totalNutrients.PROCNT.quantity;
            $('#nutrition').html(
                `<h4>Calories: ${totalcals}</h4>
<h4>Protein: ${totalprotein}</h4>`
            )
        })
    })

}
async function edamamCall(ingr){
    return await fetch(`https://api.edamam.com/api/nutrition-data?app_id=${EDAMAM_APPID}&app_key=${EDAMAM_KEY}&ingr=${ingr}`).then(response => response.json()).then(data => data)
}
let ingredientList = "";


const submit = document.querySelector('#submit');
submit.addEventListener('click', (event) => {
    event.preventDefault();
    ingredientList =  document.querySelector('#ingredientList').value
    let ingredients = ingredientList.split(',');
    calculateNutrition(ingredients);
})



