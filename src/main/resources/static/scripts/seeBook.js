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

    const bookDetailsHTML = `
        <div class="book-cover">
            <img src="${book.coverUrl}" alt="Обложка книги" class="cover">
        </div>
        <div class="book-info">
            <h2 class="book-title">${book.title}</h2>
            <p class="book-author">Автор: ${book.author}</p>
            <p class="book-price">Цена: ${book.price.toFixed(2)} грн.</p>
            <p class="book-genre">Жанр: ${book.genre}</p>
            <p class="book-language">Язык: ${book.language}</p>
            <p class="book-pages">Количество страниц: ${book.numbersOfPages}</p>
            <p>Количество: ${book.quantity}</p>
            <p class="book-publishing-date">Дата публикации: ${book.dateOfPublishing}</p>
            <p class="book-description">Описание: ${book.description}</p>
            <p><a class="book-user" href="../profile?user=${book.user.id}">Пользователь: ${book.user.username}</a></p>
            <button class="add-to-cart" onclick="addToCart(${book.id}, userAuth)">Добавить в корзину</button>
        </div>
    `;

    container.innerHTML = bookDetailsHTML;
    document.querySelector(".container").appendChild(container);
}

async function addToCart(id, userId) {
    try {
        await makeRequest(`http://localhost:8080/api/cart/${id}/${userId.id}`, "POST");
        showNotification('Книга успешно добавлена в корзину!', 'success');
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
