let totalPrice = 0;

async function makeRequest(url, method, data = null) {
    const req = {
        method: method,
        headers: {
            'Content-Type': 'application/json'
        },
        body: data
    };

    const response = await fetch(url, req);
    return response;
}

async function loadCart() {
    const res = await makeRequest(`http://localhost:8080/api/cart`, "GET");
    if(!res.ok){
        const error = await res.json();
        notification(error.detail || "Something went wrong", "error");
        return;
    }
    const cart = await res.json();
    updateCart(cart);
}

function updateCart(cart) {
    const tbody = document.querySelector(".cart-table tbody");
    tbody.innerHTML = '';

    cart.forEach((item, index) => {
        const row = document.createElement("tr");

        row.innerHTML = `
            <tr>
        <td>
            <a href="../books/${item.id}">
                <div class="cover-wrapper">
                    <img src="${item.coverUrl}" alt="Cover" class="cover">
                    ${item.quantity === 0 ? '<div class="sold-out-overlay">Sold out</div>' : ''}
                </div>
            </a>
        </td>
        <td>${item.title}</td>
        <td>${item.author}</td>
        <td>${item.price.toFixed(2)} UA.</td>
        <td>
            <button id="decrease-button-${index}" data-index="${index}" onclick="decreaseQuantity(${item.id}, ${index})">-</button>
            <span id="quantity-${index}" class="quantity-display">${item.quantityToPurchase}</span>
            <button id="increase-button-${index}" data-index="${index}" onclick="increaseQuantity(${item.id}, ${index})">+</button>
        </td>
        <td><button class="remove-button" onclick="removeFromCart(${item.id})">Delete</button></td>
    </tr>
        `;
		
        tbody.appendChild(row);
        
        if(item.quantity === 0){
			document.getElementById(`decrease-button-${index}`).disabled = true;
			document.getElementById(`increase-button-${index}`).disabled = true; 
			const but = document.querySelector(`#checkoutButton`);
			but.disabled = true;
		}
        
        totalPrice += item.price;
    });

    if(tbody.childElementCount === 0){
        document.querySelector("#checkoutButton").style.display = "none"
    }

    document.getElementById("totalPrice").textContent = totalPrice.toFixed(2);
}

async function removeFromCart(id) {
    await makeRequest(`http://localhost:8080/api/cart/${id}`, "DELETE");
    loadCart();
}

async function checkout(event){
    event.preventDefault();
    
    const form = document.querySelector(".cart-container form");
    const but = document.getElementById("checkoutButton").disabled = true;
    
    const formData = new FormData(form);
    const formObject = {};
    formData.forEach((value, key) => {
        formObject[key] = value;
    });
	
    const jsonData = JSON.stringify(formObject);
    
	let response;
	try{
	    response = await makeRequest("http://localhost:8080/api/cart/checkout", "POST", jsonData);
        notification("Check your email!", "success");		
	}    
	catch(ex){
        const error = await response.json();
        notification(error.detail || "Error during checkout", "error");		
	}finally{
		but.disabled = false;		
	}
	
}

function makeAvaibleQuantityButton(index){
	const decreaseBut = document.getElementById(`decrease-button-${index}`) 
	const increaseBut = document.getElementById(`increase-button-${index}`) 
	
	decreaseBut.disabled = true;
	increaseBut.disabled = true;
	
	setTimeout(() => {
		decreaseBut.disabled = false;
		increaseBut.disabled = false;
	}, 1500)
}


function updateElem(index, num){
	var quantityElement = document.getElementById(`quantity-${index}`);
    var currentQuantity = parseInt(quantityElement.textContent, 10);
    
    quantityElement.textContent = currentQuantity + num;
}

async function decreaseQuantity(bookid, index) {
    var quantityElement = document.getElementById(`quantity-${index}`);
    var currentQuantity = parseInt(quantityElement.textContent, 10);

	try{
	    if (currentQuantity > 1) {
	        await updateQuantity(bookid, currentQuantity - 1);
	        quantityElement.textContent = currentQuantity - 1;
			makeAvaibleQuantityButton(index)
	    }
	    else{
			document.getElementById(`decrease-button-${index}`).disabled = true
		}
	}
	catch(ex){
		document.getElementById(`increase-button-${index}`).disabled = true
		notification(ex.message || "Error", "error");
	}
}

async function increaseQuantity(bookid, index) {
    var quantityElement = document.getElementById(`quantity-${index}`);
    var currentQuantity = parseInt(quantityElement.textContent, 10);
	try{
	    await updateQuantity(bookid, currentQuantity + 1);
	    quantityElement.textContent = currentQuantity + 1;		
		makeAvaibleQuantityButton(index)
	}
	catch(ex) {
		document.getElementById(`increase-button-${index}`).disabled = true
		notification(ex.message || "Something went wrong", "error");
	}
}

async function updateQuantity(bookid, quantity) {
	const res = await makeRequest(`http://localhost:8080/api/cart/${bookid}/quantity?value=${quantity}`, "PUT")
	if(!res.ok){
        const error = await res.json();
        throw new Error(error.detail || "Something went wrong");			

    }
	const totalPrice = await res.json();
	
	document.getElementById("totalPrice").textContent = totalPrice.toFixed(2);
}


function notification(message, type){
    const notification = document.getElementById('notification');

    notification.textContent = '';
    notification.className = `notification ${type}`;
    notification.textContent = message;
    notification.style.display = 'block';

    setTimeout(() => {
        notification.style.display = 'none';
    }, 3000);
}

document.addEventListener("DOMContentLoaded", () => {
    loadCart();
	document.querySelector(".shipping-form").addEventListener("submit", (event) => checkout(event));
});

