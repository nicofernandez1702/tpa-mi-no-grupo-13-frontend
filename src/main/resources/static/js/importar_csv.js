document.addEventListener("DOMContentLoaded", () => {
    const form = document.getElementById("formImportar");
    const mensajeModal = document.getElementById("mensajeModal");

    form.addEventListener("submit", async (e) => {
        e.preventDefault();

        const formData = new FormData(form);

        try {
            const response = await fetch(form.action, {
                method: form.method,
                body: formData
            });

            const text = await response.text();
            mensajeModal.innerText = text;

            // Mostrar el modal usando Bootstrap
            const modal = new bootstrap.Modal(document.getElementById('modalCarga'));
            modal.show();

        } catch (error) {
            mensajeModal.innerText = "Ocurrió un error al subir el archivo.";
            const modal = new bootstrap.Modal(document.getElementById('modalCarga'));
            modal.show();
        }
    });
});