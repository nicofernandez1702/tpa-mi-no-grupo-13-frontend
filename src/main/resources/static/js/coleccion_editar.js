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
  const fuentes = Array.isArray(coleccion.fuentes) ? coleccion.fuentes : [coleccion.fuente];
  fuenteEstatica.checked = fuentes.includes("Estática");
  fuenteDinamica.checked = fuentes.includes("Dinámica");
  fuenteProxy.checked = fuentes.includes("Proxy");

  algoritmoSelect.value = coleccion.consenso || "mayoria";

  // Guardar cambios enviando PUT al backend
  form.addEventListener("submit", async (e) => {
    e.preventDefault();

    // Obtener fuentes seleccionadas
    const fuentesSeleccionadas = [];
    if (fuenteEstatica.checked) fuentesSeleccionadas.push("Estática");
    if (fuenteDinamica.checked) fuentesSeleccionadas.push("Dinámica");
    if (fuenteProxy.checked) fuentesSeleccionadas.push("Proxy");

    // Construir DTO para enviar
    const coleccionActualizada = {
      id: coleccion.id,
      titulo: tituloInput.value,
      descripcion: descripcionInput.value,
      fuentes: fuentesSeleccionadas,
      consenso: algoritmoSelect.value
    };

    try {
      const response = await fetch(`/colecciones/`, {
        method: "PUT",
        headers: {
          "Content-Type": "application/json"
        },
        body: JSON.stringify(coleccionActualizada)
      });

      if (!response.ok) throw new Error("Error al actualizar la colección");

      alert("Colección guardada correctamente");
      window.location.href = "colecciones_admin.html";

    } catch (err) {
      console.error(err);
      alert("No se pudo guardar la colección. Revisa la consola.");
    }
  });
});
