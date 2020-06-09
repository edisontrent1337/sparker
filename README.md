## Sparker - A Project Bootstrapper

Sparker helps to bootstrap software projects by providing boiler-plate code such as folder structures, pom files and modules.
Sparker is currently capable of creating project structures centered around the Spring Framework.
It creates a backend module (app), an api module (api) and a ui module (ui).

## Preliminaries

Note that you must have installed the following dependencies and tools to use this software:
* `maven`
* `npm` & `node`
* `Java`
* `docker`
* `docker-compose`

## Usage

First, build `Sparker` from source by running

``mvn clean install``

Navigate into `sparker\target` and use `java -jar sparker-X.X.X-SNAPSHOT.jar` in combination with the following options:

### Options

| Argument            | Description                                                |
|---------------------|------------------------------------------------------------|
| --artifactId        | The artifact id.                                           |
| --basePath          | The path where the project should be created.              |
| --groupId           | The group id complying with the maven naming conventions.  |
| --help              | Shows the help dialog.                                     |
| --language          | The language of the backend module. Can be java or kotlin. |
| --mainClass         | The desirec name of the main class of the backend module.  |
| --projectName       | The name of the project.                                   |
| --runAsServer       | Flag to run this application as a server.                  |


## Features

### General
* Automatic generation of project structure with `maven`
* Automatic generation of a `maven` build script
* Automatic generation of a ready to deploy docker image with all dependencies included

### Backend Module (app)
* Automatic generation of `pom.xml`
* Inclusion of `app` module as a submodule
* Creation of a Spring container preconfigured to serve as a web server with the front-end code already compiled and ready to be served
* Automatic configuration of backend module to use api specified by `api.yaml` file
* Automatic generation a docker-compose file for deployment
* Automatic generation of a `fly-way` migration script

### API Module (api)
* Automatic generation of `pom.xml`
* Inclusion of `api` module as a submodule
* Automatic generation of build scripts necessary to create Java API classes

### UI Module (ui)
* Automatic generation of `pom.xml`
* Automatic generation of a front-end module based on `react` and `create-react-app`
* Inclusion of `ui` module as a submodule
* Automatic generation of a `package.json` file including useful scripts like redeployment of compiled javascript into the static directory served by the `Spring` backend

## Build project

After executing the bootstrap command, simply navigate into the folder of the newly created project and type
``mvn clean install``. Your project will be built and a new docker image of your project should be created.
