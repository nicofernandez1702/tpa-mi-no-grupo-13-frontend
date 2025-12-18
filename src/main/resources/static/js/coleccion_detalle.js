// coleccion_detalle.js

let mapa;
let markers = [];
const itemsPorPagina = 10;
let paginaActual = 1;

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

    // Mostrar la primera página al inicio
    actualizarMapaYLista(hechosBase);
});

// === Función para limpiar/agregar pines y actualizar listado paginado ===
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

    // Paginación: calcular slice según página actual
    const lista = document.getElementById("lista-hechos");
    lista.innerHTML = "";

    const inicio = (paginaActual - 1) * itemsPorPagina;
    const fin = inicio + itemsPorPagina;
    const hechosPagina = listaHechos.slice(inicio, fin);

    hechosPagina.forEach(h => {
        const li = document.createElement("li");
        li.className = "list-group-item";
        li.innerHTML = `
            <h5>${h.titulo}</h5>
            <p>Fuente: ${h.fuente || "Desconocida"}</p>
            <small>Fecha: ${h.fecha_hecho || "No disponible"} | Categoría: ${h.categoria || "Sin categoría"}</small>
            <br>
            <a href="/hechos/${h.id}?from=coleccion&coleccionId=${coleccionId}" 
               class="btn btn-sm btn-outline-primary mt-2">
                Ver detalle
            </a>
        `;
        lista.appendChild(li);
    });

    renderizarPaginacion(listaHechos.length, listaHechos);
}

// === Función para renderizar la paginación ===
function renderizarPaginacion(totalItems, listaHechos) {
    const totalPaginas = Math.ceil(totalItems / itemsPorPagina);
    const paginacion = document.getElementById("paginacion");
    paginacion.innerHTML = "";

    const ventana = 5; // número de páginas a mostrar alrededor de la actual
    let inicio = Math.max(paginaActual - Math.floor(ventana / 2), 1);
    let fin = inicio + ventana - 1;

    if (fin > totalPaginas) {
        fin = totalPaginas;
        inicio = Math.max(fin - ventana + 1, 1);
    }

    // Botón “Primera página”
    const liFirst = document.createElement("li");
    liFirst.className = `page-item ${paginaActual === 1 ? "disabled" : ""}`;
    liFirst.innerHTML = `<a class="page-link" href="#">|<<</a>`;
    liFirst.addEventListener("click", (e) => {
        e.preventDefault();
        if (paginaActual !== 1) {
            paginaActual = 1;
            actualizarMapaYLista(listaHechos);
        }
    });
    paginacion.appendChild(liFirst);

    // Botón “Anterior”
    const liPrev = document.createElement("li");
    liPrev.className = `page-item ${paginaActual === 1 ? "disabled" : ""}`;
    liPrev.innerHTML = `<a class="page-link" href="#"><<</a>`;
    liPrev.addEventListener("click", (e) => {
        e.preventDefault();
        if (paginaActual > 1) {
            paginaActual--;
            actualizarMapaYLista(listaHechos);
        }
    });
    paginacion.appendChild(liPrev);

    // Botones de página
    for (let i = inicio; i <= fin; i++) {
        const li = document.createElement("li");
        li.className = `page-item ${i === paginaActual ? "active" : ""}`;
        li.innerHTML = `<a class="page-link" href="#">${i}</a>`;
        li.addEventListener("click", (e) => {
            e.preventDefault();
            paginaActual = i;
            actualizarMapaYLista(listaHechos);
        });
        paginacion.appendChild(li);
    }

    // Botón “Siguiente”
    const liNext = document.createElement("li");
    liNext.className = `page-item ${paginaActual === totalPaginas ? "disabled" : ""}`;
    liNext.innerHTML = `<a class="page-link" href="#">>></a>`;
    liNext.addEventListener("click", (e) => {
        e.preventDefault();
        if (paginaActual < totalPaginas) {
            paginaActual++;
            actualizarMapaYLista(listaHechos);
        }
    });
    paginacion.appendChild(liNext);

    // Botón “Última página”
    const liLast = document.createElement("li");
    liLast.className = `page-item ${paginaActual === totalPaginas ? "disabled" : ""}`;
    liLast.innerHTML = `<a class="page-link" href="#">>>|</a>`;
    liLast.addEventListener("click", (e) => {
        e.preventDefault();
        if (paginaActual !== totalPaginas) {
            paginaActual = totalPaginas;
            actualizarMapaYLista(listaHechos);
        }
    });
    paginacion.appendChild(liLast);
}



// === Filtros ===
document.getElementById("filtros-form")?.addEventListener("submit", function (e) {
    e.preventDefault();

    const fechaDesde = document.getElementById("filtro-fecha-desde")?.value;
    const fechaHasta = document.getElementById("filtro-fecha-hasta")?.value;
    const categoria = document.getElementById("filtro-categoria")?.value;
    const fuente = document.getElementById("filtro-fuente")?.value;

    const hechosFiltrados = hechosBase.filter(h => {
        let match = true;

        if (fechaDesde) match = match && new Date(h.fecha_hecho) >= new Date(fechaDesde);
        if (fechaHasta) match = match && new Date(h.fecha_hecho) <= new Date(fechaHasta);
        if (categoria) match = match && normalizarTexto(h.categoria) === normalizarTexto(categoria);
        if (fuente) match = match && h.fuente === fuente;

        return match;
    });

    paginaActual = 1; // Resetear a primera página
    actualizarMapaYLista(hechosFiltrados);
});

// Botón para limpiar filtros
document.getElementById("limpiar-filtros")?.addEventListener("click", () => {
    document.getElementById("filtros-form").reset();
    paginaActual = 1;
    actualizarMapaYLista(hechosBase);
});

// === Función de normalización de texto ===
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
});

btnIrrestricto?.addEventListener("click", () => {
    modo = "irrestricto";
    btnIrrestricto.classList.replace("btn-outline-primary", "btn-primary");
    btnCurado.classList.replace("btn-primary", "btn-outline-primary");
});
