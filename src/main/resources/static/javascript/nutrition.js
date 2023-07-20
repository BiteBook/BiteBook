

let ingredient = '2 cups milk';
let nutrition = edamamCall(ingredient);
nutrition.then(function (result) {
    console.log(result);
    console.log(result.totalNutrients.PROCNT.quantity + result.totalNutrients.PROCNT.unit + ' ' + result.totalNutrients.PROCNT.label);
    console.log(result.calories + ' calories');
})

async function edamamCall(ingr){
    return await fetch(`https://api.edamam.com/api/nutrition-data?app_id=${EDAMAM_APPID}&app_key=${EDAMAM_KEY}&ingr=${ingr}`).then(response => response.json()).then(data => data)
}
