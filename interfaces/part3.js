
async function handleBadSubmit(event) {
    event.preventDefault()
    const name = document.getElementById('v_id').value;
    const author = document.getElementById('v_author').value;
    const category = document.getElementById('v_category_id').value;
    const v_pricemin = document.getElementById('v_pricemin').value;
    const v_pricemax = document.getElementById('v_pricemax').value;

    const startMonth = document.getElementById('v_sp_start_month').value;
    const startDay = document.getElementById('v_sp_start_day').value;
    const startYear = document.getElementById('v_sp_start_year').value;

    const endMonth = document.getElementById('v_sp_end_month').value;
    const endDay = document.getElementById('v_sp_end_day').value;
    const endYear = document.getElementById('v_sp_end_year').value;

    const within = document.getElementById('v_sp_date_range').value;
    const withinSelected = document.getElementById('v_custom').checked;

    const limitNumber = document.getElementById('v_sp_c').value;

    let url = 'http://localhost:8090/book/insecure?'
    if (name) url += '&title=' + name // url.searchParams.append('title', name);
    if (author) url += '&author=' + author // url.searchParams.append('author', author);
    if (category) url += '&category=' + category // url.searchParams.append('category', category);
    if (v_pricemin) url += '&priceMore=' + v_pricemin // url.searchParams.append('priceMore', v_pricemin);
    if (v_pricemax) url += '&priceLess=' + v_pricemax  // url.searchParams.append('priceLess', v_pricemax);
    if (withinSelected == false && startDay && startMonth && startYear) url += '&from=' + startYear+"-"+startMonth+"-"+startDay  // url.searchParams.append('from', startYear+"-"+startMonth+"-"+startDay);
    if (withinSelected == false && endDay && endMonth && endYear) url += '&until=' + endYear+"-"+endMonth+"-"+endDay // url.searchParams.append('until', endYear+"-"+endMonth+"-"+endDay);
    if (limitNumber) url+= '&limit=' + limitNumber // url.searchParams.append('limit', limitNumber);

    if (withinSelected == true && within != -1) {
        const newDate = new Date()
        newDate.setDate(newDate.getDate() - within);
        const formattedDate = newDate.toISOString().split('T')[0];
        // url.searchParams.append('from', formattedDate)
        url += "&from=" + formattedDate
    }

    url = url.replace("http://localhost:8090/book/insecure?&", "http://localhost:8090/book/insecure?")
    console.log(url)

    await fetch(url, {
        method: 'GET',
    }).then(response => {
          if (!response.ok) {
            throw new Error(`HTTP error! status: ${response.status}`);
          }
          return response.json(); // Parse the JSON response
        })
        .then(data => {
        document.getElementById('values_here').innerHTML = "";
        for (const value of data) {
            document.getElementById('values_here').innerHTML += `ID: ${value.id} - Title: ${value.title} - Author: ${value.author} - Price: ${value.price} - Date: ${value.date} - Text: ${value.text}<br>`
        }

        //document.getElementById('values_here').innerHTML = data;
          console.log('Data retrieved:', data); // Handle the data from the API
        })
}

async function handleGoodSubmit(event) {
    event.preventDefault()

    const name = document.getElementById('c_id').value;
    const author = document.getElementById('c_author').value;
    const category = document.getElementById('c_category_id').value;
    const v_pricemin = document.getElementById('c_pricemin').value;
    const v_pricemax = document.getElementById('c_pricemax').value;

    const within = document.getElementById('c_sp_date_range').value;
    const withinSelected = document.getElementById('c_custom').checked;

    const startMonth = document.getElementById('c_sp_start_month').value;
    const startDay = document.getElementById('c_sp_start_day').value;
    const startYear = document.getElementById('c_sp_start_year').value;

    const endMonth = document.getElementById('c_sp_end_month').value;
    const endDay = document.getElementById('c_sp_end_day').value;
    const endYear = document.getElementById('c_sp_end_year').value;

    const limitNumber = document.getElementById('c_sp_c').value;

    const url = new URL('http://localhost:8090/book/secure');
    if (name) url.searchParams.append('title', name);
    if (author) url.searchParams.append('author', author);
    if (category) url.searchParams.append('category', category);
    if (v_pricemin) url.searchParams.append('priceMore', v_pricemin);
    if (v_pricemax) url.searchParams.append('priceLess', v_pricemax);
    if (withinSelected != true && startDay && startMonth && startYear) url.searchParams.append('from', startYear+"-"+startMonth+"-"+startDay);
    if (withinSelected != true && endDay && endMonth && endYear) url.searchParams.append('until', endYear+"-"+endMonth+"-"+endDay);

    if (limitNumber) url.searchParams.append('limit', limitNumber);

    if (withinSelected == true && within != -1) {
        const newDate = new Date()
        newDate.setDate(newDate.getDate() - within);
        const formattedDate = newDate.toISOString().split('T')[0];
        url.searchParams.append('from', formattedDate)
    }

    console.log(url)
    await fetch(url, {
        method: 'GET',
    }).then(response => {
          if (!response.ok) {
            throw new Error(`HTTP error! status: ${response.status}`);
          }
          return response.json(); // Parse the JSON response
        })
        .then(data => {
          document.getElementById('values_here').innerHTML = "";
                  for (const value of data) {
                      document.getElementById('values_here').innerHTML += `ID: ${value.id} - Title: ${value.title} - Author: ${value.author} - Price: ${value.price} - Date: ${value.date} - Text: ${value.text}<br>`
                  }

                  //document.getElementById('values_here').innerHTML = data;
                    console.log('Data retrieved:', data); // Handle the data from the API
        })
}

async function insertData(event) {
    event.preventDefault()
    const title = ["a", "b", "c", "d", "e", "f", "g"]
    const author = ["abca", "abcb", "abcc", "abcd", "abce", "abcf", "abcg"]
    const category = ["databases", "html_web_design", "programming"]
    const date = ["2020-12-29", "2019-01-01", "2024-12-05", "2024-12-05", "2023-07-01", "2022-09-05", "2024-03-09"]
    const price = [5.5, 9.2, 30.5, 2.6, 12.3, 18.5, 1.0]
    const text = ["texta", "textb", "textc", "textd", "texte", "textf", "textg"]


    for (let i = 0; i < 7; i++) {
        await fetch('http://localhost:8090/book', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({
                title: title[i],
                author: author[i],
                category: category[Math.floor(Math.random() * 3)],
                date: date[i],
                price: price[i],
                text: text[i]
            })
        })
    }
}

async function checkLogin() {
    console.log(localStorage.getItem("jwt"))
    const response = await fetch('http://localhost:8090/accounts/checkAuth', {
        method: 'GET',
        headers: {
            'Content-type': 'application/json',
            'Authorization': "Bearer " + localStorage.getItem("jwt"),
        }
    })

    if ((await response.text()) != "true") {
        document.location = "index.html";
    }
}

checkLogin()