function addStudent() {
    let record = {
        studentid : document.getElementById("studentid").value,
        firstname : document.getElementById("fname").value,
        lastname : document.getElementById("lname").value,
        modulename : document.getElementById("modulename").value,
        level : document.getElementById("level").value,
        credits : document.getElementById("credits").value
    };
    let xhr = new XMLHttpRequest();
    xhr.open("POST", "/addstudents");
    xhr.setRequestHeader('Content-type', 'application/json');
    xhr.send(JSON.stringify(record));
}

function logout() {
    let xhr = new XMLHttpRequest();
    xhr.open("POST", "/logout");
    xhr.setRequestHeader("Content-Type", "application/x-www-form-urlencoded");
    xhr.onload = function() {
        if (xhr.status === 200) {
            // Redirect or perform any other action after successful logout
            window.location.href = "/"; // Redirect to homepage
        } else {
            console.error('Logout failed. Status code: ' + xhr.status);
        }
    };
    xhr.send(); // No data is sent with the request body
}