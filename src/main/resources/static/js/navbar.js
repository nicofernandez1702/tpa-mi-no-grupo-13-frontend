// Simulamos un usuario logeado
  const usuario = {
    nombre: "Usuario Contribuyente",
    logeado: true
  };

  const navItems = document.getElementById('nav-items');
  const authButtons = document.getElementById('auth-buttons');

  if (usuario.logeado) {
    // Agregar botón "Mis Hechos"
    const misHechosItem = document.createElement('li');
    misHechosItem.className = "nav-item my-2 me-2 text-nowrap text-center";
    misHechosItem.innerHTML = `<a class="nav-link" href="hechos_contribuyente.html">Mis Hechos</a>`;
    navItems.insertBefore(misHechosItem, authButtons);

    // Botón de perfil con dropdown
    authButtons.innerHTML = `
      <div class="dropdown ">
        <button class="btn btn-light dropdown-toggle d-flex mx-auto align-items-center" type="button" id="userDropdown" data-bs-toggle="dropdown" aria-expanded="false">
          <i class="bi bi-person-fill me-2"></i> ${usuario.nombre}
        </button>
        <ul class="dropdown-menu dropdown-menu-end text-center" aria-labelledby="userDropdown">
          <li><a class="dropdown-item" href="perfil.html">Perfil</a></li>
          <li><hr class="dropdown-divider"></li>
          <li><a class="dropdown-item" href="#" id="logout-btn">Logout</a></li>
        </ul>
      </div>
    `;

    // Evento logout
    document.getElementById('logout-btn').addEventListener('click', () => {
      alert('Cerrando sesión...');
      // Aquí borrarías sesión / localStorage y redirigirías
      window.location.href = "index.html";
    });

  } else {
    authButtons.innerHTML = `<a class="btn btn-light ms-md-2 w-100" href="login.html">Ingresar</a>`;
  }