## Sparker - A Project Bootstrapper

Sparker helps to bootstrap software projects by providing boiler-plate code such as folder structures, pom files and modules.
Sparker is currently capable of creating project structures centered around the Spring Framework.
It creates a backend module (app), an api module (api) and a ui module (ui).


## Usage

Use `java -jar sparker-X.X.X-SNAPSHOT.jar` in combination with the following options:

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


