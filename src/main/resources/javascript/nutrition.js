const ingrediant = '1 chicken breast';

function edamamCall(ingr){
    return fetch(`https://api.edamam.com/api/nutrition-data?app_id=${EDAMAM_APPID}&app_key=${EDAMAM_KEY}&ingr=${ingr}`).then(response => response.json()
    )
}

let nutrition = edamamCall(ingrediant);

console.log(nutrition);

