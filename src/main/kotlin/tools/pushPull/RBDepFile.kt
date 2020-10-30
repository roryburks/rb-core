package tools.pushPull

import java.io.File

data class RBDepFile(
    val root: String,
    val sub: RBSubDomains)

data class RBSubDomains(
    val rbDomains: List<String>,
    val rbJvmDomains: List<String> )

object RBDepFileParser {
    fun parse(filename: String) : RBDepFile {
        val file = File(filename)
        if( !file.exists()) throw Error("File to Parse does not exist.  $filename")

        val lines = file.readLines()

        val rootLine = lines.indexOfFirst { it.startsWith("root:") }
        if( rootLine == -1) throw Error("Root Line does not exist")
        val rootFilename = lines[rootLine].substring(5).trim { it.isWhitespace() }
        val rootDir = File(File(filename).parent, rootFilename)
        //val rootDir = File(filename).toURI().relativize(File(rootFilename).toURI()).path
        if( !rootDir.isDirectory) throw Error("Root file is not a directory.")

        val rbLines = lines.filter { it.startsWith("/rb/") }
        val rbDomains = rbLines.map { it.substring(4).trim { it.isWhitespace() } }

        val rbjvmLines = lines.filter { it.startsWith("/rbJvm/") }
        val rbjvmDomains = rbjvmLines.map { it.substring(7).trim { it.isWhitespace() } }

        return RBDepFile(rootDir.canonicalPath, RBSubDomains( rbDomains, rbjvmDomains))
    }
}