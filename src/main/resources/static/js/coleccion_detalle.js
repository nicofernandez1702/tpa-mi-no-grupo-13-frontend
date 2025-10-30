// === Variables globales ===
let colecciones = [];
let coleccionesFiltradas = [];

// === Inicializar datos desde Thymeleaf ===
document.addEventListener("DOMContentLoaded", () => {
    // Convertimos las colecciones Thymeleaf a JS
    const coleccionesJson = document.getElementById("grid-colecciones").dataset.colecciones;
    if (coleccionesJson) {
        colecciones = JSON.parse(coleccionesJson);
        coleccionesFiltradas = [...colecciones];
        renderizarColecciones();
    }

    // Evento búsqueda
    document.getElementById("busqueda-colecciones").addEventListener("input", (e) => {
        const termino = e.target.value.toLowerCase();
        coleccionesFiltradas = colecciones.filter(c => c.titulo.toLowerCase().includes(termino));
        renderizarColecciones();
    });

    // Ordenamiento
    document.querySelectorAll("[data-orden]").forEach(btn => {
        btn.addEventListener("click", () => {
            const tipo = btn.dataset.orden;
            ordenarColecciones(tipo);
            renderizarColecciones();
        });
    });

    // Botón de eliminar
    document.getElementById("btnConfirmarEliminar").addEventListener("click", () => {
        const id = document.getElementById("btnConfirmarEliminar").dataset.id;
        eliminarColeccion(id);
    });
});

// === Renderizar colecciones en el DOM ===
function renderizarColecciones() {
    const grid = document.getElementById("grid-colecciones");
    if (!grid) return;

    grid.innerHTML = "";
    if (coleccionesFiltradas.length === 0) {
        grid.innerHTML = `<div class="col-12 text-center py-5"><h3>No hay colecciones registradas</h3></div>`;
        return;
    }

    coleccionesFiltradas.forEach(c => {
        grid.innerHTML += `
            <div class="col">
                <div class="card h-100 shadow-sm">
                    <div class="card-body d-flex flex-column">
                        <h5 class="card-title">${c.titulo}</h5>
                        <p class="card-text text-muted">${c.descripcion}</p>
                        <p class="mb-1"><strong>Cantidad de hechos:</strong> ${c.cantidadHechos}</p>
                        <p class="mb-3"><strong>Última actualización:</strong> ${c.fechaActualizacion}</p>
                        <div class="mt-auto d-flex justify-content-between">
                            <a href="/colecciones/editar/${c.id}" class="btn btn-sm btn-primary">
                                <i class="bi bi-pencil-square me-1"></i>Editar
                            </a>
                            <button class="btn btn-sm btn-danger" onclick="abrirModalEliminar('${c.id}', '${c.titulo}')">
                                <i class="bi bi-trash me-1"></i>Eliminar
                            </button>
                        </div>
                    </div>
                </div>
            </div>
        `;
    });
}

// === Abrir modal de eliminación ===
function abrirModalEliminar(id, titulo) {
    const modal = new bootstrap.Modal(document.getElementById("modalEliminar"));
    document.getElementById("modalEliminarTitulo").textContent = titulo;
    document.getElementById("btnConfirmarEliminar").dataset.id = id;
    modal.show();
}

// === Eliminar colección (ejemplo: llamada fetch al backend) ===
function eliminarColeccion(id) {
    fetch(`/colecciones/eliminar/${id}`, { method: "DELETE" })
        .then(res => {
            if (res.ok) {
                colecciones = colecciones.filter(c => c.id !== id);
                coleccionesFiltradas = coleccionesFiltradas.filter(c => c.id !== id);
                renderizarColecciones();
                const modal = bootstrap.Modal.getInstance(document.getElementById("modalEliminar"));
                modal.hide();
            } else {
                alert("No se pudo eliminar la colección");
            }
        })
        .catch(err => console.error(err));
}

// === Ordenar colecciones ===
function ordenarColecciones(tipo) {
    if (tipo === "alfabetico") {
        coleccionesFiltradas.sort((a, b) => a.titulo.localeCompare(b.titulo));
    } else if (tipo === "cantidad") {
        coleccionesFiltradas.sort((a, b) => b.cantidadHechos - a.cantidadHechos);
    } else if (tipo === "fecha") {
        coleccionesFiltradas.sort((a, b) => new Date(b.fechaActualizacion) - new Date(a.fechaActualizacion));
    }
}

