document.addEventListener("DOMContentLoaded", function() {
    const bookId = new URLSearchParams(window.location.search).get('book');
    if (bookId) {
        loadBookDetails(bookId);
    }

    const updateBookForm = document.getElementById('updateBookForm');
    updateBookForm.addEventListener('submit', async function(event) {
        event.preventDefault();

        
        const formData = new FormData(updateBookForm);
        
        try {
            const response = await fetch(`http://localhost:8080/api/books/${bookId}`, {
                method: 'PUT',
                body: formData
            });
            if (!response.ok) {
                const errorData = await response.json();
                throw new Error(errorData.detail || 'Error updating book');
            }
           	loadBookDetails(bookId)
            notification('Book updated successfully!', "success")
        } catch (error) {
			 notification(error.message, "error")
        }
        
    });

    async function loadBookDetails(id) {
        try {
            const response = await fetch(`http://localhost:8080/api/books/${id}`);
            if (!response.ok ) {
				const json = await response.json(); 
                throw new Error(json.detail);
            }
   
            const book = await response.json();
            if(book.user.id !== userAuth.id){
				document.querySelector("#updateBookForm").style.display = "none";
				throw new Error("You don't have an access to upgrade this book");
			}
            
            document.getElementById('coverImg').src = book.coverUrl;
            document.getElementById('title').value = book.title;
            document.getElementById('author').value = book.author;
            document.getElementById('genres').value = book.genre;
            document.getElementById('lang').value = book.language;
            document.getElementById('pages').value = book.numbersOfPages;
            document.getElementById('price').value = book.price;
            document.getElementById('quantity').value = book.quantity;
            document.getElementById('description').value = book.description || "";
            document.getElementById('publishDate').value = book.dateOfPublishing;
        } catch (error) {
            notification(error.message, "error");
        }
    }
    
    function notification(message, type){
		const notification = document.getElementById('notification');
		
		notification.textContent = '';	
		
	 	notification.className = `notification ${type}`;
        notification.textContent = message;		
        notification.style.display = 'block';
	}
    
});
