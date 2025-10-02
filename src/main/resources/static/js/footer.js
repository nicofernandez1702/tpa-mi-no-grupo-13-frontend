document.addEventListener("DOMContentLoaded", () => {
  const navbar = document.getElementById("footer");
  if (!navbar) return; // seguridad por si falta el div

  navbar.innerHTML = `
    <footer id="legal" class="bg-primary text-white text-center py-3 mt-5">
    <p>&copy; 2025 MetaMapa | <a href="legal.html" class="text-white text-decoration-underline">Privacidad y Legal</a></p>
  </footer>
  `;
});

