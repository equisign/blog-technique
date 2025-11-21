import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

group = "equisign"
version = "1.0-SNAPSHOT"

plugins {
    java
    kotlin("jvm") version libs.versions.kotlin
    kotlin("kapt") version libs.versions.kotlin
    alias(libs.plugins.kotlin.allopen)
    alias(libs.plugins.kotlin.spring)
    alias(libs.plugins.spring.boot)
    alias(libs.plugins.spring.dependencyManagement)
}

repositories {
    mavenCentral()
}

dependencies {
    implementation(libs.kotlin.reflect)
    implementation(libs.kotlin.stdlib)
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-validation")
    implementation(libs.passay)
    implementation(libs.springdoc)

    testImplementation(kotlin("test"))
    testImplementation("org.springframework.boot:spring-boot-starter-test") {
        exclude(group = "org.assertj")
        exclude(group = "org.xmlunit")
        exclude(group = "org.mockito")
    }
    testImplementation(libs.restAssured.core)
    testImplementation(libs.restAssured.kotlin)

    // FIXME Groovy5 workaround for RestAssured:
    testImplementation("org.apache.groovy:groovy:4.0.29")
    testImplementation("org.apache.groovy:groovy-json:4.0.29")
    constraints {
        testImplementation("org.apache.groovy:groovy") {
            version {
                strictly("4.0.29")
            }
        }
        testRuntimeOnly("org.apache.groovy:groovy") {
            version {
                strictly("4.0.29")
            }
        }
        testImplementation("org.apache.groovy:groovy-json") {
            version {
                strictly("4.0.29")
            }
        }
        testRuntimeOnly("org.apache.groovy:groovy-json") {
            version {
                strictly("4.0.29")
            }
        }
    }
}

tasks.test {
    useJUnitPlatform()
}
kotlin {
    jvmToolchain(21)
}
val compileTestKotlin: KotlinCompile by tasks
compileTestKotlin.compilerOptions {
    freeCompilerArgs.set(listOf("-Xannotation-default-target=param-property"))
}