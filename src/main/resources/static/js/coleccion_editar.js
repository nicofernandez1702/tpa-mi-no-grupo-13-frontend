// Asegurate de importar datos_prueba.js antes de este script
document.addEventListener("DOMContentLoaded", () => {
  const form = document.getElementById("formColeccion");
  const tituloInput = document.getElementById("tituloColeccion");
  const descripcionInput = document.getElementById("descripcionColeccion");
  const fuenteEstatica = document.getElementById("fuenteEstatica");
  const fuenteDinamica = document.getElementById("fuenteDinamica");
  const fuenteProxy = document.getElementById("fuenteProxy");
  const algoritmoSelect = document.getElementById("algoritmoConsenso");


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


    console.log("Colección actualizada:", coleccion);
    alert("Colección guardada correctamente (simulado).");

    // Redirigir a la lista
    window.location.href = "colecciones_admin.html";
  });
});
