document.addEventListener("DOMContentLoaded", () => {
    const form = document.getElementById("formImportar");
    const mensajeModal = document.getElementById("mensajeModal");
    const overlay = document.getElementById("overlayCarga");
    const boton = form.querySelector("button[type='submit']");

    form.addEventListener("submit", async (e) => {
        e.preventDefault();

        const formData = new FormData(form);

        // Mostrar loading
        overlay.style.display = "flex";
        boton.disabled = true;

        try {
            const response = await fetch(form.action, {
                method: form.method,
                body: formData
            });

            const text = await response.text();
            mensajeModal.innerText = text;

        } catch (error) {
            mensajeModal.innerText = "Ocurrió un error al subir el archivo.";
        } finally {
            // Ocultar loading
            overlay.style.display = "none";
            boton.disabled = false;

            const modal = new bootstrap.Modal(
                document.getElementById("modalCarga")
            );
            modal.show();
        }
    });
});
