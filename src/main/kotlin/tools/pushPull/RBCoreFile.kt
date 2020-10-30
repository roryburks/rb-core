package tools.pushPull

import java.io.File

data class RBCoreFile(
    val root: String,
    val deps: Map<String, String>)

object RBCoreFileParser {
    fun parse(filename: String) : RBCoreFile {
        val file = File(filename)
        if( !file.exists()) throw Error("File to Parse does not exist.  $filename")

        val lines = file.readLines()

        val rootLine = lines.indexOfFirst { it.startsWith("root:") }
        if( rootLine == -1) throw Error("Root Line does not exist")
        val rootFilename = lines[rootLine+1].trim { it.isWhitespace() }
        val rootDir = File(rootFilename)
        if( !rootDir.isDirectory) throw Error("Root file is not a directory.")

        val depLines = lines
            .mapIndexed { index, s -> Pair(index, s) }
            .filter { p -> p.second.startsWith("dep:") }
            .map { it.first }
        val deps = depLines.map { lineNum ->
            val depName = lines[lineNum].substring(4).trim { it.isWhitespace() }
            val depFile = lines[lineNum+1].trim{ it.isWhitespace()}
            Pair(depName, depFile)
        }
            .toMap()


        return RBCoreFile(rootFilename, deps)
    }
}
