package rb.clearwater.zone.base

import rb.clearwater.resources.IResourceLoadingSystem
import rb.clearwater.collision.ICollisionAccess
import rb.clearwater.collision.alerting.IAlertingAccess
import rb.clearwater.collision.hitboxes.IHitbox
import rb.clearwater.collision.hitboxes.IHurtbox
import rb.clearwater.dialog.IDialogAccess
import rb.clearwater.zone.actors.IActorAccess
import rb.clearwater.zone.actors.IActorState
import rb.clearwater.zone.camera.ICameraAccess
import rb.clearwater.zone.particle.IParticleAccess

/** The ZoneAccess is an Actors both Read and Write Access into the various different sub-components of the World State. */
interface IZoneAccessBase {
    val actorAccess : IActorAccess
    val collisionAccess : ICollisionAccess
    val particleAccess : IParticleAccess
    val camera: ICameraAccess
    // projectileAccess
    val alertingAccess: IAlertingAccess
    val res : IResourceLoadingSystem
    val dialogAccess : IDialogAccess
    //val differentialAccess: IDifferentialAccess

    val tickRate: Double
    val met: Int
    // Hithox Access
}

/** This more specific Access allows the Actor access to information specific to it (and not the generic world state)
 * including the ability to die (nothing can kill actors externally; they have to choose to do) and knowledge of their
 * mid.*/
interface IDAAccess { // DirectActorAccess
    fun die()
    val thisMid : Int
}

/** This is the level of access an individual Actor will have within their tick/step method.  It combined the global
 * zone access with the Direct Actor Access (which gives them the ability to die and know their own mid).  Plus it allows
 * them to expose hitboxes and hurtboxes, actions which can only happen within the step method.
 */
interface IZoneAccess<T : IActorState<T>> : IZoneAccessBase, IDAAccess{
    fun addHitbox(hitbox: IHitbox)
    fun addHurtbox(hurtbox: IHurtbox)
}

