    document.addEventListener("DOMContentLoaded", () => {
    const modalHecho = new bootstrap.Modal(document.getElementById("modalHecho"));
    const modalHechoTitulo = document.getElementById("modalHechoTitulo");
    const modalHechoColeccion = document.getElementById("modalHechoColeccion");
    const modalHechoCategoria = document.getElementById("modalHechoCategoria");
    const modalHechoUbicacion = document.getElementById("modalHechoUbicacion");
    const modalHechoFecha = document.getElementById("modalHechoFecha");
    const modalHechoDescripcion = document.getElementById("modalHechoDescripcion");
    const modalHechoFuente = document.getElementById("modalHechoFuente");
    const modalHechoImagen = document.getElementById("modalHechoImagen");

    document.querySelectorAll(".btn-ver").forEach(btn => {
    btn.addEventListener("click", () => {
    modalHechoTitulo.textContent = btn.dataset.titulo;
    modalHechoColeccion.textContent = btn.dataset.coleccion;
    modalHechoCategoria.textContent = btn.dataset.categoria;
    modalHechoUbicacion.textContent = btn.dataset.ubicacion;
    modalHechoFecha.textContent = btn.dataset.fecha;
    modalHechoDescripcion.textContent = btn.dataset.descripcion;
    modalHechoFuente.textContent = btn.dataset.fuente;
    modalHechoImagen.src = btn.dataset.imagen || "";
    modalHecho.show();
});
});
});
