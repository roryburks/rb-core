package rbJvm.glow

import rb.glow.GLowDependencySet
import rb.glow.gle.IGLEngine

object JvmGLowDependencyInjection {
    fun setDi(gle: IGLEngine){
        GLowDependencySet.gle = lazy { gle }
        GLowDependencySet.imageLoader = lazy { JvmImageLoader(GLowDependencySet.gle.value) }
    }
}