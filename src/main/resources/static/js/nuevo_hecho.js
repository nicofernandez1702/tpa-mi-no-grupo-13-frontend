  document.addEventListener("DOMContentLoaded", function () {
    // Inicializar el mapa
    const mapa = L.map("mapa-ubicacion").setView([-36.6641, -64.1754], 4); // Córdoba por defecto, podés cambiarlo

    // Capa base de OpenStreetMap
    L.tileLayer("https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png", {
      attribution: "&copy; <a href='https://www.openstreetmap.org/'>OpenStreetMap</a> contributors"
    }).addTo(mapa);

    // Variables para el marcador
    let marker;

    // Evento clic en el mapa
    mapa.on("click", function (e) {
      const lat = e.latlng.lat;
      const lng = e.latlng.lng;

      // Guardar en los inputs ocultos
      document.getElementById("latitud").value = lat;
      document.getElementById("longitud").value = lng;

      // Si ya hay un marcador, moverlo; si no, crearlo
      if (marker) {
        marker.setLatLng([lat, lng]);
      } else {
        marker = L.marker([lat, lng]).addTo(mapa);
      }
    });
  });

