

var $jq = jQuery.noConflict();

$jq(document).ready(function() {
    $jq('#recipeList').slick({
        infinite: true,
        slidesToShow: 3,
        slidesToScroll: 3
    });

    $jq('#readyList').slick({
        infinite: true,
        slidesToShow: 3,
        slidesToScroll: 3
    });
});
