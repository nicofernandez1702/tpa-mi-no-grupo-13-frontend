// Asegurate de importar datos_prueba.js antes de este script
document.addEventListener("DOMContentLoaded", () => {
  const form = document.getElementById("formColeccion");
  const tituloInput = document.getElementById("tituloColeccion");
  const descripcionInput = document.getElementById("descripcionColeccion");
  const fuenteEstatica = document.getElementById("fuenteEstatica");
  const fuenteDinamica = document.getElementById("fuenteDinamica");
  const fuenteProxy = document.getElementById("fuenteProxy");
  const algoritmoSelect = document.getElementById("algoritmoConsenso");

  // Obtener query param
  const urlParams = new URLSearchParams(window.location.search);
  const coleccionId = parseInt(urlParams.get("id"));
  if (!coleccionId) {
    alert("No se especificó el ID de la colección.");
    window.location.href = "colecciones_admin.html";
  }

  // Buscar la colección en datos_prueba.js
  const coleccion = colecciones.find(c => c.id === coleccionId);
  if (!coleccion) {
    alert("Colección no encontrada.");
    window.location.href = "colecciones_admin.html";
  }

  // Rellenar formulario
  tituloInput.value = coleccion.titulo;
  descripcionInput.value = coleccion.descripcion;

  // Configurar checkboxes de fuentes
  // Si coleccion tiene solo una fuente en string, convertimos a array
  const fuentes = Array.isArray(coleccion.fuentes) ? coleccion.fuentes : [coleccion.fuente];
  fuenteEstatica.checked = fuentes.includes("Estática");
  fuenteDinamica.checked = fuentes.includes("Dinámica");
  fuenteProxy.checked = fuentes.includes("Proxy");

  algoritmoSelect.value = coleccion.consenso || "mayoria";

  // Guardar cambios (simulado)
  form.addEventListener("submit", (e) => {
    e.preventDefault();

    // Obtener fuentes seleccionadas
    const fuentesSeleccionadas = [];
    if (fuenteEstatica.checked) fuentesSeleccionadas.push("Estática");
    if (fuenteDinamica.checked) fuentesSeleccionadas.push("Dinámica");
    if (fuenteProxy.checked) fuentesSeleccionadas.push("Proxy");

    // Actualizar objeto coleccion
    coleccion.titulo = tituloInput.value;
    coleccion.descripcion = descripcionInput.value;
    coleccion.fuentes = fuentesSeleccionadas;
    coleccion.consenso = algoritmoSelect.value;

    console.log("Colección actualizada:", coleccion);
    alert("Colección guardada correctamente (simulado).");

    // Redirigir a la lista
    window.location.href = "colecciones_admin.html";
  });
});
