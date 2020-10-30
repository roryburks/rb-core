package rb.clearwater.collision.hitboxes

import rb.clearwater.zone.actors.IActorState
import rb.clearwater.zone.base.IDAAccess
import rb.clearwater.zone.base.IZoneAccess
import rb.clearwater.zone.base.IZoneAccessBase
import rb.vectrix.intersect.CollisionObject
import rb.vectrix.intersect.CollisionRigidRect
import rb.vectrix.shapes.Rect

class HitboxCollisionContract(
    val hitbox: IHitbox,
    val actorAccess: IDAAccess,
    val state: IActorState<*>,
    var isConsumed: Boolean = false )

class HurtboxCollisionContract(
    val hurtbox: IHurtbox,
    val actorAccess: IDAAccess,
    val state: IActorState<*>)


interface IHitbox {
    val collision : CollisionObject
    val type: Int

    fun collide(zone: IZoneAccessBase, hitbox: HitboxCollisionContract, hurtbox: HurtboxCollisionContract)
}

interface IHurtbox {
    val collision : CollisionObject
    val typesCollidesWith: HashSet<Int>
}
