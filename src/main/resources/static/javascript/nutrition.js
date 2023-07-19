

const ingrediant = '1 chicken breast';

async function edamamCall(ingr){
    return await fetch(`https://api.edamam.com/api/nutrition-data?app_id=${EDAMAM_APPID}&app_key=${EDAMAM_KEY}&ingr=${ingr}`).then(response => response.json()).then(data => console.log(data))
}

let nutrition = edamamCall(ingrediant);

console.log(nutrition);

