window.onload = function () {
    var cards = ['card1', 'card2', 'card3', 'card4', 'card5', 'card6']; // Add more card ids if needed
    var delay = 950; // Delay in milliseconds

    cards.forEach(function (card, index) {
        setTimeout(function () {
            var element = document.getElementById(card);
            element.classList.add('hover');
            setTimeout(function () {
                element.classList.remove('hover');
            }, delay * 1.1);
        }, index * delay);
    });
};
//
//     $(document).ready(function(){
//     $(".quote").click(function(){
//         var fontFamily = $(this).css("font-family");
//         if (fontFamily == "ElvishFont") {
//             $(this).css("font-family", "EnglishFont");
//         } else {
//             $(this).css("font-family", "ElvishFont");
//         }
//     });
// });
// // Get the card and quote elements
var cardElement = document.getElementById('card3'); // Replace with the actual ID of your card
var quoteElement = document.querySelector('.quote');

// // Store the Elvish and English versions of the quote
// var elvishQuote = quoteElement.textContent;
// var englishQuote = "If more of us valued food and cheer above hoarded gold, it would be a much merrier world";
//
// // Set the initial text to the Elvish quote
// quoteElement.textContent = elvishQuote;
//
// // Set the font to Elvish
// quoteElement.style.fontFamily = 'ElvishFont';
//
// // When the mouse enters the card, start replacing the Elvish characters with English characters
// cardElement.addEventListener('mouseover', function() {
//     var index = 0;
//     var intervalId = setInterval(function() {
//         // Replace the next character
//         quoteElement.textContent = englishQuote.substring(0, index) + elvishQuote.substring(index);
//
//         // Switch to the English font for the replaced part
//         quoteElement.innerHTML = '<span style="font-family: EnglishFont;">' + englishQuote.substring(0, index) + '</span>' + elvishQuote.substring(index);
//
//         index++;
//
//         // If all characters have been replaced, stop the interval
//         if (index > englishQuote.length) {
//             clearInterval(intervalId);
//         }
//     }, 150); // Replace a character every 500 milliseconds
// });
cardElement.addEventListener('mouseover', function() {
    var englishElement = document.querySelector('.quote .english');
    var elvishElement = document.querySelector('.quote .elvish');
    englishElement.style.opacity = 1;
    elvishElement.style.opacity = 0;
});
cardElement.addEventListener('mouseout', function() {
    var englishElement = document.querySelector('.quote .english');
    var elvishElement = document.querySelector('.quote .elvish');
    englishElement.style.opacity = 0;
    elvishElement.style.opacity = 1;
});
