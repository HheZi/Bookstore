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
        const res = await makeRequest(`http://localhost:8080/books/allBooks/${user}`, "GET");
        const books = await res.json();
        updateTable(books);
    } catch (error) {
        showNotification(error.message || 'Ошибка при загрузке книг', 'error');
    }
}

function updateTable(books) {
    const tbody = document.querySelector("#booksTable tbody");
    tbody.innerHTML = '';

    books.forEach(book => {
        const row = document.createElement("tr");
		
		let but = `<button id="butt${book.id}">Add to cart</button>`;
		if (user == userAuth.id) {
			but = `<button id="butt${book.id}">Delete</button>`;
		}
		
        row.innerHTML = `
            <td>
                <a href="/seeBook/${book.id}">
                    <img src="${book.coverUrl}" alt="Cover" class="cover">
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
                ${but}
            </td>
        `;

        tbody.appendChild(row);
        document.querySelector(`#butt${book.id}`).addEventListener("click", async () => {
            if (user == userAuth.id) {
                try {
                    await makeRequest(`http://localhost:8080/books/${book.id}`, "DELETE");
                    getBooksByUser();
                } catch (error) {
                    showNotification(error.message || 'Ошибка при удалении книги', 'error');
                }
            } else {
                addToCart(book.id);
            }
        });
    });
}

async function addToCart(id) {
    try {
        await makeRequest(`http://localhost:8080/cart/${id}/${userAuth.id}`, "POST");
        showNotification("Книга добалена в корзину!", "success")
    } catch (error) {
        showNotification(error.message || 'Ошибка при добавлении книги в корзину', 'error');
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
