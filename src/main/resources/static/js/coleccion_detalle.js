// coleccion_detalle.js

let mapa;
let markers = [];

// === Inicializar mapa y mostrar hechos ===
document.addEventListener("DOMContentLoaded", function () {
    mapa = L.map("mapa-coleccion").setView([-31.4167, -64.1833], 5);
    L.tileLayer("https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png", {
        attribution: "&copy; OpenStreetMap contributors"
    }).addTo(mapa);

    // Asegurarse de que los hechos tengan coordenadas
    const hechosConCoordenadas = hechos.map(h => ({
        ...h,
        lat: h.lat || -31.4167,
        lng: h.lng || -64.1833
    }));

    actualizarMapaYLista(hechosConCoordenadas);
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
            .bindPopup(`
        <b>${h.titulo}</b><br>
        <a href="/hechos/${h.id}" class="btn btn-sm btn-primary w-100 text-white mt-2">Ver detalle</a>
      `);
        markers.push(marker);
    });

    // actualizar listado manualmente solo si querés que reaccione a filtros
    const lista = document.getElementById("lista-hechos");
    if (lista) {
        lista.innerHTML = "";
        listaHechos.forEach(h => {
            lista.innerHTML += `
        <li class="list-group-item">
          <h5>${h.titulo}</h5>
          <p class="mb-1">Fuente: ${h.fuente || "Desconocida"}</p>
          <small>Fecha: ${h.fecha_hecho || "No disponible"} | Categoría: ${h.categoria || "Sin categoría"}</small>
          <br>
          <a href="/hechos/${h.id}" class="btn btn-sm btn-outline-primary mt-2">Ver detalle</a>
        </li>
      `;
        });
    }
}

// === Modo navegación ===
const btnCurado = document.getElementById("modo-curado");
const btnIrrestricto = document.getElementById("modo-irrestricto");
let modo = "curado"; // valor inicial

btnCurado?.classList.add("btn-primary");
btnIrrestricto?.classList.add("btn-outline-primary");

btnCurado?.addEventListener("click", () => {
    modo = "curado";
    btnCurado.classList.replace("btn-outline-primary", "btn-primary");
    btnIrrestricto.classList.replace("btn-primary", "btn-outline-primary");
    console.log("Modo seleccionado:", modo);
});

btnIrrestricto?.addEventListener("click", () => {
    modo = "irrestricto";
    btnIrrestricto.classList.replace("btn-outline-primary", "btn-primary");
    btnCurado.classList.replace("btn-primary", "btn-outline-primary");
    console.log("Modo seleccionado:", modo);
});

// === Filtros ===
document.getElementById("filtros-form")?.addEventListener("submit", function (e) {
    e.preventDefault();

    const fecha = document.getElementById("filtro-fecha").value;
    const ubicacion = document.getElementById("filtro-ubicacion").value.toLowerCase();
    const categoria = document.getElementById("filtro-categoria").value;
    const fuente = document.getElementById("filtro-fuente").value;

    const hechosFiltrados = hechos.filter(h => {
        let match = true;
        if (fecha) match = match && h.fecha_hecho === fecha;
        if (ubicacion) match = match && (h.ubicacion?.toLowerCase().includes(ubicacion));
        if (categoria) match = match && h.categoria === categoria;
        if (fuente) match = match && h.fuente === fuente;
        return match;
    });

    actualizarMapaYLista(hechosFiltrados);
});
