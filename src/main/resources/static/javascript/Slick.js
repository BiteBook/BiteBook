
var $jq = jQuery.noConflict();

$jq(document).ready(function() {
    $jq('#recipeList').slick({
        infinite: true,
        slidesToShow: 3,
        slidesToScroll: 3,
        autoplay: true, // Enables auto sliding
        autoplaySpeed: 3500, // Time in milliseconds for each slide (2 seconds)
        arrows: true, // Enables navigation arrows
        dots: true, // Enables navigation dots
        speed: 2000
    });

    $jq('#readyList').slick({
        infinite: true,
        slidesToShow: 3,
        slidesToScroll: 3,
        autoplay: true, // Enables auto sliding
        autoplaySpeed: 3500, // Time in milliseconds for each slide (2 seconds)
        arrows: true, // Enables navigation arrows
        dots: true, // Enables navigation dots
        speed: 2000
    });
});
