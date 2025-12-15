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

    const modal = new bootstrap.Modal(
        document.getElementById("modalResultado")
    );

    const modalTitulo = document.getElementById("modalResultadoTitulo");
    const modalMensaje = document.getElementById("modalResultadoMensaje");

    form.addEventListener("submit", async function (event) {
        event.preventDefault();

        const formData = new FormData(form);

        try {
            const response = await fetch("/hechos/nuevo", {
                method: "POST",
                body: formData
            });

            if (response.ok) {
                modalTitulo.textContent = "✅ Hecho creado";
                modalMensaje.innerHTML = `
                    <p>El hecho fue publicado correctamente.</p>
                `;
                modal.show();
                form.reset();

            } else {
                const text = await response.text();
                modalTitulo.textContent = "❌ Error al crear el hecho";
                modalMensaje.innerHTML = `
                    <p>${text}</p>
                `;
                modal.show();
            }

        } catch (error) {
            modalTitulo.textContent = "⚠️ Error de conexión";
            modalMensaje.innerHTML = `
                <p>No se pudo conectar con el servidor.</p>
                <small class="text-muted">${error.message}</small>
            `;
            modal.show();
        }
    });
    document
        .getElementById("modalResultado")
        .addEventListener("hidden.bs.modal", () => {
            window.location.href = "/hechos";
        });

});


