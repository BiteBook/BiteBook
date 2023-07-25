// // Select all elements with the "i" tag and store them in a NodeList called "stars"
// const stars = document.querySelectorAll(".stars i");
//
// // Loop through the "stars" NodeList
// stars.forEach((star, index1) => {
//     // Add an event listener that runs a function when the "click" event is triggered
//     star.addEventListener("click", () => {
//         // Loop through the "stars" NodeList Again
//         stars.forEach((star, index2) => {
//             // Add the "active" class to the clicked star and any stars with a lower index
//             // and remove the "active" class from any stars with a higher index
//             index1 >= index2 ? star.classList.add("active") : star.classList.remove("active");
//         });
//     });
// });

/*

// Get all the recipe rating sections
const ratingSections = document.querySelectorAll('.rating-section');

// Function to highlight stars on hover
function highlightStars(ratingSection, rating) {
    const stars = ratingSection.querySelectorAll('.star');
    stars.forEach((star, index) => {
        star.classList.toggle('filled', index < rating);
    });
}

// Function to set the rating for a recipe
function setRating(ratingSection, rating) {
    // Save the rating value to the backend (you will need to implement this)
    const recipeId = ratingSection.dataset.recipeId;
    // Here, you would make a request to your backend API to update the recipe's rating
    // Replace the following line with the actual API call
    // Example:
    fetch(`/api/recipes/${recipeId}/rating`, { method: 'POST', body: JSON.stringify({ rating }) });
}

// Attach event listeners for each rating section
ratingSections.forEach(ratingSection => {
    const stars = ratingSection.querySelectorAll('.star');

    // Handle hover over stars
    stars.forEach(star => {
        star.addEventListener('mouseover', () => {
            const rating = parseInt(star.dataset.rating);
            highlightStars(ratingSection, rating);
        });

        star.addEventListener('mouseleave', () => {
            // Reset the stars to show the current rating
            const currentRating = parseInt(ratingSection.dataset.rating) || 0;
            highlightStars(ratingSection, currentRating);
        });
        star.addEventListener('mouseleave', () => {
            // Reset the stars to show the current rating
            const currentRating = parseInt(ratingSection.dataset.rating) || 0;
            highlightStars(ratingSection, currentRating);
        });
        star.addEventListener('mouseleave', () => {
            // Reset the stars to show the current rating
            const currentRating = parseInt(ratingSection.dataset.rating) || 0;
            highlightStars(ratingSection, currentRating);
        });
        star.addEventListener('mouseleave', () => {
            // Reset the stars to show the current rating
            const currentRating = parseInt(ratingSection.dataset.rating) || 0;
            highlightStars(ratingSection, currentRating);
        });

        // Handle click on a star to set the rating
        star.addEventListener('click', () => {
            const rating = parseInt(star.dataset.rating);
            setRating(ratingSection, rating);
            ratingSection.dataset.rating = rating;
        });
    });
});*/
