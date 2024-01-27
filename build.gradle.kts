plugins {
    id("java")
    id("io.freefair.lombok") version "8.4"
}

group = "com.ardetrick"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(platform("org.junit:junit-bom:5.10.1"))
    testImplementation("org.junit.jupiter:junit-jupiter")
}

tasks.test {
    useJUnitPlatform()
}

java.toolchain {
    languageVersion.set(JavaLanguageVersion.of(21))
}
