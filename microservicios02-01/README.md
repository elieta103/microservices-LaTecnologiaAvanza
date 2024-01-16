# Proyecto 01. Servicios simples, Eureka, ApiGateway, ConfigServer
-   API-GATEWAY	        8083
-   CONFIG-SERVER	    8085
-   INVENTARIO-SERVICE	8090    MYSQL
-   ORDER-SERVICE	    8089    MYSQL
-   PRODUCTO-SERVICE	8087    MONGODB
-   REGISTRY-SERVICE    8761


## Configuraciones
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