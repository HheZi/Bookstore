document.querySelector("#reg").addEventListener("click", async function(event){
    event.preventDefault();

    const message = document.querySelector("#message");
    const form = new FormData(document.querySelector("#registerForm"));

    try {
        const response = await fetch("http://localhost:8080/api/users/reg", {
            method: "POST",
            body: form
        });

        if (!response.ok) {
            const errorData = await response.json();
            throw new Error(errorData.detail || 'Something went wrong');
        }

        message.className = "success-message";
        message.textContent = "User created!";
    } catch (ex) {
        message.className = "error-message";
        message.textContent = ex.message;
    }
});
