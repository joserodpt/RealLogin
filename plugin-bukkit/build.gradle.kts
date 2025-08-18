plugins {
    id("java-library")
}

dependencies {
    implementation(project(":plugin-commons"))
    compileOnly("org.spigotmc:spigot-api:1.13-R0.1-SNAPSHOT")
    implementation("dev.triumphteam:triumph-cmd-bukkit:2.0.0-SNAPSHOT")
    implementation("dev.triumphteam:triumph-gui:3.2.0-SNAPSHOT")
}