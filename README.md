# code-test
## Deployed Version
A deployed version of this can be found running on Heroku: https://itsjohno-codetest.herokuapp.com/

## Build and Run
Run the solution, easily, from your machine:
```
mvn clean install spring-boot:run
```

## Deploy to Docker
You can build a Docker of the Spring Boot app using the Maven plguin in the pom:
```
mvn clean install dockerfile:build
```

This will create a Docker image, itsjohno/codetest. This can be excuted through Docker:
```
docker run -p 8080:8080 --name dogs --rm itsjohno/codetest
```

The above command will create a container from your image named `dogs`, bind the container's `8080` port to your local machines and will destroy the container on exit. Add `-d` to run the container in the background.

## Utilising the API
The API is documented with Swagger, and information can be found by accessing the API documentation at http://localhost:8080/swagger-ui.html

## What's missing...
* I *really* wanted to create some UI components in React as I love front-end dev, but unfortunately time got the better of me.
* It's not really unit tested to an acceptable level (by most standards).
    * Coverage is exceptionally low, but that being said I've not actually had to write very much code.
    * I'm relying on frameworks to do the heavy lifting and I do not want to end up testing their code.