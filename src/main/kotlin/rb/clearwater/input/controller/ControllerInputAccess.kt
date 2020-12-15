package rb.clearwater.input.controller

import rb.clearwater.input.*
import rb.vectrix.mathUtil.MathUtil
import rb.vectrix.mathUtil.PIf
import kotlin.math.PI

class ControllerInputAccess(
    private val feed: IRawControllerOutputFeed,
    private val writer : IInputWriter,
    binds: List<InputBind<InputKey>> )
{
    private class AxisBlob(
        val inputKey: InputKey,
        val pressThreshold: Float,
        val releaseThreshold: Float,
        var currentData: Float = 0f )
    private class JoystickBlob(
        val bind: JoystickInputBind<InputKey>,
        var currentX: Float = 0f,
        var currentY: Float = 0f,
        var prevX: Float = 0f,
        var prevY: Float = 0f,
        var currentPov: PovInputState = PovInputState.Neutral)

    private val buttonMapping: Map<Int,InputKey>
    private val flatAxisMapping: Map<Int,AxisBlob>
    private val joystickMapping: Map<Int,JoystickBlob>

    init {
        buttonMapping = binds
            .filterIsInstance<ButtonInputBind<InputKey>>()
            .map { Pair(it.buttonId, it.t) }
            .toMap()
        flatAxisMapping = binds
            .filterIsInstance<AxisInputBind1D<InputKey>>()
            .map { Pair(it.axisId, AxisBlob(it.t, it.triggerThreshold, it.releaseThreshold)) }
            .toMap()
        joystickMapping = binds
            .filterIsInstance<JoystickInputBind<InputKey>>()
            .flatMap {
                val blob = JoystickBlob(it)
                listOf(
                    Pair(it.xAxisId, blob),
                    Pair(it.yAxisId, blob) )
            }
            .toMap()

    }

    fun tick() {
        val data = feed.tick()
        data.forEach{ process(it)}
    }

    // Expect thetas in [-PI,PI]
    fun povFromTheta(theta: Float, diagRad: Float) : PovInputState {
        val d = PIf / 4f - diagRad
        return when {
            theta < -PIf  + d -> PovInputState.Left
            theta < -PIf/2 - d -> PovInputState.DownLeft
            theta < -PIf/2 + d -> PovInputState.Down
            theta < - d -> PovInputState.DownRight
            theta < d -> PovInputState.Right
            theta < PI/2 - d -> PovInputState.UpRight
            theta < PI/2 + d -> PovInputState.Up
            theta < PI - d -> PovInputState.UpLeft
            else -> PovInputState.Left
        }
    }

    fun rawUpdateFromPov(oldPov: PovInputState, newPov: PovInputState, bind: JoystickInputBind<InputKey>) {
        // Could probably be condinsed in code, but I have a feeling that'll be meh in effort-reward
        fun left( pov: PovInputState) = when(pov) {
            PovInputState.Left, PovInputState.UpLeft, PovInputState.DownLeft -> true
            else -> false
        }
        val oldLeft = left(oldPov)
        val newLeft = left(newPov)
        if( oldLeft != newLeft) if( newLeft) writer.pipeRawPress(bind.leftT) else writer.pipeRawRelease(bind.leftT)

        fun right(pov : PovInputState) = when(pov) {
            PovInputState.DownRight, PovInputState.Right, PovInputState.UpRight -> true
            else -> false
        }
        val oldRight = right(oldPov)
        val newRight = right(newPov)
        if( oldRight != newRight) if( newRight) writer.pipeRawPress(bind.rightT) else writer.pipeRawRelease(bind.rightT)

        fun up(pov: PovInputState) = when(pov) {
            PovInputState.UpRight, PovInputState.UpLeft, PovInputState.Up -> true
            else -> false
        }
        val oldUp = up(oldPov)
        val newUp = up(newPov)
        if(oldUp != newUp) if( newUp) writer.pipeRawPress(bind.upT) else writer.pipeRawRelease(bind.upT)

        fun down( pov: PovInputState) = when(pov) {
            PovInputState.DownRight, PovInputState.DownLeft, PovInputState.Down -> true
            else -> false
        }
        val oldDown = down(oldPov)
        val newDown = down(newPov)
        if( newDown != oldDown) if( newDown) writer.pipeRawPress(bind.downT) else writer.pipeRawRelease(bind.downT)
    }


    private fun process(datum: InputDatum) {
        when(datum) {
            is ButtonInput -> {
                buttonMapping[datum.buttinId]?.also { if(datum.pressed) writer.pipeRawPress(it) else writer.pipeRawRelease(it) }
            }
            is AxisInput -> {
                flatAxisMapping[datum.axisId]?.also {
                    val old = it.currentData
                    val new = datum.value
                    it.currentData = new
                    if(new < it.releaseThreshold && old >= it.releaseThreshold)
                        writer.pipeRawRelease(it.inputKey)
                    else if( new >= it.pressThreshold && old < it.pressThreshold)
                        writer.pipeRawPress(it.inputKey)
                }
                joystickMapping[datum.axisId]?.also {
                    if( it.bind.xAxisId == datum.axisId){
                        it.prevX = it.currentX
                        it.currentX = datum.value
                    }
                    if( it.bind.yAxisId == datum.axisId){
                        it.prevY = it.currentY
                        it.currentY = datum.value
                    }
                    val oldR = MathUtil.distance(0f,0f,it.prevX, it.prevY)
                    val nowR = MathUtil.distance(0f, 0f, it.currentX, it.currentY)
                    if( (nowR >= it.bind.triggerThreshold && oldR < it.bind.triggerThreshold) ||
                        (nowR >= it.bind.releaseThreshold && it.currentPov != PovInputState.Neutral))
                    {
                        val theta = kotlin.math.atan2(-it.currentY, it.currentX) // Ugh.  Why does everything have to fuck with Y-orientation?  Positive Y = Up.
                        val newPov = povFromTheta(theta, it.bind.diagonalThata)
                        rawUpdateFromPov(it.currentPov, newPov, it.bind)
                        it.currentPov = newPov
                    }
                    else if( nowR < it.bind.releaseThreshold) {
                        rawUpdateFromPov(it.currentPov, PovInputState.Neutral, it.bind)
                        it.currentPov = PovInputState.Neutral
                    }

                }
            }
            is PovInput -> {/* TODO */}
        }

    }
}