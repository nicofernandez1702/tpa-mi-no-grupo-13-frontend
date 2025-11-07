// colecciones_admin.js (versión para server-rendered DOM)

document.addEventListener('DOMContentLoaded', () => {
    const grid = document.getElementById('grid-colecciones');
    const inputBusqueda = document.getElementById('busqueda-colecciones');
    const botonesOrden = document.querySelectorAll('[data-orden]');
    const btnConfirmarEliminar = document.getElementById('btnConfirmarEliminar');

    // estado de orden
    let ordenActual = { alfabetico: 'asc', cantidad: 'asc', fecha: 'asc' };
    let ultimoCriterio = 'alfabetico';

    function actualizarBotones(criterio) {
        botonesOrden.forEach(b => {
            const icono = b.querySelector('i');
            if (icono) {
                icono.classList.remove('bi-arrow-down', 'bi-arrow-up');
                icono.classList.add('bi-arrow-up');
            }
            b.classList.remove('btn-primary', 'text-light');
        });

        const botonActivo = document.querySelector(`[data-orden="${criterio}"]`);
        if (!botonActivo) return;
        botonActivo.classList.add('btn-primary', 'text-light');

        if (ordenActual[criterio] === 'desc' && botonActivo.querySelector('i')) {
            botonActivo.querySelector('i').classList.replace('bi-arrow-up', 'bi-arrow-down');
        }
    }

    function ordenar(criterio) {
        // tomamos las columnas visibles
        let cardsCols = Array.from(grid.querySelectorAll('.col')).filter(c => c.style.display !== 'none');

        cardsCols.sort((colA, colB) => {
            const cardA = colA.querySelector('.card');
            const cardB = colB.querySelector('.card');

            let valA, valB;

            switch (criterio) {
                case 'alfabetico':
                    valA = (colA.querySelector('.card-title')?.textContent || '').toLowerCase();
                    valB = (colB.querySelector('.card-title')?.textContent || '').toLowerCase();
                    if (valA < valB) return ordenActual[criterio] === 'asc' ? -1 : 1;
                    if (valA > valB) return ordenActual[criterio] === 'asc' ? 1 : -1;
                    return 0;

                case 'cantidad':
                    valA = parseInt(cardA?.dataset.cantidad || '0', 10);
                    valB = parseInt(cardB?.dataset.cantidad || '0', 10);
                    if (valA < valB) return ordenActual[criterio] === 'asc' ? -1 : 1;
                    if (valA > valB) return ordenActual[criterio] === 'asc' ? 1 : -1;
                    return 0;

                case 'fecha':
                    // dataset fecha en formato yyyy-mm-dd o cadena vacía
                    valA = cardA?.dataset.fecha ? new Date(cardA.dataset.fecha) : new Date(0);
                    valB = cardB?.dataset.fecha ? new Date(cardB.dataset.fecha) : new Date(0);
                    if (valA < valB) return ordenActual[criterio] === 'asc' ? -1 : 1;
                    if (valA > valB) return ordenActual[criterio] === 'asc' ? 1 : -1;
                    return 0;

                default:
                    return 0;
            }
        });

        // Reordenar en el DOM
        cardsCols.forEach(c => grid.appendChild(c));
        actualizarBotones(criterio);
    }

    // Filtrado en tiempo real
    inputBusqueda?.addEventListener('input', () => {
        const texto = (inputBusqueda.value || '').toLowerCase();
        Array.from(grid.querySelectorAll('.col')).forEach(col => {
            const titulo = (col.querySelector('.card-title')?.textContent || '').toLowerCase();
            col.style.display = titulo.includes(texto) ? '' : 'none';
        });
    });

    // Botones de orden
    botonesOrden.forEach(boton => {
        boton.addEventListener('click', () => {
            const criterio = boton.dataset.orden;
            if (!criterio) return;

            if (ultimoCriterio !== criterio) {
                // si cambio de criterio, forzamos asc
                ordenActual[criterio] = 'asc';
                ultimoCriterio = criterio;
            } else {
                ordenActual[criterio] = ordenActual[criterio] === 'asc' ? 'desc' : 'asc';
            }
            ordenar(criterio);
        });
    });

    // Inicial: ordenar alfabeticamente
    ordenar('alfabetico');

    // ========================
    // CONFIRMACIÓN ELIMINAR
    // ========================
    // reasignar eventos a botones ya renderizados
    document.querySelectorAll('.btn-eliminar').forEach(boton => {
        boton.addEventListener('click', () => {
            const id = boton.dataset.id;
            const titulo = boton.dataset.titulo || '';
            const modalTitulo = document.getElementById('modalEliminarTitulo');
            const modalConfirmar = document.getElementById('btnConfirmarEliminar');

            if (modalTitulo) modalTitulo.textContent = titulo;
            if (modalConfirmar) modalConfirmar.dataset.id = id;

            const modal = new bootstrap.Modal(document.getElementById('modalEliminar'));
            modal.show();
        });
    });

    // click confirmar eliminar
    btnConfirmarEliminar?.addEventListener('click', async () => {
        const id = btnConfirmarEliminar.dataset.id;
        if (!id) return;

        // ejemplo: llamar a endpoint DELETE (adaptá la URL)
        try {
            // await fetch(`/colecciones/${id}`, { method: 'DELETE' });
            // por ahora solo cierre y eliminación visual (si querés, descomentar arriba y adaptar)
            const modalInstance = bootstrap.Modal.getInstance(document.getElementById('modalEliminar'));
            modalInstance?.hide();

            // eliminar del DOM la tarjeta con ese id
            const boton = document.querySelector(`.btn-eliminar[data-id="${id}"]`);
            if (boton) {
                const col = boton.closest('.col');
                col?.remove();
            }
        } catch (err) {
            console.error('Error eliminando:', err);
            // mostrar mensaje de error si querés
        }
    });

});
