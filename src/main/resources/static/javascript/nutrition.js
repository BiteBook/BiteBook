

// let ingrediant = '2 cups milk';
// let nutrition = edamamCall(ingrediant);
// nutrition.then(function (result) {
//     console.log(result);
//     console.log(result.totalNutrients.PROCNT.quantity + result.totalNutrients.PROCNT.unit + ' ' + result.totalNutrients.PROCNT.label);
//     console.log(result.calories + ' calories');
// })

let ingrediants = ['flour 1 cup', '1 sugar cup', 'cup 1 milk'];
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

calculateNutrition(ingrediants);
