# Proyecto 03. Servicios simples, Eureka, ApiGateway, ConfigServer, Keycloak, Login a Eureka, Resilience4J, Proyecto con Spring-Retry
-   API-GATEWAY	        8083
-   CONFIG-SERVER	    8085
-   INVENTARIO-SERVICE	8090    MYSQL
-   ORDER-SERVICE	    8089    MYSQL
-   PRODUCTO-SERVICE	8087    MONGODB
-   REGISTRY-SERVICE    8761
-   KEYCLOAK            8081


## Configuraciones
- Agregar dependencias de resilience4J en orden-service
    - spring-cloud-starter-circuitbreaker-resilience4j
    - application.services en orden-service
    - Methodo FallBack en el Controller de orden-service
    - Cambio valor de retorno en service de OrdenService
- Para probar los servicios, se levantan todos los servicios, en inventario-service, se agrega un delay:
    -   log.info("Espera iniciada ....");
        Thread.sleep(10000);
        log.info("Espera finalizada ...");
    - Como tenemos configurado :
        resilience4j.retry.instances.inventario.max-attempts=5
        resilience4j.retry.instances.inventario.wait-duration=2s
        Deberia Intentar 5 veces con 2 segundos se separacion, SOLO TOMA EL DEFAULT(Hace 3 intentos cada 5seg)
    - Se agrega proyecto con spring-retry

- Install Keycloak con Docker :
    - docker run -p 8081:8080 -e KEYCLOAK_ADMIN=admin -e KEYCLOAK_ADMIN_PASSWORD=admin quay.io/keycloak/keycloak:20.0.3 start-dev
- Agregar un Realm : spring-boot-microservices-realm
- Agregar config en ApiGateway para keycloak
- Dependencias keycloak
    <artifactId>spring-boot-starter-oauth2-resource-server</artifactId>
	<artifactId>spring-boot-starter-security</artifactId>

- Configuracion Keycloak 
    - Crear un Realm : spring-boot-microservices-realm
    - Crear un cliente : 
        tipo -> OpenID Connect, 
        ClientID -> spring-boot-client, 
        Client authentication -> on, 
        Solo activar : Service accounts roles -> selected
        
- Agregar en ApiGateway :  SecurityConfig

- Configuracion ApiGateway, Properties de Keycloak :  Realm Settings -> OpenID Endpoint Configuration -> "issuer"
    http://localhost:8081/realms/spring-boot-microservices-realm
    spring.security.oauth2.resourceserver.jwt.issuer-uri=http://localhost:8081/realms/spring-boot-microservices-realm

- Para hacer pruebas desde postman :
    - Authorization -> OAuth 2
    - Header prefix -> Bearer
    - Token Name -> token
    - Grant Type -> Client Credentials
    - Access Token URL, desde keycloak:  Realm Settings -> OpenID Endpoint Configuration -> token_endpoint :  
        http://localhost:8081/realms/spring-boot-microservices-realm/protocol/openid-connect/token
    - Client ID, desde keycloak:  Clients -> spring-boot-client
    - Client Secret, desde keycloak: Clients -> spring-boot-client -> Credentials -> Clients Secrets :  HH9lCghH3igLL5xXxKiritltOqXYICga
    - Scope -> openid offline_access
    - Click boton 'Get New Access Token' -> Proceed -> Use token ->  Send el request.

- Agregar login a Eureka (NO FUNCIONA, CON EL CONFIG-SERVER )
    - Dependencias en registry-service
	    <artifactId>spring-boot-starter-security</artifactId>
    - Crear SecurityConfing en registry-service
    - Crear user/password en properties de registry
    - En los properties de los servicios
    # Sin Login Eureka
    # eureka.client.service-url.defaultZone=http://localhost:8761/eureka
    # Con Login Eureka
    eureka.client.service-url.defaultZone=http://eureka:password@localhost:8761/eureka

- Para hacer un proyecto modular :
    - Se crea un proyecto principal, solo con el pom.xml :
    - En cada modulo que se agrega, se pone como parent el modulo inicial y se agrega como modules.
- Config-Server
    - Dependencias
        -   spring-cloud-config-server
        -   spring-cloud-starter-netflix-eureka-client
    - Agregar configuraciones/rutas application.properties
    - Agregar @EnableConfigServer
    - Crear un repositorio en GIT: microservices-config-02
    - Agregar application, application-dev, application-prod
    - Agregar la configuracion del registro de eureka client de los servicios
    - Hasta este punto se puede levantar eureka y config-server y validar que levanta las configuraciones:
        -   http://localhost:8085/application/default
        -   http://localhost:8085/application/dev
        -   http://localhost:8085/application/prod
    - Posteriormente en cada ms-service agregar la dependencia, config-server client (orden, api-gateway, inventario, producto)
        -   spring-cloud-starter-config
    - Eliminar la configuracion del registro de eureka de (orden, api-gateway, inventario, producto), se lee del git y activar algun profile
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
        -   spring-cloud-starter
    - @EnableDiscoveryClient Agregar
    - Agregar configuraciones application.properties
    - Se puede agregar mas de una ruta : - Path=/hoteles/**,/staffs/**
- Eureka Service : Registro de servicios, disponibilidad, balanceo de cargas.
    -   Dependencias 
        -   spring-boot-starter-web
        -   spring-cloud-starter
        -   spring-cloud-starter-netflix-eureka-server
    -   Configuracion
        -   Agregar que Eureka no se registre a si mismo en application.properties
        -   @EnableEurekaServer
- Inventario, Orden, Producto Service
    -   Dependencias
        -   spring-cloud-starter
        -   spring-cloud-starter-netflix-eureka-client
        -   <spring-cloud.version>2023.0.0-RC1</spring-cloud.version>
        -   Cloud  <dependencyManagement>...
        -   Agregar en cada ms, su respectivo nombre ( spring.application.name INVENTARIO-SERVICE, ORDEN-SERVICE PRODUCTO-SERVICE)
        -   Agregar que Eureka  registre a los ms-services en application.properties
        -   @EnableDiscoveryClient
    - orden-service   Utiliza WebCliente para consumir Inventario el stock
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
- Con el Api-Gateway se entra por el 8083, por ejemplo :
- GUARDAR PRODUCTO      POST      http://localhost:8083/api/producto
{
    "nombre": "Memoria USB 3",
    "descripcion": "Memoria USB de 16GB",
    "precio": 500
}

- LISTAR PRODUCTO       GET       http://localhost:8083/api/producto
- REALIZAR ORDEN        POST      http://localhost:8083/api/order
{
    "orderLineItemsDtoList" : [
        {
            "codigoSku": "iphone_12",
            "precio": 1200,
            "cantidad" : 20
        }
    ]
}

- VER STOCK             GET        http://localhost:8083/api/inventario?codigoSku=iphone_12,iphone_12_blue