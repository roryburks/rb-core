package rb.clearwater.kronos

interface ITimeEngine
{
    var fps: Float
    fun start(onTick: ()->Unit)
    fun pause()
}


