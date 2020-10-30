package rb.clearwater.zone.base

import rb.animo.DrawContract
import rb.clearwater.IMenu
import rb.clearwater.differentialEngine.*
import rb.clearwater.hud.IHud
import rb.clearwater.input.*
import rb.clearwater.zone.camera.CameraProcessor
import rb.glow.*
import rb.vectrix.linear.ITransform

interface IMeta {
    fun tick(inGame: GameInputState, inMeta: MetaInputState, inSys: SystemInputState)
    fun draw( gc: IGraphicsContext)
}

class Meta(
    private val _tickRate: Double,
    startingState: WorldState,
    private val _hud : IHud,
    private val _oracleSystemFactory : IOracleSystemFactory,
    private val _menu: IMenu,
    private val _worldStateRenderer : IWorldStateRenderer = WorldStateRenderer
) : IMeta
{
    private var worldState: WorldState = startingState
    private var oraclesSystem : IOracleSystem = _oracleSystemFactory.begin(worldState)

    private var _state : State = State.Running
    private enum class State{
        Running,
        Menu,
        Stasis
    }

    private var prevMeta = MetaInputState(0)
    private var predictiveInput = GameInputState(0)
    private var prevSys = SystemInputState(0)

    override fun tick(inGame: GameInputState, inMeta: MetaInputState, inSys: SystemInputState) {
        val metaAccess = InputSnapshot<MetaKey>(prevMeta, inMeta)
        val systemAccess = InputSnapshot(prevSys, inSys)
        prevMeta = inMeta
        prevSys = inSys
        when(val ss = _state){
            State.Menu -> {
                if( _menu.step(systemAccess))
                    _state = State.Running
            }
            State.Stasis -> {
                predictiveInput = inGame
                if( metaAccess.pressed(MetaKey.L2)) _state = State.Running
                else if(
                    (metaAccess.isPressing(MetaKey.R2) && metaAccess.pressed(MetaKey.L1))||
                    (!metaAccess.isPressing(MetaKey.R2) && metaAccess.isPressing(MetaKey.L1)))
                {
                    worldState = oraclesSystem.tickBack()
                }
                else if(
                    (metaAccess.isPressing(MetaKey.R2) && metaAccess.pressed(MetaKey.R1)) ||
                    (!metaAccess.isPressing(MetaKey.R2) && metaAccess.isPressing(MetaKey.R1)))
                {
                    worldState = oraclesSystem.tickUp(inGame, true)
                }
                //oraclesSystem.scryForward(inGame)
            }
            State.Running -> {
                if( systemAccess.pressed(SystemKey.Menu)){
                    _state = State.Menu
                }
                else if( metaAccess.pressed(MetaKey.L2)) {
                    _state = State.Stasis
                }
                else {
                    worldState = oraclesSystem.tickUp(inGame)
                }
            }
        }

        _hud.tickHud( worldState)
    }

    //val bufImg = GLImage(300,300, Hybrid.gle, true)

    override fun draw(gc: IGraphicsContext) {
        gc.clear(ColorARGB32Normal(0x626664))
        gc.color = ColorARGB32Normal(0x626664)
        gc.drawer.fillRect(0.0,0.0,2000.0,2000.0)
        gc.transform = CameraProcessor.transformFrom(worldState.cameraState)

        val drawList = _worldStateRenderer.render(worldState)
            .toMutableList()

        drawList.sortWith(compareBy { -it.depth })
        drawList.forEach { render(it, gc); gc.alpha = 1f ; gc.composite = Composite.SRC_OVER}

        gc.transform = ITransform.Identity
        _hud.renderHud(worldState, gc)

        if( _state == State.Menu)
        {
            _menu.draw(gc)
        }

//        fun renderOthState( actor: ActorK<OthState>, color: Color, screenGc: IGraphicsContext)
//        {
//            val igc = GLGraphicsContext(bufImg, true)
//            igc.clear(Colors.TRANSPARENT)
//            igc.transform = ITransform.Translate(150.0 - actor.state.x,100.0- actor.state.y)
//
//            actor.draw().forEach { render(it, igc) }
//
//            screenGc.renderImage(bufImg, actor.state.x - 150.0, actor.state.y - 100.0, RenderRubric(method = RenderMethod(RenderMethodType.COLOR_CHANGE_HUE, color.argb32)))
//        }
//
//        if( _state == State.Paused){
//            gc.composite = Composite.ADD
//            val othB = worldState.actors
//                .asSequence()
//                .map{ it.state}
//                .filterIsInstance<OthState>()
//                .firstOrNull()
//            val speedB = othB?.run { MathUtil.distance(0.0,0.0, vx, vy) } ?: 0.0
//            val offB = when  {
//                speedB < OthConstants.MaxGroundSpeed * 0.2 -> 10
//                speedB < OthConstants.MaxGroundSpeed * 0.5 -> 7
//                speedB < OthConstants.MaxGroundSpeed * 0.75 -> 4
//                else -> 3
//            }
//            for (i in 1..5) {
//                gc.alpha = 1 - (i / 6f)
//                oraclesSystem.peekBack( offB*i)
//                    ?.actors
//                    ?.firstOrNull{ it.state is OthState}
//                    ?.also { renderOthState(it as ActorK<OthState>, Colors.BLUE, gc) }
//            }
//            val othF = oraclesSystem.getForesight(predictiveInput, 1)
//                ?.actors
//                ?.asSequence()
//                ?.map{ it.state}
//                ?.filterIsInstance<OthState>()
//                ?.firstOrNull()
//            val speed = othF?.run { MathUtil.distance(0.0,0.0, vx, vy) } ?: 0.0
//            val offF = when  {
//                speed < OthConstants.MaxGroundSpeed * 0.2 -> 10
//                speed < OthConstants.MaxGroundSpeed * 0.5 -> 7
//                speed < OthConstants.MaxGroundSpeed * 0.75 -> 4
//                else -> 3
//            }
//            for (i in 1..5) {
//                gc.alpha = 1 - (i / 6f)
//                oraclesSystem.getForesight(predictiveInput,i*offF)
//                    ?.actors
//                    ?.firstOrNull { it.state is OthState }
//                    ?.also { renderOthState(it as ActorK<OthState>, Colors.RED, gc) }
//            }
//        }

        gc.transform = ITransform.Identity
    }

    fun render(dc: DrawContract, gc: IGraphicsContext){
        if( dc.renderProperties != null){
            gc.pushTransform()
            gc.alpha = dc.renderProperties.alpha
            gc.composite = dc.renderProperties.composite
            gc.transform = gc.transform * dc.renderProperties.trans
            dc.drawRubrick(gc)
            gc.popTransform()
        }
        else dc.drawRubrick(gc)
    }
}