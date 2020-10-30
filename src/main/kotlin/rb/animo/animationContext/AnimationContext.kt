package rb.animo.animationContext

import rb.animo.animation.IAnimation
import rb.animo.animation.RenderProperties
import rb.clearwater.zone.base.IZoneAccessBase
import rb.vectrix.linear.ITransform

data class AnimationContext<T: IAnimatedActorState<T>>(
        val anim: IAnimation,
        var fps: Double,
        var met: Double = 0.0, // Value in [0,anim.length)
        val flipped: Boolean = false,
        val onEnd:  ((T, IZoneAccessBase)->Unit)? = null)
    : IAnimationContext<T>
{
    override fun tick(t: T, zone: IZoneAccessBase) {
        if( fps != 0.0) {
            met += fps / zone.tickRate
            val mod = anim.length
            if (met >=  mod )
            {
                met %= mod
                onEnd?.invoke(t, zone)
            }
            else if( met < 0){
                met = (met % mod) + mod
                onEnd?.invoke(t, zone)
            }
        }
    }
    override fun draw(properties: RenderProperties, depth: Int) = anim.draw(
            met = met,
            properties = if( flipped) properties.copy(trans = properties.trans * ITransform.Scale(-1.0, 1.0)) else properties,
            depth = depth)

    override fun dupe() = copy()
}

data class SimpleAnimationContext(
        val anim: IAnimation,
        val fps: Double,
        var met: Double = 0.0)
{
    fun tick(zone: IZoneAccessBase) : Boolean {
        if (fps != 0.0) {
            met += fps / zone.tickRate
            val mod = anim.length
            if (met >=  mod )
            {
                met %= mod
                return true
            }
        }
        return false
    }
}