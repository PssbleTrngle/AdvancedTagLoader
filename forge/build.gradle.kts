forge {
    enableMixins()

    dependOn(project(":common"))
}

uploadToModrinth {
    syncBodyFromReadme()
}