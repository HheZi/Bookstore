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
        updatePagination(res.page, titleFilter);
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

        let buttonHTML = `<button onClick="addToCart(${book.id})">Add to cart</button>`;
        let soldOutOverlay = "";

        if (book.quantity === 0) {
            buttonHTML = "";
            soldOutOverlay = `<div class='sold-out-overlay'>Sold out</div>`;
        }

        bookDiv.innerHTML = `
            <div class='book-cover'>
                <a href="../books/${book.id}" class="bookSrc">
                    <img src='${book.coverUrl}' alt='Book cover'>
                    ${soldOutOverlay}
                </a>
            </div>
            <div class='book-info'>
                <h2>${book.title}</h2>
                <p>Author: ${book.author}</p>
                <p>Quantity: ${book.quantity}</p>
                <p class='price'>Price: ${book.price.toFixed(2)} UA.</p>
                ${buttonHTML}
            </div>
        `;

        container.appendChild(bookDiv);
    });
}

async function addToCart(id) {
    try {
        await makeRequest(`http://localhost:8080/api/cart/${id}`, "POST");
        showNotification("The book has been added to cart!", "success");
    } catch (error) {
        showNotification(error.message || 'Something went wrong', 'error');
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

function updatePagination(page, titleFilter = '') {
    const paginationContainer = document.querySelector(".pagination");
    paginationContainer.innerHTML = '';

    for (let i = 0; i < page.totalPages; i++) {
        const pageLink = document.createElement("a");
        pageLink.textContent = i + 1;
        pageLink.className = "pagination-link";
        if (i === page.number) {
            pageLink.classList.add("active");
        }
        pageLink.addEventListener('click', (event) => {
            event.preventDefault();
            loadAll(i, page.size, titleFilter);
        });
        paginationContainer.appendChild(pageLink);
    }
}

document.addEventListener("DOMContentLoaded", () => {
    const params = new URLSearchParams(window.location.search);
    const titleFilter = params.get('titleFilter') || '';
    document.getElementById('searchInput').value = titleFilter;
    loadAll(0, 10, titleFilter);
});

