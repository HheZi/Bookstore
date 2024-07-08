const user = new URLSearchParams(document.location.search).get("user");

async function makeRequest(url, method) {
    const req = {
        method: method,
        headers: {
            'Content-Type': 'application/json'
        }
    };

    const response = await fetch(url, req);
    if (!response.ok) {
        const errorData = await response.json();
        throw new Error(errorData.detail || 'Произошла ошибка');
    }
    
    return response;
   
}

async function getBooksByUser() {
    try {
        const res = await makeRequest(`http://localhost:8080/api/books/allBooks/${user}`, "GET");
        const books = await res.json();
        updateTable(books);
    } catch (error) {
        showNotification(error.message || 'Something went wrong', 'error');
    }
}

function updateTable(books) {
    const tbody = document.querySelector("#booksTable tbody");
    tbody.innerHTML = '';
    document.querySelector("#booksTable thead").innerHTML = ``;
	
	if(books.length !== 0){
		
		document.getElementById("headTable").innerHTML = `<tr>
                    <th>Cover</th>
                    <th>Title</th>
                    <th>Author</th>
                    <th>Actions</th>
                </tr>`;
		
		
	    books.forEach(book => {
	        const row = document.createElement("tr");
			
			let updateBut = '';
			let but = `<button id="butt${book.id}" onclick="addToCart(${book.id})">Add to cart</button>`;
			let soldOut = ''
			if (user == userAuth.id) {
				but = `<button id="butt${book.id}" onclick="deleteBookFromCart(${book.id})">Delete</button>`;
				updateBut = `<button onClick="location.href='../books/update?book=${book.id}'">Update</button>`
			}
			else if(book.quantity === 0){
				but = '';
				soldOut = "<div class='sold-out-overlay'>Sold out</div>";
			}
			
			
	        row.innerHTML = `
	            <td>
	                <a href="../books/${book.id}">
	                    <img src="${book.coverUrl}" alt="Cover" class="cover">
	                    ${soldOut}
	                </a>
	            </td>
	            <td>
	                <a href="/seeBook/${book.id}">
	                    ${book.title}
	                </a>
	            </td>
	            <td>
	                <a href="/seeBook/${book.id}">
	                    ${book.author}
	                </a>
	            </td>
	            <td>
	            	${updateBut}
	                ${but}
	            </td>
	        `;
	
	        tbody.appendChild(row);

	    });
    }
    else{
		document.getElementById("titleText").innerText = "This user has no books"
	}
    
}

async function deleteBookFromCart(id){
	if (user == userAuth.id) {
		 try {
		      await makeRequest(`http://localhost:8080/api/books/${id}`, "DELETE");
		      getBooksByUser();
		 } catch (error) {
		      showNotification(error.message || 'Something went wrong', 'error');
	 	 }
	 } else {
	  addToCart(id);
	 }
}


async function addToCart(id) {
    try {
        await makeRequest(`http://localhost:8080/api/cart/${id}`, "POST");
        showNotification("Книга добалена в корзину!", "success")
    } catch (error) {
        showNotification(error.message || 'Something went wrong', 'error');
    }
}

function showNotification(message, type) {
    const notification = document.getElementById('notification');
    notification.className = `notification ${type}`;
    notification.textContent = message;

    setTimeout(() => {
        notification.style.display = 'none';
    }, 5000);
}

document.addEventListener("DOMContentLoaded", () => {
    getBooksByUser();
});
