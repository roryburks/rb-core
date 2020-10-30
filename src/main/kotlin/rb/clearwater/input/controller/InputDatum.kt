package rb.clearwater.input.controller

sealed class InputDatum

data class ButtonInput(
    val buttinId: Int,
    val pressed: Boolean ) : InputDatum()

data class AxisInput(
    val axisId: Int,
    val value: Float) : InputDatum()

enum class PovInputState{
    Right, UpRight, Up, UpLeft, Left, DownLeft, Down, DownRight, Neutral }

data class PovInput(
    val povId: Int,
    val value: PovInputState ) : InputDatum()
