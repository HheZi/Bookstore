async function makeRequest(url, method, data = null) {
    const req = {
        method: method
    };

    if (data) {
        req.headers = {
            'Content-Type': 'application/json'
        };
        req.body = JSON.stringify(data);
    }

    const response = await fetch(url, req);
    if (!response.ok) {
        const errorData = await response.json();
        throw new Error(errorData.detail);
    }
    
    return response;
}

async function loadAll(page = 0, size = 10, titleFilter = '') {
    try {
        const response = await makeRequest(`http://localhost:8080/api/books?page=${page}&size=${size}&titleFilter=${titleFilter}`, "GET");
        const res = await response.json();

        updatePage(res.content);
        updatePagination(res.pageable, res.totalPages, titleFilter);
    } catch (error) {
        console.error("Error loading books:", error);
    }
}

function updatePage(books) {
    if (!Array.isArray(books)) {
        console.error("Expected an array but got:", books);
        return;
    }

    const container = document.querySelector(".containerOfbooks");
    if (!container) {
        console.error("Container not found");
        return;
    }

    container.innerHTML = "";

    books.forEach((book) => {
        const bookDiv = document.createElement("div");
        bookDiv.classList.add("book-block");

        bookDiv.innerHTML = `
            <div class='book-cover'>
                <a href="/books/${book.id}" class="bookSrc">
                    <img src='${book.coverUrl}' alt='Обложка книги'>
                </a>
            </div>
            <div class='book-info'>
                <h2>${book.title}</h2>
                <p>Автор: ${book.author}</p>
                <p>Количество: ${book.quantity}</p>
                <p class='price'>Цена: ${book.price.toFixed(2)} грн.</p>
                <button onClick="addToCart(${book.id})">Добавить в корзину</button>
            </div>
        `;

        container.appendChild(bookDiv);
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
    const notification = document.createElement('div');
    notification.className = `notification ${type}`;
    notification.textContent = message;

    document.body.appendChild(notification);

    setTimeout(() => {
        notification.remove();
    }, 5000);
}

function updatePagination(pageable, totalPages, titleFilter = '') {
    const paginationContainer = document.querySelector(".pagination");
    paginationContainer.innerHTML = '';

    for (let i = 0; i < totalPages; i++) {
        const pageLink = document.createElement("a");
        pageLink.textContent = i + 1;
        pageLink.classList.add("pagination-link");
        pageLink.addEventListener('click', (event) => {
            event.preventDefault();
            loadAll(i, pageable.pageSize, titleFilter);
        });
        paginationContainer.appendChild(pageLink);
    }
}

document.addEventListener("DOMContentLoaded", () => loadAll());

document.getElementById('searchButton').addEventListener('click', () => {
    const titleFilter = document.getElementById('searchInput').value;
    loadAll(0, 10, titleFilter);
});
