async function getData() {
    const url = "http://localhost:8090/text";
    try {
        const response = await fetch(url);
        console.log(response)
        if (!response.ok) {
            throw new Error(`Response status: ${response.status}`);
        }

        const json = await response.json();
        let element = document.getElementById("tbody");

        for (const value of json) {
            element.innerHTML += "<tr><td>" + value.text + " </td></tr>";
        }
    } catch (error) {
        console.error(error.message);
    }
}

getData()

async function handleGoodSubmit(event) {
    event.preventDefault()
    const textValue = document.getElementById('c_text').value;
    await fetch('http://localhost:8090/text/safe', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify({ text: textValue })
    })
}

async function handleBadSubmit(event) {
    event.preventDefault()
    const textValue = document.getElementById('b_text').value;
    const bad = await fetch('http://localhost:8090/text', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify({ text: textValue })
    })
    console.log(bad)
}

console.log(localStorage.getItem("123"))