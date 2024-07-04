async function makeRequest(url, method, data = null) {
    const req = {
        method: method,
        headers: {
            'Content-Type': 'application/json'
        }
    };

    if (data) {
        req.body = JSON.stringify(data);
    }

    const response = await fetch(url, req);
    return response;
}

async function loadCart() {
	const params = new URLSearchParams(document.location.search);
	const user = params.get("user");

    const res = await makeRequest(`http://localhost:8080/api/cart/${user}`, "GET");
    if(!res.ok){
    	const error = await res.json();
        notification(error.detail, error.status, "error");
        return;
    }
    const cart = await res.json();
    updateCart(cart);

}

function updateCart(cart) {
    const tbody = document.querySelector(".cart-table tbody");
    tbody.innerHTML = '';

    let totalPrice = 0;

    cart.forEach((item, index) => {
        const row = document.createElement("tr");

        row.innerHTML = `
            <td><a href="../books/${item.id}"><img src="${item.coverUrl}" alt="Cover" class="cover"></a></td>
            <td>${item.title}</td>
            <td>${item.author}</td>
            <td>${item.price.toFixed(2)} грн.</td>
            <td><input type="number" value="1" min="1" data-index="${index}" class="quantity-input"></td>
            <td><button class="remove-button" onclick="removeFromCart(${item.id})">Удалить</button></td>
        `;

        tbody.appendChild(row);
        totalPrice += item.price; 
    });

    document.getElementById("totalPrice").textContent = totalPrice.toFixed(2);
}

async function removeFromCart(id) {
    await makeRequest(`http://localhost:8080/cart/remove/${id}/${userAuth.id}`, "DELETE", false);
    loadCart();
}

async function checkout(){
	document.getElementById("shippingForm").addEventListener("submit", async (event) => {
		event.preventDefault();
		const form = new FormData(document.getElementById("shippingForm"));
		await makeRequest("http://localhost:8080/api/users/checkout", "POST", false, form);
		notification("Check your email!", "success")
	})
}

function notification(message, status, type){
		const notification = document.getElementById('notification');
		
		notification.textContent = '';	
		
		if(status === 403)
			document.querySelector(".cart-container").style.display = 'none';
		
	 	notification.className = `notification ${type}`;
        notification.textContent = message;		
        notification.style.display = 'block';
	}


document.addEventListener("DOMContentLoaded", () => {
    loadCart();
    document.getElementById("checkoutButton").addEventListener("click",  () => checkout());
});
