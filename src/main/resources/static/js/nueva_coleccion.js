document.addEventListener("DOMContentLoaded", () => {
    const form = document.getElementById("form-nueva-coleccion");

    form.addEventListener("submit", (event) => {
        const titulo = document.getElementById("titulo").value.trim();
        const descripcion = document.getElementById("descripcion").value.trim();

        if (!titulo || !descripcion) {
            event.preventDefault(); // Cancela el envío
            alert("Por favor, completa todos los campos antes de continuar.");
        }
    });
});