package rb.clearwater.kronos

import rb.clearwater.hybrid.ISystemTime
import rb.vectrix.mathUtil.floor
import rb.vectrix.mathUtil.round

class SlidingTimeEngine(private val sysTime : ISystemTime) : ITimeEngine
{
    override var fps: Float = 60f

    private var nextStartNano = 0.0
    private var running = false
    private var onTick: (()->Unit)? = null

    override fun start(onTick: () -> Unit) {
        val oldRun = running
        running = true
        if(!oldRun)
            sysTime.runLater((1000 / fps).round) {tick()}

        this.onTick = onTick
        nextStartNano = sysTime.currentMilli
    }

    override fun pause() {
        running = false
    }

    val startMili = sysTime.currentMilli
    var i =0
    private fun tick() {
        ++i
        val now = sysTime.currentMilli
        val secondsPassed = (now - startMili) / 1000.0
        if( running) {
            onTick?.invoke()
            val currentNano = sysTime.currentMilli
            val expectedEnd = nextStartNano + 1000/fps
            val remainingTimeToWait = expectedEnd - currentNano
            nextStartNano = expectedEnd

            if( remainingTimeToWait < 0) {
                tick()
            }
            else
                sysTime.runLater(remainingTimeToWait.floor) {tick()}
        }
    }

}