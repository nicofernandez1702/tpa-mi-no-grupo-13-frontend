// hecho_detalle.js

document.addEventListener("DOMContentLoaded", () => {
  // 1. Obtener ID desde la URL
  const params = new URLSearchParams(window.location.search);
  const hechoId = parseInt(params.get("id"));

  // 2. Buscar el hecho en todas las colecciones
  let hecho = null;
  for (const coleccion of colecciones) {
    hecho = coleccion.hechos.find(h => h.id === hechoId);
    if (hecho) break;
  }

  if (!hecho) {
    document.querySelector("main").innerHTML = "<p class='text-center text-danger'>Hecho no encontrado</p>";
    return;
  }

  // 3. Insertar datos en el DOM
  document.querySelector("h1").textContent = hecho.titulo;
  document.querySelector(".text-muted span").textContent = hecho.categoria || "Sin categoría";
  document.querySelector("section .col-md-8 p").textContent = hecho.descripcion;
  document.querySelector("section .col-md-4 p").textContent = hecho.lugar || "Ubicación desconocida";
  
  // Imagen opcional (si existe en el dato de prueba)
  const imgEl = document.querySelector("section img");
  if (hecho.imagen) {
    imgEl.src = hecho.imagen;
    imgEl.alt = "Imagen de " + hecho.titulo;
  } else {
    imgEl.style.display = "none";
  }

  // Fuente
  const fuenteEl = document.querySelector("section a");
  if (hecho.fuente) {
    fuenteEl.href = hecho.fuente;
    fuenteEl.textContent = hecho.fuente;
  } else {
    fuenteEl.style.display = "none";
  }

  // 4. Inicializar Leaflet en la ubicación del hecho
  // Se requiere que los hechos tengan lat/lng en los datos de prueba
  const coords = hecho.coords || hecho.latLng || [ -34.6037, -58.3816 ]; // fallback
  const map = L.map("mapa-detalle").setView(coords, 13);

  L.tileLayer("https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png", {
    attribution: '&copy; <a href="https://www.openstreetmap.org/copyright">OpenStreetMap</a>'
  }).addTo(map);

  L.marker(coords)
    .addTo(map)
    .bindPopup(`<b>${hecho.titulo}</b><br>${hecho.lugar || hecho.ubicacion || ""}`);
});


// Solicitud Eliminación

const justificacion = document.getElementById("justificacion");
  const contador = document.getElementById("contador");
  const btnEnviar = document.getElementById("btnEnviar");

  justificacion.addEventListener("input", () => {
    const length = justificacion.value.length;
    contador.textContent = `${length}/500 caracteres`;
    btnEnviar.disabled = length < 500;
  });