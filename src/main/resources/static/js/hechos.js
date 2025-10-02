// hechos.js

document.addEventListener("DOMContentLoaded", () => {
  // Inicializar el mapa
  const mapa = L.map("mapa-hechos").setView([-34.6037, -58.3816], 5); // Centro aprox. Argentina

  // Cargar tiles de OpenStreetMap
  L.tileLayer("https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png", {
    attribution: '&copy; <a href="https://www.openstreetmap.org/">OpenStreetMap</a> contributors'
  }).addTo(mapa);

  // Extraer todos los hechos de todas las colecciones
  const hechos = colecciones.flatMap(c => 
    c.hechos.map(h => ({
      id: h.id,
      titulo: h.titulo,
      descripcion: h.descripcion,
      lat: h.lat,
      lng: h.lng
    }))
  );

  // Agregar marcadores al mapa
  hechos.forEach(hecho => {
    const marcador = L.marker([hecho.lat, hecho.lng]).addTo(mapa);

    // Popup con botón de detalle
    const popupContent = `
      <div class="text-center">
        <h6>${hecho.titulo}</h6>
        <p>${hecho.descripcion}</p>
        <a href="hecho_detalle.html?id=${hecho.id}" class="btn btn-sm btn-primary w-100 text-white">
          Ver detalle
        </a>
      </div>
    `;

    marcador.bindPopup(popupContent);
  });
});
