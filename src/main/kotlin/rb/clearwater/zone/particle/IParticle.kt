package rb.clearwater.zone.particle

import rb.animo.DrawContract
import rb.animo.animation.IAnimation
import rb.animo.animation.RenderProperties
import rb.clearwater.resources.animationContext.SimpleAnimationContext
import rb.clearwater.zone.base.IZoneAccessBase


/***
 * Particles have zero interaction with anything and are not intended to have their states stored.  They simply live briefly
 * and are expected to die.
 */
interface IParticle {
    fun step(contract: IParticleContract, zone: IZoneAccessBase)
    fun draw() : Sequence<DrawContract>

    interface IParticleContract {
        fun die()
    }
}

interface IParticleAccess{
    fun addParticle(particle: IParticle)
}

abstract class FlashAnimationParticle( animation: IAnimation, fps: Double) : IParticle {
    val anim = SimpleAnimationContext(animation, fps)

    override fun step(contract: IParticle.IParticleContract, zone: IZoneAccessBase) {
        if( anim.tick(zone)) {
            contract.die()
        }
        _step(contract)
    }

    abstract fun _step(contract: IParticle.IParticleContract)
    abstract val properties : RenderProperties

    override fun draw() = anim.anim.draw(anim.met,properties).asSequence()
}