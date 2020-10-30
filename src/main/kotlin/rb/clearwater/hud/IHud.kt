package rb.clearwater.hud

import rb.clearwater.differentialEngine.WorldState
import rb.glow.IGraphicsContext

interface IHud {
    fun tickHud( worldState: WorldState)
    fun renderHud( worldState: WorldState, gc: IGraphicsContext)
}

object NilHud : IHud {
    override fun tickHud(worldState: WorldState) {}
    override fun renderHud(worldState: WorldState, gc: IGraphicsContext) { }
}