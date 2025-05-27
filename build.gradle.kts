plugins {
    id("com.possible-triangle.gradle") version "0.1.4"
}

subprojects {
    repositories {
        mavenCentral()
    }

    enablePublishing {
        githubPackages()
    }
}

