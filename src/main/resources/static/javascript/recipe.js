"use strict";

// Get references to the input field, search button, and search results container
const searchInput = document.getElementById('searchInput');
const searchButton = document.getElementById('searchButton');
const searchResults = document.getElementById('searchResults');

// Add event listener to the search button
searchButton.addEventListener('click', performSearch);

function useMutation(REMOVE_RECIPE) {

}



// Define the search function
function performSearch() {
    // Clear previous search results
    searchResults.innerHTML = '';

    // Get the search query from the input field
    const query = searchInput.value;


    // Get the selected difficulty level (easy, medium, hard)
    const difficulty = getSelectedDifficulty();

    // Perform the search logic based on the difficulty level
    const searchResultsData = searchRecipes(query, difficulty);

    // Display search results

    //easy, medium, difficult
    function getSelectedDifficulty() {

    }

    function searchRecipes(query, difficulty) {

    }

    function RecipeCard({recipe, showDelete, showSave, viewOnly}) {

        const {title, ingredients, description, instructions, total_time, link} = recipe;
        const [removeRecipe, {error, data}] = useMutation(REMOVE_RECIPE);
        const [saveRecipe, {error: saveError, data: saveData}] = useMutation(SAVE_RECIPE);
        const [viewSave, setViewSave] = useState(showSave);

        async function deleteRecipe(recipeId) {
            await removeRecipe({variables: {_id: recipeId}})
        }

        async function saveToCollection(recipe) {
            console.log(recipe);
            await saveRecipe({variables: recipe});
        }

        // Sample data for trending and ready in minutes recipes (Replace with your actual data).
        const trendingRecipes = [
            {title: "Recipe 1", total_time: "30 minutes", description: "Delicious recipe 1"},
            {title: "Recipe 2", total_time: "45 minutes", description: "Yummy recipe 2"},
            {title: "Recipe 3", total_time: "20 minutes", description: "Tasty recipe 3"}
        ];

        const readyInMinutesRecipes = [
            {title: "Recipe 4", total_time: "15 minutes", description: "Quick recipe 4"},
            {title: "Recipe 5", total_time: "25 minutes", description: "Fast recipe 5"},
            {title: "Recipe 6", total_time: "10 minutes", description: "Easy recipe 6"}
        ];


    }
}
