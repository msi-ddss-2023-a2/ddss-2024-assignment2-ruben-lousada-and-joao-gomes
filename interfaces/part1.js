
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
    } else {
        alert("Error logging in")
    }
}

async function register(event) {
    event.preventDefault()
    const email = document.getElementById('register_email').value;
    const password = document.getElementById('register_password').value;

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