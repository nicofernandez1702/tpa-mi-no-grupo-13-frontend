document.addEventListener("DOMContentLoaded", () => {
    const gridColecciones = document.getElementById("grid-colecciones-destacadas");
    const gridHechos = document.getElementById("grid-hechos-destacados");

    // Mostrar las primeras 3 colecciones como destacadas
    const coleccionesDestacadas = colecciones.slice(0,3);
    coleccionesDestacadas.forEach(c => {
      gridColecciones.innerHTML += `
        <div class="col-md-4">
          <div class="card h-100">
            <div class="card-body">
              <h5 class="card-title">${c.titulo}</h5>
              <p class="card-text">${c.descripcion}</p>
              <a href="coleccion_detalle.html?id=${c.id}" class="btn btn-primary w-100">Ver colección</a>
            </div>
          </div>
        </div>
      `;
    });

    // Mostrar los primeros 3 hechos de todas las colecciones como destacados
    let todosLosHechos = colecciones.flatMap(c => c.hechos);
    const hechosDestacados = todosLosHechos.slice(0,3);
    hechosDestacados.forEach(h => {
      gridHechos.innerHTML += `
        <div class="col-md-4">
          <div class="card h-100">
            <div class="card-body">
              <h5 class="card-title">${h.titulo}</h5>
              <p class="card-text">${h.descripcion}</p>
              <a href="hecho_detalle.html?id=${h.id}" class="btn btn-primary w-100">Ver detalle</a>
            </div>
          </div>
        </div>
      `;
    });
  });