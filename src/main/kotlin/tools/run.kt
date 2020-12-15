package tools

import tools.pushPull.RBCoreFileParser
import tools.pushPull.RBCorePush
import tools.pushPull.RBDepFileParser

val spirite = "Spirite"
val fDom = "FJVM"
val clearmap = "clearmap"
val clearpipe = "clearpipe"

fun main() {
    pullFrom(fDom)
    //pushTo(clearmap)
    //pushAllFromCore()
}

fun pushTo(domain: String) {
    val rbcf = RBCoreFileParser.parse("C:\\Workspace\\RBCoreFile.txt")
    val depFilePath = rbcf.deps[domain]!!
    val depFile = RBDepFileParser.parse(depFilePath)
    RBCorePush.push(rbcf.root, depFile.root, depFile.sub)
}

fun pullFrom( domain: String) {
    val rbcf = RBCoreFileParser.parse("C:\\Workspace\\RBCoreFile.txt")
    val depFilePath = rbcf.deps[domain]!!
    val depFile = RBDepFileParser.parse(depFilePath)
    RBCorePush.pull(depFile.root, rbcf.root, depFile.sub)
}

fun pushAllFromCore() {
    val rbcf = RBCoreFileParser.parse("C:\\Workspace\\RBCoreFile.txt")

    rbcf.deps.forEach { (domain, depFilePath) ->
        val depFile = RBDepFileParser.parse(depFilePath)
        RBCorePush.push(rbcf.root, depFile.root, depFile.sub)
    }
}