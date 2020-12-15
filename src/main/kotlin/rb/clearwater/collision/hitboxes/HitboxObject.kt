package rb.clearwater.collision.hitboxes

import rb.clearwater.zone.actors.IActorState
import rb.clearwater.zone.base.IDAAccess

class HitboxObject<T : IActorState<T>>(
    val hitbox: IHitbox,
    val state: IActorState<T>,
    val actorAccess : IDAAccess )

class HurtboxObject<T : IActorState<T>>(
    val hurtbox: IHurtbox,
    val state: IActorState<T>,
    val actorAccess : IDAAccess )

class HitboxObjectSet {
    val hitboxes = mutableListOf<HitboxObject<*>>()
    val hurtbox = mutableListOf<HurtboxObject<*>>()
}

