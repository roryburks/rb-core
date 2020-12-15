package rb.clearwater.differentialEngine

import rb.animo.DrawContract
import rb.clearwater.resources.IResourceLoadingSystem
import rb.extendo.extensions.then

interface IWorldStateRenderer
{
    fun render(ws: WorldState, res: IResourceLoadingSystem) : List<DrawContract>
}

object WorldStateRenderer : IWorldStateRenderer{
    override fun render(ws: WorldState, res: IResourceLoadingSystem): List<DrawContract> {
        val actorContracts =  ws.actors.asSequence()
            .flatMap { it.draw() }

        val stageContracts = ws.stage.drawStageSpace(res)

        return actorContracts
            .then(stageContracts)
            .toList()
    }

}