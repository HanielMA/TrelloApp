# Aplicación con Spring Boot

La idea inicial parte de la iniciativa de **[Marcos](https://github.com/markikito)**. En resumen es una aplicación con **[Spring](https://github.com/HanielMA/TrelloApp)** en el back y con **[Angular2](www.repositoriodeAngular2.es)** en el front, la cual tiene dos finalidades:

1. Ser un valor añadido, para nosotros, a la aplicación Trello.
1. Generar conocimiento del funcionamiento de un proyecto, del trabajo en equipo y de las tecnologías actuales.

Este repository contiene la aplicación en SpringBoot. Es una API REST, con la que tendremos comunicación con la API de Trello.
 
## Empezando

Run Spring Boot.

```
mvn spring-boot:run
```

## Como usarlo

###/api/login

	Funcionamiento: Crea o actualiza el usario. 

	Body:
		- Campos obligatorios:
			{"email": "usuario@ejemplo.es","password":"tokenGeneradoPorTrelloEnAngular"}

		- Campos optionales:
			{"name":"NombreEjemplo", "email": "usuario@ejemplo.es","password":"tokenGeneradoPorTrelloEnAngular"}

	Respuesta: 
		En el Header nos devovlera un campo llamado 'x-auth-token' que contiene el token jwt, el cual vamos a necesitar incoprar en el Headers de la siguiente consultas.

###/api/users/me

	Funcionamiento: Devuelve datos del usuario autenticado.

	Headers: 
		x-auth-token : (Generado en el Login)
		Content-Type : application/json


## Proyectos relacionados

*Nuestro punto de partida ha sido de:*

* [Angular2 app](https://github.com/springboot-angular2-tutorial/angular2-app)
* [SpringBoot app](https://github.com/springboot-angular2-tutorial/boot-app)


##Integrantes

[Marcos](https://github.com/markikito) //
[Omar](https://github.com/portaslua) //
[Isaac](https://github.com/faerindel) //
[Airan](https://github.com/Hori30) //
[Fabio](https://github.com/alu0100463910) //
[Haniel](https://github.com/HanielMA/) //
[Gonzalo](https://github.com/gonzalogarciajaubert)

## License

[MIT](/LICENSE)
