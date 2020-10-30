package tools

import tools.pushPull.RBCoreFileParser
import tools.pushPull.RBCorePush
import tools.pushPull.RBDepFileParser

fun main() {
    val rbcf = RBCoreFileParser.parse("C:\\Workspace\\RBCoreFile.txt")
    val rbDepFile = rbcf.deps["Spirite"]!!

    val rbDep = RBDepFileParser.parse(rbDepFile)

    RBCorePush.push(rbcf.root, rbDep.root, rbDep.sub)
}