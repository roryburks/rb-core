package rb.clearwater.hybrid

import rb.vectrix.mathUtil.f

object HybridUtil {
    fun random(bottom: Double, top: Double) = (Math.random() * (top - bottom) + bottom)
    fun random(bottom: Float, top: Float) = (Math.random() * (top - bottom) + bottom).f
}