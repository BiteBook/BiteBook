

// Get all the recipe rating sections
const ratingSections = document.querySelectorAll('.rating-section');

function highlightStars(ratingSection, rating) {
    const stars = ratingSection.querySelectorAll('.star');
    stars.forEach((star, index) => {
        star.classList.toggle('filled', index < rating);
    });
}

// Function to set the rating for a recipe
function setRating(ratingSection, rating) {
    // Save the rating value to the backend
    const recipeId = ratingSection.dataset.recipeId;
    fetch(`/api/recipes/${recipeId}/rating`, { method: 'POST', body: JSON.stringify({ rating }) });
}
ratingSections.forEach(ratingSection => {
    const stars = ratingSection.querySelectorAll('.star');

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
            const currentRating = parseInt(ratingSection.dataset.rating) || 0;
            highlightStars(ratingSection, currentRating);
        });
        star.addEventListener('mouseleave', () => {
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
});
