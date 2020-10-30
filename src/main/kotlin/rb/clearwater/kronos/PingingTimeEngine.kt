package rb.clearwater.kronos

import rb.clearwater.hybrid.ISystemTime

class PingingTimeEngine(private val _systemTime : ISystemTime)  : ITimeEngine {
    override var fps: Float = 30f
    private var _isPaused = false
    private var _nextStartNano :Double? = null
    private var _onTick : ()->Unit = {}

    override fun start(onTick: () -> Unit) {
        _onTick = onTick
        _systemTime.runRepeatedly(10) {tick()}
    }

    fun tick() {
        val nextNano = _nextStartNano
        val current = _systemTime.currentMilli
        if(current >= (nextNano ?: 0.0)) {
            _onTick()
            _nextStartNano  = when {
                nextNano == null -> current + 1000/fps
                current - nextNano > 1000 -> current + 1000/fps
                else -> nextNano + 1000/fps
            }
        }
    }

    override fun pause() {
        _isPaused = true
    }

}