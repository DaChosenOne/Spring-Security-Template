# Spring Security Template

## Introducción
Este proyecto tiene como finalidad poder ser utilizado como base en proyectos que necesitan utilizar seguridad (autenticacíon y autorizacion), se desarrollo en el framework Spring con su implementacion Spring Boot y sus modulos mas importantes de este ecosistema.

## Configuración
Para poder correr esta aplicacion en tu entorno de desarrollo local, tienes que dirigirte al archivo application.properties y poner los datos para poder acceder a tu gestor de base de datos, **es necesario crear la base de datos**.

## Datos importantes 
Por defecto la aplicación en su primera ejecución creara los roles y los permisos que se encuentran por defecto, ademas de crear un usuario con todos los permisos de la aplicacion.

El controlador main solo tiene unos metodos para comprobar que roles funcionan correctamente.

## Dependencias usadas en el proyecto
* Spring JPA
* Spring Security
* Spring Validation
* Spring Web
* Project Loombok
* JWT Atuh
* ModelMapper
