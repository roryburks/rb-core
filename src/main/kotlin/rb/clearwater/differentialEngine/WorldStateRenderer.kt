package rb.clearwater.differentialEngine

import rb.animo.DrawContract
import rb.extendo.extensions.then
import rb.glow.IGraphicsContext

interface IWorldStateRenderer
{
    fun render(ws: WorldState) : List<DrawContract>
}

object WorldStateRenderer : IWorldStateRenderer{
    override fun render(ws: WorldState): List<DrawContract> {
        val actorContracts =  ws.actors.asSequence()
            .flatMap { it.draw() }

        val stageContracts = ws.stage.drawStageSpace()

        return actorContracts
            .then(stageContracts)
            .toList()
    }

}