plugins {
    id("com.possible-triangle.gradle") version ("0.2.7")
}

subprojects {
    enablePublishing {
        githubPackages()
    }
}