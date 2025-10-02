// navbar_admin.js

document.addEventListener("DOMContentLoaded", () => {
  const navbarContainer = document.getElementById("navbar");

  navbarContainer.innerHTML = `
    <nav class="navbar navbar-expand-lg navbar-dark bg-primary">
      <div class="container">
        <a class="navbar-brand" href="control_panel.html">MetaMapa Admin</a>
        <button class="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#navbarContent">
          <span class="navbar-toggler-icon"></span>
        </button>
        
        <div class="collapse navbar-collapse" id="navbarContent">
          <ul class="navbar-nav ms-auto text-center">

            <li class="nav-item my-2 w-100 w-md-auto">
              <a class="nav-link" href="control_panel.html">Dashboard</a>
            </li>

            <li class="nav-item my-2 w-100 w-md-auto">
              <a class="nav-link" href="colecciones_admin.html">Colecciones</a>
            </li>

            <li class="nav-item my-2 w-100 w-md-auto">
              <a class="nav-link text-nowrap" href="hechos_pendientes.html">Hechos Pendientes</a>
            </li>

            <li class="nav-item my-2 w-100 w-md-auto">
              <a class="nav-link" href="solicitudes_admin.html">Solicitudes</a>
            </li>

            <!-- Dropdown de usuario admin -->
            <li class="nav-item my-2 w-100 text-center">
              <div class="dropdown">
                <button class="btn btn-light dropdown-toggle d-flex mx-auto align-items-center" 
                        type="button" id="adminDropdown" 
                        data-bs-toggle="dropdown" aria-expanded="false">
                  <i class="bi bi-person-fill me-2"></i> Admin
                </button>
                <ul class="dropdown-menu dropdown-menu-end text-center" aria-labelledby="adminDropdown">
                  <li><a class="dropdown-item" href="perfil_admin.html">Perfil</a></li>
                  <li><hr class="dropdown-divider"></li>
                  <li><a class="dropdown-item" href="index.html" id="logout-btn">Logout</a></li>
                </ul>
              </div>
            </li>
          </ul>
        </div>
      </div>
    </nav>
  `;

  // Logout simulado
  const logoutBtn = document.getElementById("logout-btn");
  if (logoutBtn) {
    logoutBtn.addEventListener("click", (e) => {
      e.preventDefault();
      alert("Cerrando sesión de administrador...");
      window.location.href = "index.html"; // redirigir al inicio
    });
  }
});

// Desactiva la restauración del scroll
if ("scrollRestoration" in history) {
  history.scrollRestoration = "manual";
}
