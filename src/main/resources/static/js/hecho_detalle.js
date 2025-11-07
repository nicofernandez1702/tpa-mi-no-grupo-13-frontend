// ====== MAPA ======
const coords = [hecho.latitud, hecho.longitud];
const map = L.map("mapa-detalle").setView(coords, 13);

L.tileLayer("https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png", {
    attribution: '&copy; <a href="https://www.openstreetmap.org/copyright">OpenStreetMap</a>'
}).addTo(map);

L.marker(coords).addTo(map).bindPopup(`<b>${hecho.titulo}</b>`);

// ====== SOLICITUD DE ELIMINACIÓN ======
const motivo = document.getElementById("motivo");
const contador = document.getElementById("contador");
const btnEnviar = document.getElementById("btnEnviar");
const formSolicitud = document.getElementById("formSolicitud");

// Estado inicial
btnEnviar.disabled = true;

// Actualizar contador en tiempo real
motivo.addEventListener("input", () => {
    const length = motivo.value.length;
    contador.textContent = `${length}/500 caracteres`;
    btnEnviar.disabled = length < 500;
});

// Validación visual de Bootstrap
formSolicitud.addEventListener("submit", (event) => {
    if (motivo.value.length < 500) {
        event.preventDefault();
        event.stopPropagation();
    }
    formSolicitud.classList.add("was-validated");
});
