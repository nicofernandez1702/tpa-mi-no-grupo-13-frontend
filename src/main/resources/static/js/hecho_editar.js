 // --- Obtener id de la URL ---
  const params = new URLSearchParams(window.location.search);
  const hechoId = parseInt(params.get("id"));

  // --- Buscar hecho en colecciones ---
  let hecho = null;
  colecciones.forEach(c => {
    c.hechos.forEach(h => {
      if (h.id === hechoId) hecho = h;
    });
  });

  if (!hecho) {
    alert("Hecho no encontrado");
  } else {
    // --- Rellenar campos ---
    document.getElementById("titulo").value = hecho.titulo;
    document.getElementById("descripcion").value = hecho.descripcion;
    document.getElementById("categoria").value = hecho.categoria;
    document.getElementById("ubicacionTexto").value = hecho.lugar;
    document.getElementById("fuente").value = hecho.fuente;
    document.getElementById("lat").value = hecho.lat;
    document.getElementById("lng").value = hecho.lng;
    document.getElementById("preview").src = hecho.imagen;

    // --- Inicializar mapa ---
    const map = L.map('mapa-editar').setView([hecho.lat, hecho.lng], 13);
    L.tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', {
      attribution: '© OpenStreetMap'
    }).addTo(map);

    let marker = L.marker([hecho.lat, hecho.lng], { draggable: true }).addTo(map);

    function updateCoords(lat, lng) {
      document.getElementById('lat').value = lat;
      document.getElementById('lng').value = lng;
    }

    marker.on('dragend', function(e) {
      const coords = marker.getLatLng();
      updateCoords(coords.lat, coords.lng);
    });

    map.on('click', function(e) {
      marker.setLatLng(e.latlng);
      updateCoords(e.latlng.lat, e.latlng.lng);
    });
  }

  // --- Vista previa imagen nueva ---
  document.getElementById('imagen').addEventListener('change', function(event) {
    const reader = new FileReader();
    reader.onload = function(e) {
      document.getElementById('preview').src = e.target.result;
    };
    reader.readAsDataURL(event.target.files[0]);
  });