package rb.clearwater.input.controller

import rb.vectrix.mathUtil.PIf
import kotlin.math.PI

sealed class InputBind<T>

data class ButtonInputBind<T>(
    val buttonId : Int,
    val t: T) : InputBind<T>()

data class AxisInputBind1D<T>(
    val axisId: Int,
    val t: T,
    val triggerThreshold: Float = 0.4f,
    val releaseThreshold: Float = 0.1f ) : InputBind<T>()

data class AxisInputBind2D<T>(
    val axisId: Int,
    val tPositive: T,
    val tNegative: T,
    val triggerThreshold: Float = 0.4f,
    val releaseThreshold: Float = 0.1f) : InputBind<T>()

data class JoystickInputBind<T>(
    val xAxisId: Int,
    val yAxisId: Int,
    val upT: T,
    val downT: T,
    val leftT: T,
    val rightT: T,
    // directional theta has to be within this number of point directly in the diagonal direction
    // In particular 20% of a quarter of the circle is chosen (so that both halfs is 40%).  Could tweak it closer to
    // 50% or even 60% in order to get a larger diag range
    val diagonalThata: Float = PIf/2f* 0.2f,
    // These act in on distance from center
    val triggerThreshold: Float = 0.4f,
    val releaseThreshold: Float = 0.3f
) : InputBind<T>()

data class PovInputBind<T>(
    val povId: Int,
    val upT: T,
    val downT: T,
    val leftT: T,
    val right: T) : InputBind<T>()