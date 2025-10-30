 const form = document.getElementById("loginForm");
    const message = document.getElementById("message");

    form.addEventListener("submit", function(e) {
      e.preventDefault();

      const email = document.getElementById("email").value;
      const password = document.getElementById("password").value;

      if (email === "NeoMapatha@gmail.com" && password === "Mapatha") {
        message.style.color = "green";
        message.textContent = "✅ Login successful! Redirecting...";
        
        setTimeout(() => {
          window.location.href = "Home.html";
        }, 1000);

      } else {
        message.style.color = "red";
        message.textContent = "❌ Invalid email or password";
      }
    });