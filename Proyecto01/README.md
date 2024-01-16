# Proyecto 01. Servicios simples
- nombre : usuario-service 
- port   : 8081
- db     : MySQL
- nombre : hotel-service 
- port   : 8082
- db     : PostgreSQL
- nombre : calificacion-service 
- port   : 8083
- db     : MongoDB



## Configuraciones
- Ejecutar BD MySQL: 
    MySQLWorkBench (root, elieta103)
    C:\Workspace\Code\databases\mysql-volume>docker-compose up -d
- Ejecutar BD PostgreSQL:   
    PGAdmin(Master password: elieta103, BD Docker password : d3v3l0p3r)
    C:\Workspace\Code\databases\postgres-volume>docker-compose up -d
- Ejecutar BD MongoDB: 
    MongoDB Compass
    C:\Workspace\Code\databases\mongodb-volume>docker-compose up -d


## EndPoints
- POST http://localhost:8081/usuarios
{   "nombre": "Pepe",
    "email": "mail2@mail.com",
    "informacion": "Usuario Pepe"
}
- GET http://localhost:8081/usuarios
- GET http://localhost:8081/usuarios/cb4aabc3-c4e7-4e0b-ad2d-b362e33cb480

- POST http://localhost:8082/hoteles
{   "nombre":"Hotel02",
    "ubicacion":"Mexico02",
    "informacion":"Information02"
}
- GET http://localhost:8082/hoteles
- GET http://localhost:8082/hoteles/7da1b0e3-44ff-4ece-bc49-3c3f74482274

- POST http://localhost:8083/calificaciones
{   "usuarioId":"1bf52658-6601-4c64-bf8b-c29a82426765",
    "hotelId":"470f8a5c-5cb3-4f4d-afc1-73acf629f2d0",
    "calificacion":"4",
    "observaciones":"Esta sin pintar"
}
- GET http://localhost:8083/calificaciones
- GET http://localhost:8083/calificaciones/usuarios/1bf52658-6601-4c64-bf8b-c29a82426765
- GET http://localhost:8083/calificaciones/hoteles/470f8a5c-5cb3-4f4d-afc1-73acf629f2d0