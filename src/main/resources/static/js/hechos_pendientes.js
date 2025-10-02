document.addEventListener("DOMContentLoaded", () => {
  const listaHechos = document.getElementById("listaHechos");

  const hechosPendientes = colecciones.flatMap(c =>
    c.hechos.map(h => ({ ...h, coleccion: c.titulo }))
  );

  const modalDetalles = new bootstrap.Modal(document.getElementById("modalDetalles"));
  const modalAccion = new bootstrap.Modal(document.getElementById("modalAccion"));

  const modalTitulo = document.getElementById("modalTitulo");
  const modalCategoria = document.getElementById("modalCategoria");
  const modalUbicacion = document.getElementById("modalUbicacion");
  const modalFecha = document.getElementById("modalFecha");
  const modalDescripcion = document.getElementById("modalDescripcion");
  const modalFuente = document.getElementById("modalFuente");

  const modalAccionHeader = document.getElementById("modalAccionHeader");
  const modalAccionTitulo = document.getElementById("modalAccionTitulo");
  const modalAccionBody = document.getElementById("modalAccionBody");
  const modalAccionConfirmar = document.getElementById("modalAccionConfirmar");

  // Variables para saber qué hecho y acción se está usando
  let hechoActual = null;
  let accionActual = null;

  // Listener único del botón Confirmar
  modalAccionConfirmar.addEventListener("click", () => {
    if (hechoActual && accionActual) {
      console.log(`${accionActual}: ${hechoActual.titulo}`);
      modalAccion.hide();
      hechoActual = null;
      accionActual = null;
    }
  });

  function renderHechos() {
    listaHechos.innerHTML = "";

    hechosPendientes.forEach(hecho => {
      const li = document.createElement("li");
      li.className = "list-group-item d-flex flex-column flex-md-row justify-content-between align-items-start align-items-md-center";

      li.innerHTML = `
        <div class="mb-2 mb-md-0">
          <h5 class="mb-1">${hecho.titulo}</h5>
          <small class="text-muted">
            Categoría: ${hecho.categoria} | Ubicación: ${hecho.lugar} | Fecha: ${hecho.fecha}
          </small>
        </div>
        <div class="row g-2 justify-content-md-end w-100">
          <div class="col-6 col-md-auto">
            <button class="btn btn-primary w-100 btn-aprobar" title="Aprobar">
              <i class="bi bi-check-circle fs-5 mx-2"></i>
            </button>
          </div>
          <div class="col-6 col-md-auto">
            <button class="btn btn-info w-100 btn-modificar" title="Aceptar con modificaciones">
              <i class="bi bi-pencil-square fs-5 mx-2"></i>
            </button>
          </div>
          <div class="col-6 col-md-auto">
            <button class="btn btn-danger w-100 btn-rechazar" title="Rechazar">
              <i class="bi bi-x-circle fs-5 mx-2"></i>
            </button>
          </div>
          <div class="col-6 col-md-auto">
            <button class="btn btn-secondary w-100 btn-detalles" title="Ver Detalles">
              <i class="bi bi-eye fs-5 mx-2"></i>
            </button>
          </div>
        </div>

      `;

      // Detalles
      li.querySelector(".btn-detalles").addEventListener("click", () => {
        modalTitulo.textContent = hecho.titulo;
        modalCategoria.textContent = hecho.categoria;
        modalUbicacion.textContent = hecho.lugar;
        modalFecha.textContent = hecho.fecha;
        modalDescripcion.textContent = hecho.descripcion;
        modalFuente.textContent = hecho.fuente;
        modalDetalles.show();
      });

      // Aprobar
      li.querySelector(".btn-aprobar").addEventListener("click", () => {
        hechoActual = hecho;
        accionActual = "Aprobado";
        modalAccionHeader.className = "modal-header bg-primary text-white";
        modalAccionTitulo.textContent = "Aprobar Hecho";
        modalAccionBody.textContent = `¿Deseas aprobar el hecho "${hecho.titulo}"?`;
        modalAccion.show();
      });

      // Modificar
      li.querySelector(".btn-modificar").addEventListener("click", () => {
        hechoActual = hecho;
        accionActual = "Aceptado con modificaciones";
        modalAccionHeader.className = "modal-header bg-info text-white";
        modalAccionTitulo.textContent = "Aceptar con Modificaciones";
        modalAccionBody.textContent = `¿Deseas aceptar con modificaciones el hecho "${hecho.titulo}"?`;
        modalAccion.show();
      });

      // Rechazar
      li.querySelector(".btn-rechazar").addEventListener("click", () => {
        hechoActual = hecho;
        accionActual = "Rechazado";
        modalAccionHeader.className = "modal-header bg-danger text-white";
        modalAccionTitulo.textContent = "Rechazar Hecho";
        modalAccionBody.textContent = `¿Deseas rechazar el hecho "${hecho.titulo}"?`;
        modalAccion.show();
      });

      listaHechos.appendChild(li);
    });
  }

  renderHechos();
});
