  if (!hecho) {
    alert("Hecho no encontrado");
  } else {
    // --- Rellenar campos ---
    document.getElementById("titulo").value = hecho.titulo;
    document.getElementById("descripcion").value = hecho.descripcion;
    document.getElementById("categoria").value = hecho.categoria;
    document.getElementById("ubicacionTexto").value = ubicacion;
    document.getElementById("fuente").value = hecho.fuente;
    document.getElementById("lat").value = hecho.latitud;
    document.getElementById("lng").value = hecho.longitud;
//  document.getElementById("preview").src = hecho.imagen; TODO Agregar el path de la imagen al HechoDTO

    // --- Inicializar mapa ---
    const map = L.map('mapa-editar').setView([hecho.latitud, hecho.longitud], 13);
    L.tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', {
      attribution: '© OpenStreetMap'
    }).addTo(map);

    let marker = L.marker([hecho.latitud, hecho.longitud], { draggable: true }).addTo(map);

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