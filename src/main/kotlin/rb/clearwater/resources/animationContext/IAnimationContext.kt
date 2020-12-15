package rb.clearwater.resources.animationContext

import rb.animo.DrawContract
import rb.animo.animation.RenderProperties
import rb.clearwater.zone.actors.IActorState
import rb.clearwater.zone.base.IZoneAccessBase
import rb.vectrix.linear.ITransform

interface IAnimationContext<T: IAnimatedActorState<T>> {
    fun tick( t: T, zone: IZoneAccessBase)
    fun draw(trans: ITransform) : Iterable<DrawContract> = draw(RenderProperties(trans = trans))
    fun draw(properties: RenderProperties,depth: Int = 0) : Iterable<DrawContract>
    fun dupe() : IAnimationContext<T>
}

interface IAnimatedActorState<T : IAnimatedActorState<T>> : IActorState<T> {
    var anim : IAnimationContext<T>?
}