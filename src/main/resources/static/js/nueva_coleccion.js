document.addEventListener("DOMContentLoaded", function () {
    console.log("Inicializando select múltiple de fuentes...");

    if (typeof $ !== 'undefined' && $.fn.selectpicker) {
        $('#fuentes').selectpicker('refresh');
        console.log("Selectpicker inicializado correctamente.");
    } else {
        console.warn("Bootstrap Select no detectado o jQuery no cargado.");
    }
});