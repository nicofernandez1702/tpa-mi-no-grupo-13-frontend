// coleccion_detalle.js

// === Obtener colección desde la URL ===
const params = new URLSearchParams(window.location.search);
const coleccionId = parseInt(params.get("id"));

// Buscar la colección en el array global (datos_prueba.js)
const coleccion = colecciones.find(c => c.id === coleccionId);

if (!coleccion) {
  document.querySelector("main").innerHTML = "<p class='text-danger'>Colección no encontrada</p>";
  throw new Error("Colección no encontrada");
}

// Mostrar título y descripción en el header de la página
document.querySelector("h1.display-5").textContent = `${coleccion.titulo}`;
document.querySelector("p.text-muted").textContent = coleccion.descripcion;

// Hechos asociados a la colección actual (tomados directamente del array de la colección)
const hechos = coleccion.hechos.map(h => ({
  ...h,
  lat: h.lat || -31.4167, // valor por defecto si no tiene coordenadas
  lng: h.lng || -64.1833
}));

let mapa;
let markers = [];

// === Inicializar mapa y mostrar hechos ===
document.addEventListener("DOMContentLoaded", function () {
  mapa = L.map("mapa-coleccion").setView([-31.4167, -64.1833], 5);
  L.tileLayer("https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png", {
    attribution: "&copy; OpenStreetMap contributors"
  }).addTo(mapa);

  actualizarMapaYLista(hechos);
});

// === Función para limpiar/agregar pines y actualizar listado ===
function actualizarMapaYLista(listaHechos) {
  // limpiar pines anteriores
  markers.forEach(m => mapa.removeLayer(m));
  markers = [];

  // agregar pines nuevos
  listaHechos.forEach(h => {
    const marker = L.marker([h.lat, h.lng])
      .addTo(mapa)
      .bindPopup(`<b>${h.titulo}</b><br><a href="hecho_detalle.html?id=${h.id}" class="btn btn-sm btn-primary w-100 text-white mt-2">Ver detalle</a>`);
    markers.push(marker);
  });

  // actualizar listado
  const lista = document.getElementById("lista-hechos");
  lista.innerHTML = "";
  listaHechos.forEach(h => {
    lista.innerHTML += `
      <li class="list-group-item">
        <h5>${h.titulo}</h5>
        <p class="mb-1">Fuente: ${h.fuente}</p>
        <small>Fecha: ${h.fecha} | Categoría: ${h.categoria}</small>
        <br>
        <a href="hecho_detalle.html?id=${h.id}" class="btn btn-sm btn-outline-primary mt-2">Ver detalle</a>
      </li>
    `;
  });
}

// === Modo navegación ===
const btnCurado = document.getElementById("modo-curado");
const btnIrrestricto = document.getElementById("modo-irrestricto");
let modo = "curado"; // valor inicial

btnCurado.classList.add("btn-primary");
btnIrrestricto.classList.add("btn-outline-primary");

btnCurado.addEventListener("click", () => {
  modo = "curado";
  btnCurado.classList.replace("btn-outline-primary", "btn-primary");
  btnIrrestricto.classList.replace("btn-primary", "btn-outline-primary");
  console.log("Modo seleccionado:", modo);
});

btnIrrestricto.addEventListener("click", () => {
  modo = "irrestricto";
  btnIrrestricto.classList.replace("btn-outline-primary", "btn-primary");
  btnCurado.classList.replace("btn-primary", "btn-outline-primary");
  console.log("Modo seleccionado:", modo);
});

// === Filtros ===
document.getElementById("filtros-form").addEventListener("submit", function (e) {
  e.preventDefault();

  const fecha = document.getElementById("filtro-fecha").value;
  const ubicacion = document.getElementById("filtro-ubicacion").value.toLowerCase();
  const categoria = document.getElementById("filtro-categoria").value;
  const fuente = document.getElementById("filtro-fuente").value;

  const hechosFiltrados = hechos.filter(h => {
    let match = true;
    if (fecha) match = match && h.fecha === fecha;
    if (ubicacion) match = match && h.ubicacion.toLowerCase().includes(ubicacion);
    if (categoria) match = match && h.categoria === categoria;
    if (fuente) match = match && h.fuente === fuente;
    return match;
  });

  actualizarMapaYLista(hechosFiltrados);
});
