package rb.clearwater.zone.base

import rb.animo.loading.AnimationLoaderSystemProvider
import rb.animo.loading.IAnimationLoadingSystem
import rb.clearwater.IMenu
import rb.clearwater.collision.StageSpace
import rb.clearwater.differentialEngine.*
import rb.clearwater.hud.IHud
import rb.clearwater.zone.actors.IActor
import rb.clearwater.zone.actors.IActorState
import rb.clearwater.zone.camera.CameraState
import rb.clearwater.zone.stagePieces.StagePiece
import rb.vectrix.linear.Vec2i

class MetaActor<T : IActorState<T>>(
    val actor : IActor<T>,
    val state : T)
{
    internal fun k(mid: Int) = ActorK<T>(actor, state, mid)
}

interface IMetaFactory{
    fun makeWorld(actors: List<MetaActor<*>>, stages: List<StagePiece>, hud: IHud, menu: IMenu, screenDims: Vec2i) : IMeta
}

class MetaFactory(
    private val _processor: IWorldStateProcessor,
    private val _animAccess : IAnimationLoadingSystem ) : IMetaFactory
{
    override fun makeWorld( actors: List<MetaActor<*>>, stages : List<StagePiece>, hud: IHud, menu: IMenu, screenDims: Vec2i) : IMeta
    {
        var mid = 0
        val wsBase = WorldState(
            actors.map { it.k(mid++) },
            StageSpace(stages),
            CameraState(screenDims.xi, screenDims.yi) )
        val ws = _processor.onInit(wsBase, _animAccess)

        return Meta(
            60.0,
            ws,
            hud,
            OracleSystemFactoryProvider.Factory,
            menu)
    }
}

object MetaFactoryProvider {
    val Factory : IMetaFactory = MetaFactory(
        _processor = WorldStateProcessorProvider.Processor,
        _animAccess = AnimationLoaderSystemProvider.System )

}