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
        throw new Error(errorData.detail);
    }
    return response;
}

async function getBook() {
    const url = document.location.href.split('/').slice(-1)[0];
    const res = await makeRequest('http://localhost:8080/api/books/' + url, "GET");
    const book =  await res.json();
    updateInfo(book);
}

function updateInfo(book) {
    document.title = book.title;
    
    const container = document.createElement("div");
    container.classList.add("book-detail");

	let soldOut = '';
	let but = `<button class="add-to-cart" onclick="addToCart(${book.id})">Add to cart</button>`;
	if(book.quantity === 0){
		but = '';
		soldOut = "<div class='sold-out-overlay'>Sold out</div>";
	}

    const bookDetailsHTML = `
        <div class="book-cover">
            <img src="${book.coverUrl}" alt="Cover" class="cover">
            ${soldOut}
        </div>
        <div class="book-info">
            <h2 class="book-title">${book.title}</h2>
            <p class="book-author">Author: ${book.author}</p>
            <p class="book-price">Price: ${book.price.toFixed(2)} грн.</p>
            <p class="book-genre">Genre: ${book.genre}</p>
            <p class="book-language">Language: ${book.language}</p>
            <p class="book-pages">Number of pages: ${book.numbersOfPages}</p>
            <p>Quantity: ${book.quantity}</p>
            <p class="book-publishing-date">Publication date: ${book.dateOfPublishing || "Not specified"}</p>
            <p class="book-description">Description: ${book.description || "Not specified"}</p>
            <a href="../profile?user=${book.user.id}">See profile of creator: ${book.user.username}</a>
            ${but}
            
        </div>
    `;

    container.innerHTML = bookDetailsHTML;
    document.querySelector(".container").appendChild(container);
}

async function addToCart(id) {
    try {
        await makeRequest(`http://localhost:8080/api/cart/${id}`, "POST");
        showNotification('Book added to cart!', 'success');
    } catch (error) {
        showNotification(error.message, 'error');
    }
}

function showNotification(message, type) {
    const notification = document.getElementById('message');
    notification.className = `notification ${type}`;
    notification.textContent = message;
    notification.style.display = 'block'

    setTimeout(() => {
        notification.style.display = 'none';
    }, 3000);
}

document.addEventListener("DOMContentLoaded", getBook);
