// Get all elements with the class "gold-stars"
const ratingContainers = document.querySelectorAll(".gold-stars");

// Iterate over each element and add gold stars based on the rating value
ratingContainers.forEach((ratingElement) => {
    const ratingValue = parseInt(ratingElement.getAttribute("data-rating"));
    let starsHTML = '';
    for (let i = 1; i <= ratingValue; i++) {
        starsHTML += '<i class="fas fa-star" style="color: gold;"></i>'; // Font Awesome gold star icon
    }
    ratingElement.innerHTML = starsHTML; // Add the gold stars to the element
});