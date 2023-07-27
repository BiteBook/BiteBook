fetch('/api/username')
    .then(response => response.text())
    .then(username => {
        if (username) {
            localStorage.setItem('username', username);
        } else {
            localStorage.removeItem('username');
        }
        displayGreeting(); // Call the function here
    });

function getCurrentTime() {
    const date = new Date();
    const hours = date.getHours();
    let timeOfDay;

    if (hours < 12) {
        timeOfDay = 'morning';
    } else if (hours < 18) {
        timeOfDay = 'afternoon';
    } else {
        timeOfDay = 'evening';
    }

    return timeOfDay;
}

// Function to greet the user
function greetUser() {
    const username = localStorage.getItem('username');
    const timeOfDay = getCurrentTime();

    if (username) {
        return `Good ${timeOfDay}, ${username}!`;
    } else {
        return `Good ${timeOfDay}, guest!`;
    }
}

// Function to display the greeting
function displayGreeting() {
    const greetingElement = document.getElementById('greeting');
    const greetingMessage = greetUser();

    if (greetingElement) {
        greetingElement.textContent = greetingMessage;
    }
}
