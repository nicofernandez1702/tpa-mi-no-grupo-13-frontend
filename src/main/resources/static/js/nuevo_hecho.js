document.addEventListener("DOMContentLoaded", function () {
    // Inicializar el mapa
    const mapa = L.map("mapa-ubicacion").setView([-36.6641, -64.1754], 4);

    L.tileLayer("https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png", {
        attribution: "&copy; OpenStreetMap contributors"
    }).addTo(mapa);

    let marker;

    // Evento click en el mapa
    mapa.on("click", function (e) {
        const lat = e.latlng.lat;
        const lng = e.latlng.lng;

        document.getElementById("latitud").value = lat;
        document.getElementById("longitud").value = lng;

        if (marker) {
            marker.setLatLng([lat, lng]);
        } else {
            marker = L.marker([lat, lng]).addTo(mapa);
        }
    });

    // Manejar envío del formulario
    const form = document.getElementById("form-nuevo-hecho");
    form.addEventListener("submit", async function (event) {
        event.preventDefault();

        const formData = new FormData(form);

        try {
            const response = await fetch("/hechos/nuevo", {
                method: "POST",
                body: formData
            });

            if (response.ok) {
                alert("✅ Hecho creado correctamente");
                form.reset();
            } else {
                const text = await response.text();
                alert("❌ Error al crear hecho: " + text);
            }
        } catch (error) {
            alert("⚠️ Error de conexión con el servidor: " + error.message);
        }
    });
});


