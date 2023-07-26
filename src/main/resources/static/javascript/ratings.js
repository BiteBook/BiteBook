
    // Replace 'YOUR_API_KEY' with your actual Spoonacular API key
    const APIKEY2  = '43c9ca641fd149cfa982fabb08dfabe7';
    const searchQuery = 'yogurt'; // You can change the search query here

    // Function to make the API call and display the search results
    function searchForRecipes() {
    const url = `https://api.spoonacular.com/food/products/search?query=${searchQuery}&apiKey=${apiKey}`;

    fetch(url)
    .then(response => response.json())
    .then(data => displayResults(data.products))
    .catch(error => console.error('Error fetching data:', error));
}

    // Function to display the search results
    function displayResults(products) {
    const searchResultsDiv = document.getElementById('searchResults');
    searchResultsDiv.innerHTML = '';

    if (products.length === 0) {
    searchResultsDiv.innerHTML = '<p>No results found.</p>';
    return;
}

    products.forEach(product => {
    const productCard = createProductCard(product);
    searchResultsDiv.appendChild(productCard);
});
}

    // Function to create a card for each product
    function createProductCard(product) {
    const card = document.createElement('div');
    card.classList.add('card');

    const cardBody = document.createElement('div');
    cardBody.classList.add('card-body');

    const productName = document.createElement('h5');
    productName.classList.add('card-title');
    productName.textContent = product.title;

    const productImage = document.createElement('img');
    productImage.classList.add('card-img-top');
    productImage.src = product.image;
    productImage.alt = product.title;

    const productDescription = document.createElement('p');
    productDescription.classList.add('card-text');
    productDescription.textContent = product.description;

    cardBody.appendChild(productName);
    cardBody.appendChild(productImage);
    cardBody.appendChild(productDescription);
    card.appendChild(cardBody);

    return card;
}

    // Call the function to initiate the API call and display results
    searchForRecipes();

