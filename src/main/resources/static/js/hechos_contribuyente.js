// Recolectar todos los hechos del contribuyente
let hechos = [];
colecciones.forEach(c => {
  hechos.push(...c.hechos);
});

let idHechoAEliminar = null; // variable global temporal

// Rellenar tabla desktop
const tablaBody = document.querySelector('#tabla-hechos tbody');
hechos.forEach(h => {
  const row = document.createElement('tr');
  row.innerHTML = `
    <td>${h.titulo}</td>
    <td><span class="badge bg-info text-dark">${h.categoria}</span></td>
    <td>${h.fecha}</td>
    <td>${h.lugar}</td>
    <td class="text-center">
      <a href="hecho_editar.html?id=${h.id}" class="btn btn-sm btn-primary me-2">
        <i class="bi bi-pencil-square"></i>
      </a>
      <button class="btn btn-sm btn-danger btn-eliminar" data-id="${h.id}">
        <i class="bi bi-trash"></i>
      </button>
    </td>
  `;
  tablaBody.appendChild(row);
});

// Rellenar cards mobile
const cardsContainer = document.getElementById('cards-hechos');
hechos.forEach(h => {
  const card = document.createElement('div');
  card.className = 'card mb-3';
  card.innerHTML = `
    <div class="card-body">
      <h5 class="card-title">${h.titulo}</h5>
      <p><span class="badge bg-info text-dark">${h.categoria}</span></p>
      <p class="mb-1"><strong>Fecha:</strong> ${h.fecha}</p>
      <p class="mb-2"><strong>Ubicación:</strong> ${h.lugar}</p>
      <div>
        <a href="hecho_editar.html?id=${h.id}" class="btn btn-sm btn-primary me-2">
          <i class="bi bi-pencil-square"></i>
        </a>
        <button class="btn btn-sm btn-danger btn-eliminar" data-id="${h.id}">
          <i class="bi bi-trash"></i>
        </button>
      </div>
    </div>
  `;
  cardsContainer.appendChild(card);
});

// Inicializar modal de Bootstrap
const modalEliminar = new bootstrap.Modal(document.getElementById('confirmarEliminarModal'));

document.addEventListener("click", (e) => {
  if (e.target.closest(".btn-eliminar")) {
    const id = e.target.closest(".btn-eliminar").dataset.id;
    idHechoAEliminar = id; // guardar id en variable global
    modalEliminar.show(); // abrir modal
  }
});

document.getElementById("btnConfirmarEliminar").addEventListener("click", () => {
  if (idHechoAEliminar) {
    console.log("Eliminar hecho con id:", idHechoAEliminar);

    // 🔥 acá llamás a tu backend o borrás el hecho de la lista
    // ej: fetch(`/api/hechos/${idHechoAEliminar}`, { method: "DELETE" })

    modalEliminar.hide();
    idHechoAEliminar = null;
  }
});
