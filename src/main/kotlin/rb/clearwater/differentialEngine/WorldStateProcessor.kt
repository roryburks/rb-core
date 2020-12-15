package rb.clearwater.differentialEngine

import rb.clearwater.resources.IResourceLoadingSystem
import rb.clearwater.collision.CollisionAccess
import rb.clearwater.collision.ICollisionAccess
import rb.clearwater.collision.alerting.AlertingAccess
import rb.clearwater.collision.alerting.AlertingEngineProvider
import rb.clearwater.collision.alerting.IAlertingEngine
import rb.clearwater.collision.hitboxes.HitboxEngineProvider
import rb.clearwater.collision.hitboxes.HitboxObjectSet
import rb.clearwater.collision.hitboxes.IHitboxEngine
import rb.clearwater.dialog.DialogSystemProvider
import rb.clearwater.dialog.IDialogAccess
import rb.clearwater.dialog.IDialogSystem
import rb.clearwater.input.IGameInputAccess
import rb.clearwater.zone.actors.IActorAccess
import rb.clearwater.zone.base.IZoneAccessBase
import rb.clearwater.zone.camera.CameraAccess
import rb.clearwater.zone.particle.IParticleAccess
import rb.extendo.extensions.then

interface IWorldStateProcessor {
    fun tick(
        ws: WorldState,
        input: IGameInputAccess,
        res: IResourceLoadingSystem) : WorldState

    fun onInit(
        ws: WorldState,
        res: IResourceLoadingSystem ) : WorldState
}

class WorldStateProcessor(
    private val _alertingEngine : IAlertingEngine ,
    private val _hitboxEngine: IHitboxEngine,
    private val _dialogSystem : IDialogSystem
) : IWorldStateProcessor
{
    override fun tick(
        ws: WorldState,
        input: IGameInputAccess,
        res: IResourceLoadingSystem
    ): WorldState
    {
        val hitboxSet = HitboxObjectSet()

        // Note: Order-dependency is hand-waved here.  Right now there is an implicit expectation that WorldState will
        // list Actors in order of MID.
        val actors = ws.actors.map { it.sublimate(hitboxSet) }
        val actorAccess = DeActorAccess(actors)

        val access = Access(ws, res, actorAccess)

        // 1: Step
        actors.forEach { it.step(input, access) }

        // 2: Hitboxes
        _hitboxEngine.runHitboxInteractions(access, hitboxSet.hitboxes, hitboxSet.hurtbox)

        // 3: Add the new actors and remove dead actors
        val newActors = actorAccess.newActorList.map { it.sublimate(hitboxSet) }
        newActors.forEach { it.onAdd(access) }
        val oldActors = actors.asSequence()
            .filter {  !it.dead}
        val completeActorSet = oldActors
            .then(newActors.asSequence())
            .map { it.deposit() }
            .toList()

        return  WorldState(
            actors = completeActorSet,
            stage = ws.stage,
            cameraState = access.camera.cameraState,
            met = ws.met + 1,
            alertEmissionSet = _alertingEngine.buildSet(access.alertingAccess.emissions))
    }

    override fun onInit(ws: WorldState, res: IResourceLoadingSystem): WorldState {
        val hitboxSet = HitboxObjectSet()

        // Note: Order-dependency is hand-waved here.  Right now there is an implicit expectation that WorldState will
        // list Actors in order of MID.
        val actors = ws.actors.map { it.sublimate(hitboxSet) }

        val actorAccess = DeActorAccess(actors)
        val access = Access(ws, res, actorAccess)

        actors.forEach { it.onAdd(access)}


        return  WorldState(
            actors = actors.map { it.deposit() } ,
            stage = ws.stage,
            cameraState = access.camera.cameraState,
            met = 0,
            alertEmissionSet = _alertingEngine.buildSet(access.alertingAccess.emissions))
    }

    private inner class Access(
        val ws: WorldState,
        override val res: IResourceLoadingSystem,
        override val actorAccess: IActorAccess
    ) : IZoneAccessBase {
        override val camera = CameraAccess(ws.cameraState.copy())
        override val collisionAccess: ICollisionAccess = CollisionAccess(ws.stage)
        override val tickRate: Double get() = 60.0
        override val met: Int get() = ws.met
        override val alertingAccess = AlertingAccess(_alertingEngine, ws.alertEmissionSet)
        override val dialogAccess: IDialogAccess get() = _dialogSystem

        override val particleAccess: IParticleAccess
            get() = TODO("not implemented") //To change initializer of created properties use File | Settings | File Templates.
    }
}

object WorldStateProcessorProvider {
    val Processor = WorldStateProcessor(
        _alertingEngine = AlertingEngineProvider.Engine ,
        _hitboxEngine = HitboxEngineProvider.Engine ,
        _dialogSystem = DialogSystemProvider.System.value)
}