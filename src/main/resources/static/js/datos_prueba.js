// datos_prueba.js

const colecciones = [
  {
    id: 1,
    titulo: "Desastres Naturales",
    descripcion: "Eventos naturales que causaron gran impacto en diferentes regiones.",
    fuente: "Protección Civil",
    hechos: [
      {
        id: 101,
        titulo: "Terremoto en San Juan",
        fecha: "1944-01-15",
        lugar: "San Juan, Argentina",
        descripcion: "Terremoto que destruye gran parte de la ciudad.",
        fuente: "Archivo Sísmico",
        lat: -31.5375,
        lng: -68.5364,
        categoria: "terremoto",
        imagen: "./assets/img/terremoto.jpg"
      },
      {
        id: 102,
        titulo: "Inundación del Río Paraná",
        fecha: "1983-03-20",
        lugar: "Santa Fe, Argentina",
        descripcion: "Desborde del río que afectó varias localidades.",
        fuente: "Diario El Litoral",
        lat: -31.6333,
        lng: -60.7,
        categoria: "inundacion",
        imagen: "./assets/img/inundacion.webp"
      },
      {
        id: 103,
        titulo: "Tormenta de granizo en Mendoza",
        fecha: "2005-10-12",
        lugar: "Mendoza, Argentina",
        descripcion: "Granizo intenso daña cultivos y vehículos.",
        fuente: "Agencia Meteorológica",
        lat: -32.8908,
        lng: -68.8272,
        categoria: "granizo",
        imagen: "./assets/img/granizo.jpg"
      }
    ]
  },
  {
    id: 2,
    titulo: "Incidentes y Accidentes",
    descripcion: "Fuegos e incidentes que afectaron áreas urbanas y rurales.",
    fuente: "Bomberos Nacionales",
    hechos: [
      {
        id: 201,
        titulo: "Incendio forestal en Córdoba",
        fecha: "2025-03-10",
        lugar: "Córdoba, Argentina",
        descripcion: "Un incendio de gran magnitud afectó 200 hectáreas en las sierras.",
        fuente: "Agencia Noticias",
        lat: -31.4201,
        lng: -64.1888,
        categoria: "incendio",
        imagen: "./assets/img/incendio.webp"
      },
      {
        id: 202,
        titulo: "Derrumbe en La Rioja",
        fecha: "2010-06-05",
        lugar: "La Rioja, Argentina",
        descripcion: "Derrumbe en zona montañosa provoca evacuaciones.",
        fuente: "Diario Local",
        lat: -29.4136,
        lng: -66.8558,
        categoria: "derrumbe",
        imagen: "./assets/img/derrumbe.webp"
      }
    ]
  },
  {
    id: 3,
    titulo: "Eventos Sociales",
    descripcion: "Manifestaciones y movimientos que marcaron épocas.",
    fuente: "Archivo Nacional",
    hechos: [
      {
        id: 301,
        titulo: "Manifestación de masas en Buenos Aires",
        fecha: "2001-12-19",
        lugar: "Buenos Aires, Argentina",
        descripcion: "Movilización popular por crisis económica.",
        fuente: "Archivo Periodístico",
        lat: -34.6037,
        lng: -58.3816,
        categoria: "manifestacion de las masas",
        imagen: "./assets/img/marcha_obelisco.jpg"
      }
    ]
  },
  {
    id: 4,
    titulo: "Fenómenos Celestes",
    descripcion: "Eventos astronómicos y fenómenos del cielo.",
    fuente: "Observatorio Astronómico",
    hechos: [
      {
        id: 401,
        titulo: "Eclipse solar total",
        fecha: "2019-12-14",
        lugar: "Chile y Argentina",
        descripcion: "Eclipse total visible en varias regiones de Sudamérica.",
        fuente: "NASA",
        lat: -30.0,
        lng: -70.0,
        categoria: "eclipse",
        imagen: "./assets/img/eclipse.webp"
      }
    ]
  },
  {
    id: 5,
    titulo: "Historia y Sociedad",
    descripcion: "Hechos históricos que cambiaron el rumbo social y político.",
    fuente: "Archivo Histórico Nacional",
    hechos: [
      {
        id: 501,
        titulo: "Revolución de Mayo",
        fecha: "1810-05-25",
        lugar: "Buenos Aires, Argentina",
        descripcion: "Comienza el proceso de independencia argentina.",
        fuente: "Archivo General de la Nación",
        lat: -34.6037,
        lng: -58.3816,
        categoria: "manifestacion de las masas",
        imagen: "./assets/img/revolucion_de_mayo.jpg"
      }
    ]
  },
  {
    id: 6,
    titulo: "Desastres Marinos",
    descripcion: "Fenómenos en océanos y costas con impacto significativo.",
    fuente: "Instituto Oceanográfico",
    hechos: [
      {
        id: 601,
        titulo: "Tsunami en la costa de Chile",
        fecha: "2010-02-27",
        lugar: "Chile",
        descripcion: "Tsunami generado por un terremoto provoca evacuaciones masivas.",
        fuente: "Servicio Nacional de Emergencia",
        lat: -36.8269,
        lng: -73.0498,
        categoria: "tsunami",
        imagen: "./assets/img/tsunami.webp"
      }
    ]
  }
];
