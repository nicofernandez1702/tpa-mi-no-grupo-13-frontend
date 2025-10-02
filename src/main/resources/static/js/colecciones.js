// colecciones.js

document.addEventListener('DOMContentLoaded', () => {
  const grid = document.getElementById('grid-colecciones');

  // Renderizar colecciones
  function renderColecciones(lista) {
    grid.innerHTML = "";
    lista.forEach(c => {
      grid.innerHTML += `
        <div class="col">
          <div class="card h-100" data-cantidad="${c.hechos.length}" data-fecha="${c.fecha}">
            <div class="card-body d-flex flex-column">
              <h5 class="card-title">${c.titulo}</h5>
              <p class="card-text flex-grow-1">${c.descripcion}</p>
              <a href="coleccion_detalle.html?id=${c.id}" class="btn btn-primary mt-auto">Ver colección</a>
            </div>
          </div>
        </div>
      `;
    });
  }

  renderColecciones(colecciones);

  // ========================
  // BÚSQUEDA Y ORDEN
  // ========================
  const inputBusqueda = document.getElementById('busqueda-colecciones');
  const botonesOrden = document.querySelectorAll('[data-orden]');

  let ordenActual = { alfabetico: 'asc' };
  let ultimoCriterio = 'alfabetico';

  function actualizarBotones(criterio) {
    botonesOrden.forEach(b => {
      const icono = b.querySelector('i');
      icono.classList.remove('bi-arrow-down');
      icono.classList.add('bi-arrow-up');
      b.classList.remove('btn-primary', 'text-light');
    });

    const botonActivo = document.querySelector(`[data-orden="${criterio}"]`);
    botonActivo.classList.add('btn-primary', 'text-light');
    if(ordenActual[criterio] === 'desc'){
      botonActivo.querySelector('i').classList.replace('bi-arrow-up', 'bi-arrow-down');
    }
  }

  function ordenar(criterio) {
    let cards = Array.from(grid.querySelectorAll('.col')).filter(c => c.style.display !== 'none');

    cards.sort((a, b) => {
      let valA, valB;
      switch(criterio){
        case 'alfabetico':
          valA = a.querySelector('.card-title').textContent.toLowerCase();
          valB = b.querySelector('.card-title').textContent.toLowerCase();
          break;
        case 'cantidad':
          valA = parseInt(a.querySelector('.card').dataset.cantidad);
          valB = parseInt(b.querySelector('.card').dataset.cantidad);
          break;
        case 'fecha':
          valA = new Date(a.querySelector('.card').dataset.fecha);
          valB = new Date(b.querySelector('.card').dataset.fecha);
          break;
      }
      if(valA < valB) return ordenActual[criterio] === 'asc' ? -1 : 1;
      if(valA > valB) return ordenActual[criterio] === 'asc' ? 1 : -1;
      return 0;
    });

    cards.forEach(c => grid.appendChild(c));
    actualizarBotones(criterio);
  }

  // Filtrado en tiempo real
  inputBusqueda.addEventListener('input', () => {
    const texto = inputBusqueda.value.toLowerCase();
    Array.from(grid.querySelectorAll('.col')).forEach(col => {
      const titulo = col.querySelector('.card-title').textContent.toLowerCase();
      col.style.display = titulo.includes(texto) ? '' : 'none';
    });
  });

  // Botones de orden
  botonesOrden.forEach(boton => {
    boton.addEventListener('click', () => {
      const criterio = boton.dataset.orden;
      if(ultimoCriterio !== criterio){
        ordenActual[criterio] = 'asc';
        ultimoCriterio = criterio;
      } else {
        ordenActual[criterio] = ordenActual[criterio] === 'asc' ? 'desc' : 'asc';
      }
      ordenar(criterio);
    });
  });

  ordenar('alfabetico'); // orden inicial
});
