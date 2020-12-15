package rb.clearwater.differentialEngine

import rb.animo.DrawContract
import rb.clearwater.collision.StageSpace
import rb.clearwater.collision.alerting.IAlertEmissionSet
import rb.clearwater.collision.alerting.NilAlertEmissionSet
import rb.clearwater.collision.hitboxes.*
import rb.clearwater.input.IGameInputAccess
import rb.clearwater.zone.actors.IActor
import rb.clearwater.zone.actors.IActorState
import rb.clearwater.zone.base.IZoneAccess
import rb.clearwater.zone.base.IZoneAccessBase
import rb.clearwater.zone.camera.CameraState

data class WorldState (
    val actors: List<ActorK<*>>,
    val stage: StageSpace = StageSpace(),
    val cameraState: CameraState,
    val met: Int = 0,
    val alertEmissionSet: IAlertEmissionSet = NilAlertEmissionSet)
{
    fun dupe() : WorldState = copy(
        actors = actors.map { it },
        cameraState = cameraState.copy())
}

class ActorK<T : IActorState<T>> (
    val actor: IActor<T>,
    val state: T,
    val mid: Int )
{
    fun sublimate(hitboxSet: HitboxObjectSet) = SublimeActor(actor, state.dupe(), mid, hitboxSet)
    fun draw() : Sequence<DrawContract> {
        return actor.draw(state)
    }
}

/** An Actor in the Sublime is an Actor who has been lifted into a state of volatility.  This actor's state is in flux*/
class SublimeActor<T: IActorState<T>> (
    val actor: IActor<T>,
    val state: T,
    val mid: Int,
    val hitboxSet: HitboxObjectSet)
{
    //val hitboxes = mutableListOf<AHurtbox<T>>()
    var dead : Boolean = false; private set

    fun step(input: IGameInputAccess, zone: IZoneAccessBase) = actor.step(input, state, SubAccess(zone))
    fun onAdd(zone: IZoneAccessBase) = actor.onAdd(state, zone)
    fun deposit() = ActorK(actor, state, mid)

    inner class SubAccess(zone: IZoneAccessBase) : IZoneAccess<T>, IZoneAccessBase by zone{
        //override fun addHurtbox(hurtbox: AHurtbox<T>) {hitboxes.add(hurtbox) }
        override fun die() { dead = true}
        override val thisMid: Int = mid

        override fun addHitbox(hitbox: IHitbox) { hitboxSet.hitboxes.add(HitboxObject(hitbox, state, this)) }
        override fun addHurtbox(hurtbox: IHurtbox) { hitboxSet.hurtbox.add(HurtboxObject(hurtbox, state, this)) }
    }
}