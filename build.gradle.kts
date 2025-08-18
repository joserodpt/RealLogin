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
        mavenLocal()
        mavenCentral()
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