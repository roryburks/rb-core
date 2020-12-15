package rb.clearwater.collision.hitboxes

import rb.clearwater.zone.actors.IActorState
import rb.clearwater.zone.base.IDAAccess
import rb.clearwater.zone.base.IZoneAccessBase
import rb.extendo.extensions.lookup
import rb.extendo.extensions.then
import rb.extendo.extensions.toLookup


interface IHitboxEngine {
    fun runHitboxInteractions(
        zone: IZoneAccessBase,
        hitboxes: List<HitboxObject<*>>,
        hurtboxes: List<HurtboxObject<*>> )
}

object HitboxEngine : IHitboxEngine{
    /** A quick note about the Hitbox Engine: Some cursory attempts to make it optimized (type lookups, etc) are made,
     * but on the whole it is not at all what I would call optimized, but the interface should allow it to be so in
     * the future, if that becomes a need. 9.15.2020
     */
    override fun runHitboxInteractions(
        zone: IZoneAccessBase,
        hitboxes: List<HitboxObject<*>>,
        hurtboxes: List<HurtboxObject<*>> )
    {
        val hitboxesByType = hitboxes
            .toLookup ({ it.hitbox.type },{ HitboxCollisionContract(it.hitbox,it.actorAccess, it.state)})

        for (hurtbox in hurtboxes) {
            val hurtboxContract = HurtboxCollisionContract(hurtbox.hurtbox, hurtbox.actorAccess, hurtbox.state)

            for (type in hurtboxContract.hurtbox.typesCollidesWith) {
                val hitboxesOfType = hitboxesByType.lookup(type)
                    .filter { !it.isConsumed }

                for (hitboxContract in hitboxesOfType) {
                    if( hitboxContract.hitbox.collision.intersects(hurtbox.hurtbox.collision))
                    {
                        hitboxContract.hitbox.collide(zone, hitboxContract, hurtboxContract)
                    }
                }
            }
        }

        for (special in hitboxesByType.lookup(OmniscientHitboxType)){
            (special.hitbox as? IOmniscientHitbox)?.let { omni ->
                omni.clairvoyance(special.state, hitboxes.map { it.hitbox }, hurtboxes.map { it.hurtbox })
            }
        }
    }
}

object HitboxEngineProvider {
    val Engine : IHitboxEngine get() = HitboxEngine
}


