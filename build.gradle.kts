plugins {
    id 'java'
    id 'net.minecraftforge.gradle' version '5.1.+'
    id 'org.parchmentmc.librarian' version '1.+'
}

group = 'com.rain.arcane_convergence'
version = '1.0.0'
archivesBaseName = 'arcane_convergence'

java {
    toolchain.languageVersion = JavaLanguageVersion.of(17)
}

minecraft {
    mappings channel: 'parchment', version: '2024.01.14-1.20.1'
    
    runs {
        client {
            workingDirectory project.file('run')
            property 'forge.logging.markers', 'REGISTRIES'
            property 'forge.logging.console.level', 'debug'
            property 'mixin.env.remapRefMap', 'true'
            
            mods {
                arcane_convergence {
                    source sourceSets.main
                }
            }
        }
        server {
            workingDirectory project.file('run')
            property 'forge.logging.markers', 'REGISTRIES'
            property 'forge.logging.console.level', 'debug'
            property 'mixin.env.remapRefMap', 'true'
            
            mods {
                arcane_convergence {
                    source sourceSets.main
                }
            }
        }
    }
}

repositories {
    maven { url 'https://maven.minecraftforge.net' }
    maven { url 'https://maven.parchmentmc.org' }
    maven { url 'https://maven.blamejared.com' }
    maven { url 'https://maven.terraformersmc.com/releases' }
    maven { url 'https://curse.maven' }
}

dependencies {
    minecraft 'net.minecraftforge:forge:1.20.1-47.4.23'
    
    // Mixin Extras
    implementation 'com.llamalad7:mixinextras:0.3.5'
    annotationProcessor 'com.llamalad7:mixinextras:0.3.5'
    
    // Ars Nouveau
    runtimeOnly fg.deobf('curse.maven:ars-nouveau-246875:4836720')
    
    // Iron's Spellbooks
    runtimeOnly fg.deobf('curse.maven:irons-spellbooks-660248:4836723')
    
    // Goety (Soul Energy mod)
    runtimeOnly fg.deobf('curse.maven:goety-667386:4836725')
}

tasks.named('jar', Jar).configure {
    manifest {
        attributes([
                'Specification-Title'      : archivesBaseName,
                'Specification-Vendor'     : 'Rain',
                'Specification-Version'    : '1',
                'Implementation-Title'     : project.name,
                'Implementation-Version'   : project.jar.archiveVersion,
                'Implementation-Vendor'    : 'Rain',
                'Implementation-Timestamp' : new Date().format("yyyy-MM-dd'T'HH:mm:ssZ"),
                'MixinPlugin'              : 'com.rain.arcane_convergence.MixinConfigPlugin'
        ])
    }
}
