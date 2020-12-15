package rb.clearwater

import rb.clearwater.Game.State.ACTIVE
import rb.clearwater.input.IInputReader
import rb.clearwater.zone.base.IMeta
import rb.glow.IGraphicsContext
import rb.glow.MTextPlacer
import rb.vectrix.linear.Vec2i

interface IGame {
    fun step()
    fun draw( gc: IGraphicsContext)

    fun setWorld(meta: IMeta)
}

class Game(
    val textPlacer: MTextPlacer,
    val screenDims: Vec2i,
    val rawInput: IInputReader ) : IGame
{
    enum class State {
        ACTIVE,
    }


    var state : rb.clearwater.Game.State = ACTIVE
    private var _meta: IMeta? = null

    var m = 0
    override fun step() {
        val (gameI,metaI,systemI) = rawInput.tick()

        when(state) {
            ACTIVE -> {
//                if( systemI.pressing(Menu)){
//                    state = MENU
//                }
                _meta?.tick(gameI, metaI, systemI)
            }
        }
    }

    override fun draw( gc: IGraphicsContext) {
        when(state) {
            ACTIVE -> _meta?.draw(gc)
        }
    }

    override fun setWorld(meta: IMeta) { _meta = meta}
}