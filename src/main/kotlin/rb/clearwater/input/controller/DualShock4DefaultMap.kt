package rb.clearwater.input.controller

import rb.clearwater.input.GameKey
import rb.clearwater.input.InputKey
import rb.clearwater.input.MetaKey

object DualShock4DefaultMap {
    val L2Axis get() = 4
    val L1Button get() = 9
    val R2Axis get() = 5
    val R1Button get() = 10
    val DpadPov get() = 0
    val UpButton get() = 11
    val DownButton get() = 12
    val LeftButton get() = 13
    val RightButton get() = 14
    val XAxisLeft get() = 0
    val YAxisLeft get() = 1
    val L3Button get() = 7
    val XAxisRight get() = 2
    val yAxisRight get() = 3
    val R3Button get() = 8
    val CrossButton get() = 0
    val CircleButton get() = 1
    val SquareButton get() = 2
    val TriangleButton get() = 3

    val SelectButton get() = 4
    val StartButton get() = 6

    val map: List<InputBind<InputKey>> get() = listOf(
        AxisInputBind1D<InputKey>(L2Axis, MetaKey.L2),
        AxisInputBind1D<InputKey>(R2Axis, MetaKey.R2),
        ButtonInputBind<InputKey>(L1Button, MetaKey.L1),
        ButtonInputBind<InputKey>(R1Button, MetaKey.R1),

        ButtonInputBind<InputKey>(SquareButton, GameKey.Shoot),
        ButtonInputBind<InputKey>(CrossButton, GameKey.Jump),
        ButtonInputBind<InputKey>(TriangleButton, GameKey.Charge),
        ButtonInputBind<InputKey>(CircleButton, GameKey.Dodge),

        ButtonInputBind<InputKey>(LeftButton, GameKey.Left),
        ButtonInputBind<InputKey>(RightButton, GameKey.Right),
        ButtonInputBind<InputKey>(DownButton, GameKey.Down),
        ButtonInputBind<InputKey>(UpButton, GameKey.Up),

        JoystickInputBind<InputKey>(
            XAxisLeft, YAxisLeft, GameKey.Up, GameKey.Down, GameKey.Left, GameKey.Right )
    )
}