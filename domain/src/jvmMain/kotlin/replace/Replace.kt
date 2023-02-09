package replace

object Replace {
    private const val VERSION_FILE = "replace/version"
    val version: String by lazy {
        checkNotNull(Replace::class.java.classLoader.getResourceAsStream(VERSION_FILE)) { "Could not find version file $VERSION_FILE" }
            .bufferedReader().readLine()
    }
}
