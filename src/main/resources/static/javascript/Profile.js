var calendar = document.getElementById('calendar');
var date = new Date();

for (var i = 0; i < 14; i++) {
    var localDate = new Date(date.getTime() - date.getTimezoneOffset() * 60000);
    var isoDate = localDate.toISOString().split('T')[0];

    var day = document.createElement('div');
    day.className = 'day';
    var dayOfWeek = ['Sun', 'Mon', 'Tue', 'Wed', 'Thu', 'Fri', 'Sat'][date.getDay()];
    day.innerText = (date.getMonth() + 1) + '/' + date.getDate() + ' ' + dayOfWeek;

    // Highlight the current day
    if (date.toDateString() === new Date().toDateString()) {
        day.className += ' today';
    }

    // Find the meal plan for this day
    var mealPlan = mealPlanners.find(function(mealPlanner) {
        return mealPlanner.dateString === isoDate;
    });

    if (mealPlan) {
        // If a meal plan exists for this day, add it to the day div
        var meal = document.createElement('a');
        meal.innerText = mealPlan.recipeTitle;
        meal.href = '/recipes/' + mealPlan.recipeId;
        var br1 = document.createElement('br'); // Create the first break element
        var br2 = document.createElement('br'); // Create the second break element
        day.appendChild(br1); // Append the first break element to the day div
        day.appendChild(br2); // Append the second break element to the day div
        day.appendChild(meal);
    }
    console.log(mealPlanners);

    calendar.appendChild(day);
    date.setDate(date.getDate() + 1) // Move to the next day
}



var greetings = [
        "What's cookin', good lookin'?",
        "What's shakin', bacon?",
        "How's it sizzlin'?",
        "What's poppin'?",
        "Welcome back! Let's cook up some fun!",
        "Hey there, ready to roll in the dough?",
        "Welcome! Let's get this bread!",
        "Let's kick it up a notch!"
    ];

// Select a random greeting
    var randomGreeting = greetings[Math.floor(Math.random() * greetings.length)];

// Display the greeting
    document.getElementById('greeting').innerText = randomGreeting;


