document.querySelector("#reg").addEventListener("click", function(event){
	event.preventDefault();
	
	const message = document.querySelector("#message");
	
	const form = new FormData(document.querySelector("#registerForm"))
	
	fetch("http://localhost:8080/users/reg", {
		method: "POST",
		body: form
	})
	.then(res =>{
		if(!res.ok){
			message.className = "error-message";
			message.textContent = "Wrong Input"
		}
		else{
			message.className = "success-message";
			message.textContent = "User registered!";			
		}
		
	})
	.catch(ex =>{
		message.className = "error-message";
		message.textContent = "Something went wrong"
	});
})