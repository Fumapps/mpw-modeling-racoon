import java.io.File
import java.lang.IllegalArgumentException
import java.nio.charset.Charset

/*
 * Assumptions:
 * - for non-parent POMs, the module version is only retrieved by the parent, which is the first version-tag in the file
 * - for parent POMs, the parent version is the first version-tag in the file
 */

val sourceVersion = "1.0.2"
val targetVersion = "1.0.3-SNAPSHOT"

val rootDir = ".."
val projectName = "racoon"

/* Start of Script Logic */
val expectedRootDirName = "mpw-modeling-$projectName"
val absolutePathToRootDir = File(rootDir).canonicalFile
if (absolutePathToRootDir.nameWithoutExtension != expectedRootDirName) {
    throw IllegalArgumentException("The root directory has to be $expectedRootDirName, but is ${absolutePathToRootDir.nameWithoutExtension}")
}
absolutePathToRootDir.walkTopDown().forEach { file ->
    file.on("pom.xml") {
        this.replaceFirstAfter("</modelVersion>", sourceVersion, targetVersion)
            .replaceInDependencyTags()
    }
    file.on("MANIFEST.MF") {
        withReplacedSnapshotQualifiers {
            replace("Bundle-Version: $sourceVersion", "Bundle-Version: $targetVersion")
        }
    }
    file.on("feature.xml") {
        withReplacedSnapshotQualifiers {
            replace("version=\"$sourceVersion\"", "version=\"$targetVersion\"")
        }
    }
}

fun File.on(extension: String, function: String.() -> String) {
    if (this.endsWith(extension)) {
        println("modify: ${this.path}")
        val oldFileContent = this.readText(Charsets.UTF_8)
        val newFileContent = oldFileContent.function()
        this.writeText(newFileContent, Charsets.UTF_8)
    }
}

fun String.replaceFirstAfter(pivotToBeginTheSearch: String, oldValue: String, newValue: String) : String {
    val index = this.indexOf(pivotToBeginTheSearch)
    if (index >= 0) {
        return this.replaceAfterIndex(index, oldValue, newValue)
    }
    return this
}

fun String.replaceAfterIndex(index: Int, oldValue: String, newValue: String) : String {
    val firstPart = this.substring(0, index)
    val secondPart = this.substring(index)
    return firstPart + secondPart.replaceFirst(oldValue, newValue)
}

fun String.withReplacedSnapshotQualifiers(function: String.() -> String): String {
    val adjusted = this.replace(".qualifier", "-SNAPSHOT")
    val replacedByFunction = adjusted.function()
    return replacedByFunction.replace("-SNAPSHOT", ".qualifier")
}

fun String.replaceInDependencyTags() : String {
    val regex = "(<dependency>" +
            "\\s*<groupId>[^<]*${projectName}[^<]*<\\/groupId>" +
            "\\s*<artifactId>[^<]*<\\/artifactId>" +
            "\\s*<version>)$sourceVersion(<\\/version>" +
            "\\s*<scope>[^<]*<\\/scope>" +
            "\\s*<\\/dependency>)"
    return this.replace(regex.toRegex(), "$1$targetVersion$2")
}
