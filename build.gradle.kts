plugins {
    id("java")
    id("com.gradleup.shadow") version("9.0.0-beta4")
}

group = "org.example"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.apache.flink:flink-clients:1.20.0")
    implementation("org.apache.flink:flink-connector-base:1.20.0")
    implementation("com.fasterxml.jackson.core:jackson-databind:2.18.2")
    implementation("redis.clients:jedis:5.2.0")

    runtimeOnly("org.apache.logging.log4j:log4j-slf4j-impl:2.17.1")
    runtimeOnly("org.apache.logging.log4j:log4j-api:2.17.1")
    runtimeOnly("org.apache.logging.log4j:log4j-core:2.17.1")

    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")

    compileOnly("org.apache.flink:flink-streaming-java:1.20.0")
    compileOnly("org.apache.flink:flink-core:1.20.0")
}

tasks.test {
    useJUnitPlatform()
}

tasks.shadowJar {
    manifest {
        attributes(mapOf("Main-Class" to "org.example.Main"))
    }
}