package rb.clearwater.collision.hitboxes

import rb.clearwater.zone.actors.IActorState

val OmniscientHitboxType = 0

interface IOmniscientHitbox : IHitbox {
    fun clairvoyance(state: IActorState<*>, hitboxes: List<IHitbox>, hurtbixes: List<IHurtbox>)
}