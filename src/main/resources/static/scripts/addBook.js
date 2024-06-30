document.getElementById('formPost').addEventListener('submit', async function(event) {
    event.preventDefault();
    
    const message = document.querySelector("#message");

    const form = document.getElementById('formPost');
    const formData = new FormData(form);
    
    try {
        const response = await fetch('http://localhost:8080/api/books', {
            method: 'POST',
            body: formData
        });
        
        if (!response.ok) {
            const errorData = await response.json();
            throw new Error(errorData.detail || 'Произошла ошибка');
        }
        
        message.textContent = 'Successfully created!';
        message.className = "success-message";
    } catch (ex) {
        message.textContent = ex.message;
        message.className = "error-message";
    }
});
