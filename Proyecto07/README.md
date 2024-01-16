# Proyecto 07. Servicios simples, Eureka Server, Rest Template Feign, API Gateway, Config Server, Resilience4J, Retry, Auth Service
- nombre : usuario-service 
- port   : 8081
- db     : MySQL
- nombre : hotel-service 
- port   : 8082
- db     : PostgreSQL
- nombre : calificacion-service 
- port   : 8083
- db     : MongoDB
- nombre : registry-service 
- port   : 8761
- nombre : api-gateway
- port   : 8084
- nombre : config-server
- port   : 8085
- nombre : auth-service
- port   : 8086


## Configuraciones
- Auth Service
    - JwtProvider : Genera token y lo valida
    - SecurityConfig : Configuracion csrf permite todos los request
    - AuthService : Funcionalidad para crear usuario, validar token , login.
    - AuthController : Endpoints de los servicios
    - En el Api-Gateway se agregan los filtros
        -   WebClientConfig y AuthFilter
    - En el Api-Gateway se agregan los dto
        -   DTO TokenDTO y RequestDTO, para request/response
    - En el Api-Gateway se agregan la configuracion de rutas
            - id: AUTH-SERVICE
              uri: lb://AUTH-SERVICE
              predicates:
                - Path=/auth/**
    - Se agrega el filtro en la ruta de usuarios
        - id: USUARIO-SERVICE
          uri: lb://USUARIO-SERVICE
          predicates:
            - Path=/usuarios/**
          filters:
            - AuthFilter
    - Activar el servicio como cliente de eureka: @EnableDiscoveryClient


- Retry
    - Agregar : @Retry(name = "ratingHotelService", fallbackMethod = "ratingHotelFallback")
    - Comentar la de @CircuitBreaker
    - Configurar application.yml
        -       retry:
                    instances:
                        ratingHotelService:
                        max-attempts : 3
                        wait-duration: 5s
    - Detener HotelService y probar UsuarioporId
- Circuit Breaker, Resilience4J
    - Dependencias
        -   spring-boot-starter-actuator
        -   spring-cloud-starter-circuitbreaker-resilience4j
        -   spring-boot-starter-aop
    - Agregar configuracion en application.yml
    - Agregar metodos del circuit breaker : ratingHotelFallback
    - Agregar : @CircuitBreaker(name = "ratingHotelBreaker", fallbackMethod = "ratingHotelFallback")
    - Detener HotelService y probar UsuarioporId
- Config-Server
    - Dependencias
        -   spring-cloud-config-server
        -   spring-cloud-starter-netflix-eureka-client
    - Agregar configuraciones/rutas application.yml
    - Agregar @EnableConfigServer
    - Crear un repositorio en GIT: microservices-config
    - Agregar application, application-dev, application-prod
    - Agregar la configuracion del registro de eureka client de los servicios
    - Hasta este punto se puede levantar eureka y config-server y validar que levanta las configuraciones:
        -   http://localhost:8085/application/default
        -   http://localhost:8085/application/dev
        -   http://localhost:8085/application/prod
    - Posteriormente en cada ms-service agregar la dependencia, config-server client (usuario, api-gateway, hotel, calificaciones)
        -   spring-cloud-starter-config
    - Eliminar la configuracion del registro de eureka de (usuario, api-gateway, hotel, calificaciones), se lee del git y activar algun profile
        config:
            import: configserver:http://localhost:8085
        profiles:
            active: dev
    - Si se requiere activar la configuracion default:     
     config:
            import: optional:configserver:http://localhost:8085

- Api-Gateway
    - Dependencias
        -   spring-cloud-starter-netflix-eureka-client
        -   spring-cloud-starter-gateway
        -   spring-boot-starter-webflux
        -   reactor-test
        -   spring-cloud-starter
    - @EnableDiscoveryClient Agregar
    - Agregar configuraciones application.yml
    - Se puede agregar mas de una ruta : - Path=/hoteles/**,/staffs/**
- OpenFeign
    - Dependencia : spring-cloud-starter-openfeign
    - Agregar @EnableFeignClients en class main UsuarioServiceApplication
    - Agregar package feign, se declara las interfaces a consumir
- RestTemplate 
    -   @LoadBalanced restTemplate.getForEntity("http://HOTEL-SERVICE/hoteles/"+calificacion.getHotelId(),Hotel.class);
    -   Sin balanceo de carga: restTemplate.getForEntity("http://localhost:8082/hoteles/"+calificacion.getHotelId(),Hotel.class);
    -   usuario-service consume a hotel-service y/o calificacion-service
    -   Agregar @Configuration MyConfig class y @Bean RestTemplate
    -   Las llamadas a los ms-services se realiza en la capa @Service 
    -   Utilizar pojos para encapsular la respuesta de los ms-services
- Eureka Service : Registro de servicios, disponibilidad, balanceo de cargas.
    -   Dependencias 
        -   spring-boot-starter-web
        -   spring-cloud-starter
        -   spring-cloud-starter-netflix-eureka-server
    -   Configuracion
        -   Agregar que Eureka no se registre a si mismo en application.yml
        -   @EnableEurekaServer
- Usuario, Hotel, Calificacion Service
    -   Dependencias
        -   spring-cloud-starter
        -   spring-cloud-starter-netflix-eureka-client
        -   <spring-cloud.version>2023.0.0-RC1</spring-cloud.version>
        -   Cloud  <dependencyManagement>...
        -   Agregar en cada ms, su respectivo nombre ( spring.application.name USUARIO-SERVICE, HOTEL-SERVICE CALIFICACION-SERVICE)
        -   Agregar que Eureka  registre a los ms-services en application.yml
        -   @EnableDiscoveryClient
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
- Con el Auth-Service 
- Crear Usuarios un admin y un user
- POST http://localhost:8084/auth/create
{   "userName": "eliel",
    "password": "root",
    "role": "admin"
}
{   "userName": "july",
    "password": "root",
    "role": "user"
}

- Obtener token para los usuarios creados
- POST http://localhost:8084/auth/login
{   "userName": "eliel",
    "password": "root"
}
TOKEN : eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJlbGllbCIsImlkIjoxLCJyb2xlIjoiYWRtaW4iLCJpYXQiOjE3MDUxODQ4NzMsImV4cCI6MTcwNTE4ODQ3M30.cMa7F-Nblw1u9RxFM7NjxkfeuH9q_qpxKBmek3S06YY


- POST http://localhost:8084/auth/login
{   "userName": "july",
    "password": "root"
}
TOKEN : eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJqdWx5IiwiaWQiOjIsInJvbGUiOiJ1c2VyIiwiaWF0IjoxNzA1MTg0OTAwLCJleHAiOjE3MDUxODg1MDB9.RPNIIJeEO04QV2bVDaBrxTZKrtVQJlrclMbU8VIHkbw

- Authorization -> Bearer Token  y en Params: token y Cadena del token, Body
- Solo los admin pueden ejecutar POST
- admin y user pueden ejecutar GET
- POST http://localhost:8084/auth/validate?token=eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJlbGlldGExMDMiLCJpZCI6Mywicm9sZSI6ImFkbWluIiwiaWF0IjoxNzA1MTgwOTQyLCJleHAiOjE3MDUxODQ1NDJ9.f_LdxqAMffD4bw3Vx21gn1BCI673hBsxzOlgJ-1rxoQ
{   "uri": "/usuarios",
    "method": "POST"
}

- Para consumir cualquier otra API, se requiere el token de lo contrario mandará un BAD-Request
    -   Authorization -> Bearer Token -> Token generado -> OK


- Con el Api-Gateway se entra por el 8084, por ejemplo :
- GET http://localhost:8084/usuarios
- GET http://localhost:8084/usuarios/cb4aabc3-c4e7-4e0b-ad2d-b362e33cb480

- Registry Service  http://localhost:8761/
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