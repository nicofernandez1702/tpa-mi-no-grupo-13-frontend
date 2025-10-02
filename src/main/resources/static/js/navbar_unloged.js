document.addEventListener("DOMContentLoaded", () => {
  const navbar = document.getElementById("navbar");
  if (!navbar) return; // seguridad por si falta el div

  navbar.innerHTML = `
    <nav class="navbar navbar-expand-lg navbar-dark bg-primary">
    <div class="container">
      <a class="navbar-brand" href="index.html">MetaMapa</a>
      <button class="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#navbarContent">
        <span class="navbar-toggler-icon"></span>
      </button>
      
      <div class="collapse navbar-collapse" id="navbarContent">
        <ul class="navbar-nav ms-auto text-center">
          <li class="nav-item my-2 w-100 w-md-auto">
            <a class="nav-link" href="index.html">Inicio</a>
          </li>
          <li class="nav-item my-2 w-100 w-md-auto">
            <a class="nav-link" href="colecciones.html">Colecciones</a>
          </li>
          <li class="nav-item my-2 w-100 w-md-auto">
            <a class="nav-link" href="hechos.html">Hechos</a>
          </li>
          <li class="nav-item my-2 w-100 w-md-auto" id="auth-buttons">
            <!-- Estado: no logeado -->
            <a class="btn btn-light ms-md-2 w-100" href="login.html">Ingresar</a>
            <!-- Estado logeado: se mostraría un icono de perfil aquí -->
          </li>
        </ul>
      </div>
    </div>
  </nav>
  `;
});


// Esto es para que al recargar la página no se me scrollee hacia abajo, dejando fuera de vista la navbar al inicio
if ("scrollRestoration" in history) {
  history.scrollRestoration = "manual";
}
