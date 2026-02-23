# RadioAcademy - Migración a Microservicios (WIP)

Evolución del proyecto RadioAcademy: transición de un monolito funcional a una arquitectura desacoplada orientada a microservicios.

## Arquitectura y Flujo de Seguridad


El sistema utiliza un API Gateway para el enrutado y un Auth Service dedicado para la emisión de tokens JWT. Los servicios individuales validan roles y claims de forma independiente.

## Estado del Desarrollo (Roadmap)
- [x] Diseño de arquitectura y API Gateway.
- [x] Configuración de Bases de Datos independientes (PostgreSQL/Flyway).
- [x] Servicio de Autenticación y firmas JWT.
- [ ] Implementación de *Services* y *Controllers* en microservicios de Cursos, Pagos y Notificaciones (En desarrollo).
- [ ] Refactorización del cliente Angular para consumir el nuevo Gateway.
