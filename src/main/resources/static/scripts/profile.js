const userId = new URLSearchParams(window.location.search).get('user');

async function makeRequest(url, method, data = null) {
    const req = {
        method: method
    };

    if (data) {
        if (data instanceof FormData) {
            req.body = data;
        } else {
            req.headers = {
                'Content-Type': 'application/json'
            };
            req.body = JSON.stringify(data);
        }
    }

    const response = await fetch(url, req);
    if (!response.ok) {
        const errorData = await response.json();
        throw new Error(errorData.message);
    }
    
    return response;
}

async function loadUserData(userId) {
    try {
        const res = await makeRequest(`http://localhost:8080/api/users/${userId}`, "GET")
        const user = await  res.json();
        document.title = user.username;
        document.getElementById("titleProf").innerText = `Profile of ${user.username}`;
        document.getElementById('usernameOfUser').value = user.username;
        document.getElementById('email').value = user.email;
        document.getElementById('user-avatar').src = user.avatarUrl;
        document.getElementById('hrefSeeBooks').href = `../userBooks?user=${user.id}`;
        
        if (userAuth.id !== user.id) {
            document.querySelectorAll('#userForm input').forEach(input => input.disabled = true);
            document.querySelector('button[type="submit"]').style.display = 'none';
			document.querySelector("#removeAvatar").style.display = 'none'
            document.getElementById('avatarUpload').style.display = 'none';
            document.getElementById("oldPassword").style.display = 'none';
            document.getElementById("password").style.display = 'none';
        }
    } catch (error) {
        showNotification(error.details, 'error');
    }
}

async function updateUserData(userId) {
    const form = new FormData(document.getElementById("userForm"));
    try {
        await makeRequest(`http://localhost:8080/api/users/${userId}`, "PUT", form);
        loadUserData(userId);
        showNotification("Profile is saved!", "success");
    } catch (error) {
        showNotification(error.message, 'error');
    }
}

function showNotification(message, type) {
    const notification = document.getElementById('notification');
    notification.className = `notification ${type}`;
    notification.textContent = message;
    notification.style.display = 'block';

    setTimeout(() => {
        notification.style.display = 'none';
    }, 3000);
}

document.addEventListener("DOMContentLoaded", () => {
    
    loadUserData(userId);

    document.getElementById('userForm').addEventListener('submit', (event) => {
        event.preventDefault();
        updateUserData(userId);
    });
});

document.querySelector("#removeAvatar").addEventListener("click", async () => {
	 try{
		 await makeRequest(`http://localhost:8080/api/users/${userId}/avatar`, "DELETE");
		 loadUserData(userId);
		 showNotification("Avatar is deleted!", "success");
    } catch (error) {
    	showNotification(error.message, 'error');
    }
})