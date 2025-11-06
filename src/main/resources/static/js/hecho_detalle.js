btnEnviar.addEventListener("click", async () => {
    const motivo = justificacion.value.trim();

    const solicitud = {
        hecho: hecho.id,   // el id del hecho actual
        motivo: motivo
    };

    try {
        const response = await fetch("/solicitudes", {
            method: "POST",
            headers: {
                "Content-Type": "application/json"
            },
            body: JSON.stringify(solicitud)
        });

        if (response.ok) {
            const data = await response.json();
            alert("Solicitud enviada con éxito ✅");
            const modal = bootstrap.Modal.getInstance(document.getElementById("modalEliminar"));
            modal.hide();
            justificacion.value = "";
            contador.textContent = "0/500 caracteres";
            btnEnviar.disabled = true;
        } else {
            const err = await response.text();
            alert("❌ Error al enviar la solicitud: " + err);
        }
    } catch (e) {
        console.error("Error en la solicitud:", e);
        alert("❌ Error inesperado al enviar la solicitud");
    }
});