plugins {
    id("java")
    id("application")
    jacoco
    id("com.github.johnrengelman.shadow") version "8.1.1"
    id("net.razvan.jacoco-to-cobertura") version "1.2.0"
}

apply {
    plugin("java")
}

group = "ru.varfolomeev"
version = "1.0-SNAPSHOT"

application {
    mainClass = "Main"
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("commons-cli:commons-cli:1.6.0")
    implementation("org.reflections:reflections:0.10.2")
    implementation("com.github.javaparser:javaparser-core:3.23.1")

    implementation("org.slf4j:slf4j-nop:1.7.29")

    testImplementation(platform("org.junit:junit-bom:5.9.1"))
    testImplementation("org.junit.jupiter:junit-jupiter")
}

tasks.withType<JavaCompile> {
    options.compilerArgs.addAll(listOf("-Xlint:all", "-Werror"))
}

tasks.test {
    useJUnitPlatform()
    finalizedBy(tasks.jacocoTestReport)
}

tasks.jacocoTestReport {
    dependsOn(tasks.test)
    reports {
        xml.required = true
        csv.required = false
        html.required = false
    }
    finalizedBy(tasks.jacocoToCobertura)
}

tasks.jacocoToCobertura {
    dependsOn(tasks.jacocoTestReport)
}