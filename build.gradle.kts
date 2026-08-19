plugins {
    id("java-library")
    id("maven-publish")
    id("idea")
    id("net.neoforged.moddev") version "2.0.89"
    id("io.freefair.lombok") version "8.11"
}

val baseArchivesName = project.property("mod_id").toString()
base {
    archivesName.set(project.property("mod_id").toString())
}
version = "${property("minecraft_version")}-${property("mod_version")}"
group = "${property("mod_group_id")}"

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(21))
    }
}

tasks.named<Wrapper>("wrapper") {
    distributionType = Wrapper.DistributionType.BIN
}

tasks.withType<Javadoc>().configureEach {
    isFailOnError = false
    (options as StandardJavadocDocletOptions).addStringOption("Xdoclint:none", "-quiet")
}

val localRuntime: Configuration by configurations.creating
configurations.runtimeClasspath {
    extendsFrom(localRuntime)
}

neoForge {
    version = "${property("neo_version")}"

    parchment {
        mappingsVersion.set(project.property("parchment_mappings_version").toString())
        minecraftVersion.set(project.property("parchment_minecraft_version").toString())
    }

    runs {
        register("client") {
            client()

            systemProperty("neoforge.enabledGameTestNamespaces", project.property("mod_id").toString())
        }

        register("server") {
            server()
            programArgument("--nogui")
            systemProperty("neoforge.enabledGameTestNamespaces", project.property("mod_id").toString())
        }

        register("gameTestServer") {
            type = "gameTestServer"
            systemProperty("neoforge.enabledGameTestNamespaces", project.property("mod_id").toString())
        }

        register("data") {
            data()
            programArguments.addAll(
                "--mod", project.property("mod_id").toString(),
                "--all",
                "--output", file("src/generated/resources/").absolutePath,
                "--existing", file("src/main/resources/").absolutePath
            )
        }

        configureEach {
            systemProperty("forge.logging.markers", "REGISTRIES")
            logLevel = org.slf4j.event.Level.DEBUG
        }
    }

    mods {
        create("${property("mod_id")}") {
            sourceSet(sourceSets.main.get())
        }
    }
}

sourceSets {
    main {
        resources.srcDir("src/generated/resources")
    }
}

repositories {
    flatDir {
        dirs("lib")
    }
    mavenLocal()
    mavenCentral()
    maven("https://maven.createmod.net") // Ponder, Flywheel
    maven("https://mvn.devos.one/snapshots") // Registrate
    maven("https://raw.githubusercontent.com/Fuzss/modresources/main/maven/") // ForgeConfigAPIPort
    maven("https://maven.blamejared.com") // JEI, Vazkii's Mods
    maven("https://cursemaven.com")
    maven("https://maven.ryanhcode.dev/releases")

    maven {
        name = "DevAuth Maven"
        url = uri("https://pkgs.dev.azure.com/djtheredstoner/DevAuth/_packaging/public/maven/v1")
    }
}

dependencies {
    jarJar(api("com.tterrag.registrate:Registrate:${property("registrate_version")}") {
        version {
            prefer(property("registrate_version") as String)
        }
    })
    jarJar(api("net.createmod.ponder:ponder-neoforge:${property("ponder_version")}+mc${property("minecraft_version")}") {
        version {
            strictly("[${property("ponder_version")}+mc${property("minecraft_version")},)")
            prefer("${property("ponder_version")}+mc${property("minecraft_version")}")
        }
    })

    compileOnly("dev.engine-room.flywheel:flywheel-neoforge-api-${property("minecraft_version")}:${property("flywheel_version")}")
    runtimeOnly("dev.engine-room.flywheel:flywheel-neoforge-${property("minecraft_version")}:${property("flywheel_version")}")

    runtimeOnly("dev.engine-room.vanillin:vanillin-neoforge-${property("minecraft_version")}:${property("vanillin_version")}")

    implementation("mezz.jei:jei-${property("jei_minecraft_version")}-neoforge:${property("jei_version")}")

    runtimeOnly("me.djtheredstoner:DevAuth-neoforge:1.2.1")
}

val generateModMetadata by tasks.registering(ProcessResources::class) {
    val replaceProperties = mapOf(
        "minecraft_version" to project.findProperty("minecraft_version") as String,
        "minecraft_version_range" to project.findProperty("minecraft_version_range") as String,
        "neo_version" to project.findProperty("neo_version") as String,
        "neo_version_range" to project.findProperty("neo_version_range") as String,
        "loader_version_range" to project.findProperty("loader_version_range") as String,
        "mod_id" to project.findProperty("mod_id") as String,
        "mod_name" to project.findProperty("mod_name") as String,
        "mod_license" to project.findProperty("mod_license") as String,
        "mod_version" to project.findProperty("mod_version") as String,
        "mod_authors" to project.findProperty("mod_authors") as String,
        "mod_credits" to project.findProperty("mod_credits") as String,
        "mod_description" to project.findProperty("mod_description") as String
    )
    inputs.properties(replaceProperties)
    expand(replaceProperties)
    // Exclude .java files or any other files that shouldn't have template expansion
    filesMatching("**/*.java") {
        exclude()
    }

    from("src/main/templates")
    into("build/generated/sources/modMetadata")
}

// Include the output of "generateModMetadata" as an input directory for the build.
// This works with both building through Gradle and the IDE.
sourceSets["main"].resources.srcDir(generateModMetadata)
neoForge.ideSyncTask(generateModMetadata)


java {
    withJavadocJar()
    withSourcesJar()
}

publishing {
    publications {
        register<MavenPublication>("mavenJava") {
            artifactId = "${property("mod_id")}"
            from(components["java"])
        }
    }
    repositories {
        maven {
            url = uri("file://${System.getenv("local_maven")}")
        }
    }
}

idea {
    module {
        for (fileName in listOf("run", "out", "logs")) {
            excludeDirs.add(file(fileName))
        }
    }
}

tasks.withType<JavaCompile> {
    options.encoding = "UTF-8"
}
