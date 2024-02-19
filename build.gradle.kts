plugins {
    id("java")
    id("io.freefair.lombok") version "8.6"
    id("dev.jbang") version "0.2.0"
}

group = "com.ardetrick"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.apache.pdfbox:pdfbox:3.0.1")

    testImplementation(platform("org.junit:junit-bom:5.10.1"))
    testImplementation("org.junit.jupiter:junit-jupiter")
}

tasks.test {
    useJUnitPlatform()
}

java.toolchain {
    languageVersion.set(JavaLanguageVersion.of(21))
}
