
// Minimal client-side utilities (mock navigation + localStorage demo)
const $ = (sel, el=document) => el.querySelector(sel);
const $$ = (sel, el=document) => [...el.querySelectorAll(sel)];

// Fake login/register flow
function handleAuth(formId, storageKey) {
  const form = document.getElementById(formId);
  if (!form) return;
  form.addEventListener('submit', (e)=>{
    e.preventDefault();
    const data = Object.fromEntries(new FormData(form).entries());
    localStorage.setItem(storageKey, JSON.stringify(data));
    alert('Guardado ✔');
    window.location.href = 'panel_control.html';
  });
}

// Fake create collection
function handleCreateCollection() {
  const form = $('#formCrearColeccion');
  if(!form) return;
  form.addEventListener('submit', (e)=>{
    e.preventDefault();
    const data = Object.fromEntries(new FormData(form).entries());
    const list = JSON.parse(localStorage.getItem('colecciones')||'[]');
    list.push({ id: Date.now(), ...data });
    localStorage.setItem('colecciones', JSON.stringify(list));
    alert('Colección creada ✔');
    window.location.href = 'menu_Colecciones.html'; // ya existente
  });
}

// Fake subir hecho
function handleSubirHecho() {
  const form = $('#formSubirHecho');
  if(!form) return;
  form.addEventListener('submit',(e)=>{
    e.preventDefault();
    const data = Object.fromEntries(new FormData(form).entries());
    const hechos = JSON.parse(localStorage.getItem('hechos')||'[]');
    hechos.push({ id: Date.now(), ...data });
    localStorage.setItem('hechos', JSON.stringify(hechos));
    alert('Hecho cargado ✔');
    window.location.href = 'hecho.html?id=' + hechos[hechos.length-1].id;
  });
}

// Render hecho by id
function renderHecho() {
  const params = new URLSearchParams(location.search);
  const id = params.get('id');
  const hechos = JSON.parse(localStorage.getItem('hechos')||'[]');
  const h = hechos.find(x=> String(x.id)===String(id)) || hechos[0];
  if(!h) { const c = $('.empty'); if(c) c.textContent='No hay hechos aún.'; return; }
  $("[data-field='titulo']").textContent = h.titulo || '-';
  $("[data-field='ubicacion']").textContent = h.ubicacion || '-';
  $("[data-field='categoria']").textContent = h.categoria || '-';
  $("[data-field='fecha']").textContent = h.fecha || '-';
  $("[data-field='fuente']").textContent = h.fuente || '-';
  $("[data-field='descripcion']").textContent = h.descripcion || '-';
}

// Admin solicitudes expand/collapse
function initSolicitudes() {
  $$('.solicitud .toggle').forEach(btn=>{
    btn.addEventListener('click', ()=>{
      btn.closest('.solicitud').classList.toggle('open');
    });
  });
}

// Shared init
document.addEventListener('DOMContentLoaded', ()=>{
  handleAuth('formLogin','usuario');
  handleAuth('formRegister','usuario');
  handleCreateCollection();
  handleSubirHecho();
  renderHecho();
  initSolicitudes();
});



// Common MetaMapa client helpers
(function(){
  function getParam(key){ return new URLSearchParams(location.search).get(key); }
  const logged = (getParam('logged')==='1') || window.__forceAdmin;
  const admin = (getParam('admin')==='1') || window.__forceAdmin;
  const elLogin = document.getElementById('nav-login');
  const elReg = document.getElementById('nav-register');
  const elProf = document.getElementById('nav-profile');
  const elPanel = document.getElementById('dd-panel');
  if(elLogin && elReg && elProf){
    if(logged){ elLogin.hidden = true; elReg.hidden = true; elProf.hidden = false; }
    else { elLogin.hidden = false; elReg.hidden = false; elProf.hidden = true; }
  }
  if(elPanel){ elPanel.style.display = admin ? 'block' : 'none'; }
  const del = document.getElementById('btn-delete');
  if(del){ del.hidden = !admin; }
})();
