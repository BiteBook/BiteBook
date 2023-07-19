// Get references to the input field, search button, and search results container
const searchInput = document.getElementById('searchInput');
const searchButton = document.getElementById('searchButton');
const searchResults = document.getElementById('searchResults');

// Add event listener to the search button
searchButton.addEventListener('click', performSearch);

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
    displaySearchResults(searchResultsData);

    //easy, medium, difficult
    function getSelectedDifficulty(){

    }
    function searchRecipes(query, difficulty){

    }
    function displaySearchResults(results){
        results.forEach(result =>{
            const resultElement = document.createElement('div');
            resultElement.textContent = result.title;
            searchResults.appendChild(resultElement)
        })
    }
}
