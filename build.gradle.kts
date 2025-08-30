plugins {
    java
    jacoco
    alias(libs.plugins.shadow)
}

dependencies {
    implementation(libs.h2)
    implementation(libs.hibernate)
    implementation(libs.logback)
    implementation(libs.hikaricp)
    implementation(libs.hibernate.hikaricp)

    testImplementation(libs.assertj.core)
    testImplementation(platform(libs.junit.bom))
    testImplementation(libs.junit.jupiter)
    testImplementation(libs.mockito.junit.jupiter)

    testRuntimeOnly(libs.junit.jupiter.engine)
    testRuntimeOnly(libs.junit.platform.launcher)
}

repositories {
    mavenCentral()
    mavenLocal()
}

tasks.build {
    dependsOn(tasks.shadowJar)
}

tasks.test {
    finalizedBy(tasks.jacocoTestReport)
    useJUnitPlatform()
    reports {
        html.required = true
    }
}

tasks.jacocoTestReport {
    dependsOn(tasks.test)
    reports {
        xml.required = false
        html.required = true
        html.outputLocation = layout.buildDirectory.dir("reports/jacoco")
    }
}

tasks.shadowJar {
    archiveClassifier = ""
    manifest {
        attributes(
            "Main-Class" to "com.github.squirrelgrip.meter.Main"
        )
    }
}