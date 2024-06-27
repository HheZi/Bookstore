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
    if (!response.ok) {
        const errorData = await response.json();
        throw new Error(errorData.message);
    }
    return response.json();
}

async function loadUserData(userId) {
    try {
        const user = await makeRequest(`http://localhost:8080/users/${userId}`, "GET");
        document.getElementById('username').value = user.username;
        document.getElementById('email').value = user.email;
        document.getElementById('user-avatar').src = user.avatarUrl;

        if (userAuth.id !== userId) {
            document.querySelectorAll('input').forEach(input => input.disabled = true);
            document.querySelector('button').style.display = 'none';
            document.getElementById('avatarUpload').style.display = 'none';
        }
    } catch (error) {
        showNotification(error.message, 'error');
    }
}

async function updateUserData(userId) {
    const userData = {
        email: document.getElementById('email').value,
    };

    try {
        await makeRequest(`http://localhost:8080/users/${userId}`, "PUT", userData);
        showNotification("Данные успешно обновлены!", "success");
    } catch (error) {
        showNotification(error.message, 'error');
    }
}

async function uploadAvatar(userId) {
    const fileInput = document.getElementById('avatarUpload');
    const file = fileInput.files[0];

    if (file) {
        const formData = new FormData();
        formData.append('avatar', file);

        try {
            const response = await fetch(`http://localhost:8080/users/${userId}/avatar`, {
                method: 'POST',
                body: formData
            });

            if (!response.ok) {
                const errorData = await response.json();
                throw new Error(errorData.message);
            }

            const result = await response.json();
            document.getElementById('avatar').src = result.avatarUrl;
            showNotification("Аватар успешно обновлен!", "success");
        } catch (error) {
            showNotification(error.message, 'error');
        }
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
    const userId = new URLSearchParams(window.location.search).get('user');
    loadUserData(userId);

    document.getElementById('userForm').addEventListener('submit', (event) => {
        event.preventDefault();
        if (userAuth.id === userId) {
            updateUserData(userId);
        }
    });

    document.getElementById('avatarUpload').addEventListener('change', () => {
        if (userAuth.id === userId) {
            uploadAvatar(userId);
        }
    });

    document.querySelector('.file-input').addEventListener('click', () => {
        if (userAuth.id === userId) {
            document.getElementById('avatarUpload').click();
        }
    });
});
