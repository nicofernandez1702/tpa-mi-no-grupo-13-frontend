// colecciones.js

document.addEventListener('DOMContentLoaded', () => {
    const grid = document.getElementById('grid-colecciones');
    const inputBusqueda = document.getElementById('busqueda-colecciones');
    const botonesOrden = document.querySelectorAll('[data-orden]');

    let ordenActual = { alfabetico: 'asc' };
    let ultimoCriterio = 'alfabetico';

    // ========================
    // ORDENAMIENTO
    // ========================
    function actualizarBotones(criterio) {
        botonesOrden.forEach(b => {
            const icono = b.querySelector('i');
            icono.classList.remove('bi-arrow-down');
            icono.classList.add('bi-arrow-up');
            b.classList.remove('btn-primary', 'text-light');
        });

        const botonActivo = document.querySelector(`[data-orden="${criterio}"]`);
        botonActivo.classList.add('btn-primary', 'text-light');
        if (ordenActual[criterio] === 'desc') {
            botonActivo.querySelector('i').classList.replace('bi-arrow-up', 'bi-arrow-down');
        }
    }

    function ordenar(criterio) {
        let cards = Array.from(grid.querySelectorAll('.col')).filter(c => c.style.display !== 'none');

        cards.sort((a, b) => {
            let valA, valB;
            switch (criterio) {
                case 'alfabetico':
                    valA = a.querySelector('.card-title').textContent.toLowerCase();
                    valB = b.querySelector('.card-title').textContent.toLowerCase();
                    break;
                case 'cantidad':
                    valA = parseInt(a.querySelector('.card').dataset.cantidad);
                    valB = parseInt(b.querySelector('.card').dataset.cantidad);
                    break;
            }
            if (valA < valB) return ordenActual[criterio] === 'asc' ? -1 : 1;
            if (valA > valB) return ordenActual[criterio] === 'asc' ? 1 : -1;
            return 0;
        });

        cards.forEach(c => grid.appendChild(c));
        actualizarBotones(criterio);
    }

    // ========================
    // BÚSQUEDA EN TIEMPO REAL
    // ========================
    inputBusqueda.addEventListener('input', () => {
        const texto = inputBusqueda.value.toLowerCase();
        Array.from(grid.querySelectorAll('.col')).forEach(col => {
            const titulo = col.querySelector('.card-title').textContent.toLowerCase();
            col.style.display = titulo.includes(texto) ? '' : 'none';
        });
    });

    // ========================
    // BOTONES DE ORDEN
    // ========================
    botonesOrden.forEach(boton => {
        boton.addEventListener('click', () => {
            const criterio = boton.dataset.orden;
            if (ultimoCriterio !== criterio) {
                ordenActual[criterio] = 'asc';
                ultimoCriterio = criterio;
            } else {
                ordenActual[criterio] = ordenActual[criterio] === 'asc' ? 'desc' : 'asc';
            }
            ordenar(criterio);
        });
    });

    ordenar('alfabetico'); // orden inicial
});
