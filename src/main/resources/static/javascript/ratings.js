const ratingContainers = document.querySelectorAll(".gold-stars-container");

ratingContainers.forEach((container) => {
    const ratingValue = parseInt(container.querySelector(".gold-stars").getAttribute("data-rating"));
    let starsHTML = '';
    for (let i = 1; i <= ratingValue; i++) {
        starsHTML += '<i class="fas fa-star"></i>'; // Font Awesome solid gold star icon
    }
    container.querySelector(".gold-stars").innerHTML = starsHTML;
});
