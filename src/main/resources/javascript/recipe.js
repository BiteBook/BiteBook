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

    // Perform your search logic here
    // Display search results
    const searchResultItem = document.createElement('div');
    searchResultItem.textContent = 'Search result for: ' + query;
    searchResults.appendChild(searchResultItem);
    //easy, medium, difficult
}
