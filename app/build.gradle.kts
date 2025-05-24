import org.gradle.api.tasks.testing.logging.TestExceptionFormat
import org.gradle.api.tasks.testing.logging.TestLogEvent

plugins {
    java
    jacoco
    id("com.github.ben-manes.versions") version "0.52.0"
    id("io.spring.dependency-management") version "1.1.0"

}

group = "com.bookstore"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.springframework:spring-tx:6.1.2")

    implementation("org.springframework:spring-orm:6.1.2")

    implementation("org.springframework:spring-context:6.1.2")

    // Spring Data JPA
    implementation("org.springframework.data:spring-data-jpa:3.1.2")

    // JPA API (jakarta)
    implementation("jakarta.persistence:jakarta.persistence-api:3.1.0")

    // Hibernate ORM как JPA-провайдер
    implementation("org.hibernate.orm:hibernate-core:6.4.2.Final")

    // Встроенная база данных H2
    runtimeOnly("com.h2database:h2:2.2.224")

    implementation("org.springframework.security:spring-security-web:6.3.5")
    implementation("org.springframework.security:spring-security-config:6.3.5")
    implementation("org.springframework.security:spring-security-core:6.4.4")

    implementation("org.springframework:spring-web:6.1.12")
    implementation("org.springframework:spring-webmvc:6.1.14")
    implementation("org.springframework:spring-aop:6.1.5")


    implementation("jakarta.servlet:jakarta.servlet-api:6.0.0")

    implementation("com.fasterxml.jackson.core:jackson-databind:2.16.1")

    implementation("org.slf4j:slf4j-api:2.0.9")
    implementation("ch.qos.logback:logback-classic:1.5.13")

    testImplementation("org.springframework:spring-test:6.1.5")
    testImplementation("org.springframework.security:spring-security-test:6.1.5")
    testImplementation("org.junit.jupiter:junit-jupiter:5.10.0")
    testImplementation("org.mockito:mockito-core:5.11.0")
    testImplementation("org.mockito:mockito-junit-jupiter:5.11.0")
    testImplementation("org.assertj:assertj-core:3.25.3")
    testImplementation("com.github.tomakehurst:wiremock-jre8:3.0.1")
}

tasks.named<Test>("test") {
    useJUnitPlatform()
    jvmArgs = listOf(
        "-XX:+EnableDynamicAgentLoading",
        "-Djdk.instrument.traceUsage=false",
        "-Xshare:off"
    )

    testLogging {
        exceptionFormat = TestExceptionFormat.FULL
        events = mutableSetOf(TestLogEvent.FAILED, TestLogEvent.PASSED, TestLogEvent.SKIPPED)
        showStandardStreams = true
    }
}


/*tasks.named("build") {
    dependsOn("checkstyleMain")
    dependsOn("checkstyleTest")
}
*/

sourceSets {
    main {
        resources {
            srcDir("src/main/webapp")
        }
    }
}
