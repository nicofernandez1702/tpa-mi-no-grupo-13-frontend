// coleccion_detalle.js

let mapa;
let markers = [];

// === Inicializar mapa y mostrar hechos ===
document.addEventListener("DOMContentLoaded", function () {
    mapa = L.map("mapa-coleccion").setView([-31.4167, -64.1833], 5);

    L.tileLayer("https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png", {
        attribution: "&copy; OpenStreetMap contributors"
    }).addTo(mapa);

    // Normalizar hechos UNA SOLA VEZ
    hechosBase = hechos.map(h => ({
        ...h,
        lat: h.latitud || -31.4167,
        lng: h.longitud || -64.1833
    }));

    actualizarMapaYLista(hechosBase);
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
        <a href="/hechos/${h.id}?from=coleccion&coleccionId=${coleccionId}"
           class="btn btn-sm btn-outline-primary mt-2">
            Ver detalle
        </a>

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
         <a href="/hechos/${h.id}?from=coleccion&coleccionId=${coleccionId}"
           class="btn btn-sm btn-outline-primary mt-2">
            Ver detalle
        </a>
        </li>
      `;
        });
    }
}

// Herramienta para filtros
function normalizarTexto(texto) {
    return texto
        ?.toLowerCase()
        .normalize("NFD")
        .replace(/[\u0300-\u036f]/g, "");
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

    const fechaDesde = document.getElementById("filtro-fecha-desde")?.value;
    const fechaHasta = document.getElementById("filtro-fecha-hasta")?.value;
    const categoria = document.getElementById("filtro-categoria").value;
    const fuente = document.getElementById("filtro-fuente").value;

    const hechosFiltrados = hechosBase.filter(h => {
        let match = true;

        if (fechaDesde) {
            match = match && new Date(h.fecha_hecho) >= new Date(fechaDesde);
        }

        if (fechaHasta) {
            match = match && new Date(h.fecha_hecho) <= new Date(fechaHasta);
        }

        if (categoria) {
            match =
                match &&
                normalizarTexto(h.categoria) === normalizarTexto(categoria);
        }


        if (fuente) {
            match = match && h.fuente === fuente;
        }

        return match;
    });

    actualizarMapaYLista(hechosFiltrados);
});


document.getElementById("limpiar-filtros")?.addEventListener("click", () => {
    document.getElementById("filtros-form").reset();
    actualizarMapaYLista(hechosBase);
});
