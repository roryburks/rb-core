package rb.animo.animationContext

import rb.animo.animation.IAnimation
import rb.clearwater.zone.base.IZoneAccessBase

class RootBuildAnimContext<T:IAnimatedActorState<T>>(
        anim: IAnimation,
        fps: Double,
        met: Double = 0.0)
{
    val root = BuildingAnimationContext(this, anim, fps, met)

    fun build() : IAnimationContext<T> {
        return root._build()
    }
}

class BuildingAnimationContext<T: IAnimatedActorState<T>>(
    internal val root: RootBuildAnimContext<T>,
    internal var anim: IAnimation,
    internal var fps: Double,
    internal var met: Double = 0.0,
    internal var onEnd:  ((T, IZoneAccessBase)->Unit)? = null,
    internal var nextAnim: BuildingAnimationContext<T>? = null)
{
    fun build() = root.build()

    internal fun _build() : IAnimationContext<T>{
        val nextAnim = nextAnim
        if( nextAnim != null) {
            val nextContext = nextAnim._build()
            onEnd = {t, zone -> t.anim = nextContext }
        }
        return AnimationContext(anim, fps, met, onEnd = onEnd)
    }
}


fun <T : IAnimatedActorState<T>> IAnimation.play( fps: Double, met: Double = 0.0) = RootBuildAnimContext<T>(this, fps, met).root

fun <T : IAnimatedActorState<T>> BuildingAnimationContext<T>.then(anim : IAnimation, fps: Double, met: Double = 0.0) : BuildingAnimationContext<T>
{
    val next = BuildingAnimationContext<T>(root, anim, fps, met)
    nextAnim = next
    return next
}

fun <T : IAnimatedActorState<T>> BuildingAnimationContext<T>.then(onEnd:  ((T, IZoneAccessBase)->Unit)) = also { this.onEnd = onEnd }