async function makeRequest(url, method, optional, data = null) {
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
    if(optional)
    	return response.json();
}

async function loadCart() {
	const params = new URLSearchParams(document.location.search);
	const user = params.get("user");
    try {
        const cart = await makeRequest(`http://localhost:8080/api/cart/${user}`, "GET", true);
        updateCart(cart);
    } catch (error) {
        console.error("Error loading cart:", error);
    }
}

function updateCart(cart) {
    const tbody = document.querySelector(".cart-table tbody");
    tbody.innerHTML = '';

    let totalPrice = 0;

    cart.forEach((item, index) => {
        const row = document.createElement("tr");

        row.innerHTML = `
            <td><a href="../seeBook/${item.id}"><img src="http://localhost:8080${item.coverUrl}" alt="Cover" class="cover"></a></td>
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
	const form = new FormData(document.getElementById("shippingForm"));
	await makeRequest("http://localhost:8080/api/users/checkout", "POST", false, form);
	notification("Check your email!", "success")
}

function notification(message, type){
		const notification = document.getElementById('notification');
		
		notification.textContent = '';	
		
	 	notification.className = `notification ${type}`;
        notification.textContent = message;		
        notification.style.display = 'block';
	}


document.addEventListener("DOMContentLoaded", () => {
    loadCart();
    document.getElementById("checkoutButton").addEventListener("click",  () => checkout());
});
