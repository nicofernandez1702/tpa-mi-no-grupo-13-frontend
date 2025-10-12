// hecho_detalle.js

// 4. Inicializar Leaflet en la ubicación del hecho
// Se requiere que los hechos tengan lat/lng en los datos de prueba
const coords = [ hecho.latitud, hecho.longitud ]; // fallback
const map = L.map("mapa-detalle").setView(coords, 13);

L.tileLayer("https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png", {
    attribution: '&copy; <a href="https://www.openstreetmap.org/copyright">OpenStreetMap</a>'
}).addTo(map);

L.marker(coords)
    .addTo(map)
    .bindPopup(`<b>${hecho.titulo}</b><br>${""}`);



// Solicitud Eliminación

const justificacion = document.getElementById("justificacion");
const contador = document.getElementById("contador");
const btnEnviar = document.getElementById("btnEnviar");

justificacion.addEventListener("input", () => {
    const length = justificacion.value.length;
    contador.textContent = `${length}/500 caracteres`;
    btnEnviar.disabled = length < 500;
});