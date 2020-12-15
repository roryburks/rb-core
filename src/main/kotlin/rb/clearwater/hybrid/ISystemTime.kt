package rb.clearwater.hybrid

import rb.global.IContract

interface ISystemTime {
    fun runLater( milliseconds: Int, lambda: ()->Unit)
    fun runRepeatedly(milliseconds: Int, lambda: () -> Unit) : IContract
    val currentMilli: Double
}
