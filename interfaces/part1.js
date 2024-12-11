
async function login(event) {
    event.preventDefault()
    const email = document.getElementById('login_email').value;
    const password = document.getElementById('login_password').value;
    const good = await fetch('http://localhost:8090/login', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify({ username: email, password: password })
    })

    if (good.ok) {
        alert("Login successfully")
        localStorage.setItem("jwt", await good.text())
    } else {
        alert("Error logging in")
        localStorage.removeItem("jwt")
    }

}

function validatePassword(password) {
    // Define the regex pattern
    const passwordRegex = /^(?=.*[A-Z])(?=.*[a-z])(?=.*[0-9])(?=.*[@$!%*?&])[A-Za-z0-9@$!%*?&]{8,}$/;

    // Test the password against the pattern
    return password.length <= 32 && passwordRegex.test(password)
}

async function register(event) {
    event.preventDefault()
    const email = document.getElementById('register_email').value;
    const password = document.getElementById('register_password').value;

    if (!validatePassword(password)) {
        alert("Password must be at least 8 characters long, include at least one uppercase letter, one lowercase letter, one number, and one special character. Maximum length of 32.")
        return
    }

    const bad = await fetch('http://localhost:8090/accounts', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify({ email: email, password: password })
    })

    if (bad.ok) {
        alert("Created successfully")
    } else {
        alert("Error creating account")
    }
    console.log(bad)
}