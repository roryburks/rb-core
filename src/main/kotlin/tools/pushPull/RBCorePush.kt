package tools.pushPull

import java.io.File
import java.lang.Error
import java.nio.file.Files
import java.nio.file.StandardCopyOption

object RBCorePush {
    fun push(fromDir: String, toDir: String, subDomains: RBSubDomains) {
        fun sub( sub: String, domains: List<String>){
            val fromSub = File(fromDir + File.separator + sub)
            if( !fromSub.isDirectory) throw Error("${fromSub.absolutePath} is not a directory")
            val toSub = File(toDir + File.separator + sub)
            if( !toSub.isDirectory) throw Error("${toSub.absolutePath} is not a directory")

            toSub.deleteRecursively()
            toSub.mkdir()
            domains.forEach {
                val fromDoubleSub = File(fromSub.canonicalPath + File.separator + it)
                val toDoubleSub = File(toSub.canonicalPath + File.separator + it)
                copyRec(fromDoubleSub, toDoubleSub)
            }

        }

        if( subDomains.rbDomains.any())
            sub("rb", subDomains.rbDomains)
        if( subDomains.rbJvmDomains.any())
            sub("rbJvm", subDomains.rbJvmDomains)
    }

    private fun copyRec(from: File, to: File) {
        val toRoot = to.toPath()
        val fromRoot = from.toPath()
        Files.walk(fromRoot)
            .forEach { sourcePath ->
                val targetPath = toRoot.resolve(fromRoot.relativize(sourcePath))
                Files.copy(sourcePath, targetPath, StandardCopyOption.REPLACE_EXISTING)
            }

    }
}