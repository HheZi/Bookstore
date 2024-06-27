document.getElementById('formPost').addEventListener('submit', async function(event) {
    event.preventDefault();
	
    const error = document.querySelector(".error-message");
    const success = document.querySelector(".success-message");
	
    const form = document.getElementById('formPost');
    const formData = new FormData(form);
	
	try{
	    await fetch('http://localhost:8080/books', {
	        method: 'POST',
	        body: formData
	    });
        success.textContent = 'Successfully created!';
		
	}catch(ex){
        error.textContent = 'Wrong input';		
	}

});
