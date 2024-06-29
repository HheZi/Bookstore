var userAuth = null;
function toggleDropdown() {
            document.getElementById("dropdown").classList.toggle("show");
}

        window.onclick = function(event) {
            if (!event.target.matches('.avatar') && !event.target.matches('.avatar img') && !event.target.matches('.avatar span')) {
                var dropdowns = document.getElementsByClassName("dropdown");
                for (var i = 0; i < dropdowns.length; i++) {
                    var openDropdown = dropdowns[i];
                    if (openDropdown.classList.contains('show')) {
                        openDropdown.classList.remove('show');
                    }
                }
            }
        }


	 fetch("http://localhost:8080/users/auth", {method: "GET"})
	 .then(response => response.json()) 
	 .then(user => {
		userAuth = user;
		document.querySelector("#profile").href = `../seeUser?user=${user.id}`
		document.querySelector("#books").href = `../userBooks?user=${user.id}`;
		document.querySelector("#avatarAuth").src = user.avatarUrl;
		document.querySelector("#username").innerHTML = user.username;
		document.querySelector("#cart").href = `../seeCart?user=${user.id}`
	 });

