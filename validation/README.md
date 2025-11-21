# Jakarta Bean validation in a Spring webapp project in Kotlin

This repository demonstrate how to implement user input validation using the
[Jakarta Bean Validation framework](https://beanvalidation.org/) in a Spring webapp project written in Kotlin.

## Use cases

It covers the following scenarios:

* Groups Create and Update
* Group LogOnly
* Rewrite property names in paths (JSON names)
* Custom validator implementation with injected dependencies
* Manual (programmatic) validation
* Spring integration
* Swagger-UI with documented constraints: http://localhost:8080/swagger-ui/index.html

## Build and run

```shell
# Build
./gradlew bootJar

# Test
./gradlew test 

# Run
./gradlew bootRun
```