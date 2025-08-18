plugins {
    id("java-library")
}

allprojects {
    group = "joserodpt.reallogin"
    version = "dev-Rewrite"

    apply(plugin = "java-library")

    java {
        withSourcesJar()
    }
}

subprojects {
    repositories {
        // Maven Local Repository
        mavenLocal()
        // Maven Central Repository
        mavenCentral()
        // Another Repositories
        maven("https://repo.papermc.io/repository/maven-public/")
        maven("https://hub.spigotmc.org/nexus/content/repositories/snapshots/")
        maven("https://oss.sonatype.org/content/repositories/snapshots/")
        maven("https://repo.triumphteam.dev/snapshots/")
    }

    dependencies {
        // Lombok
        compileOnly("org.projectlombok:lombok:1.18.38")
        annotationProcessor("org.projectlombok:lombok:1.18.38")
        testCompileOnly("org.projectlombok:lombok:1.18.38")
        testAnnotationProcessor("org.projectlombok:lombok:1.18.38")
        // BoostedYAML
        implementation("dev.dejvokep:boosted-yaml:1.3.6")
        // Gson
        implementation("com.google.code.gson:gson:2.13.1")
    }

    java {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }

    tasks.withType<JavaCompile> {
        options.compilerArgs = listOf("-Xlint:deprecation")
        options.encoding = "UTF-8"
    }
}