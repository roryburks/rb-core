package rb.clearwater.collision.alerting

import rb.vectrix.linear.Vec2d

data class AlertResponse(
    val location :Vec2d,
    val relativeDb: Double,
    val motion: Double,
    val uid: Int )

data class AlertEmitter(
    val x: Double,
    val y: Double,
    val volume: Double,
    val motion: Double,
    val uid: Int ,
    val type: Int)// Maybe type should be types plural, but problems for later.  -9.14.2020

interface IAlertingAccess {
    fun emit( x: Double, y: Double, volume: Double, motion: Double, type: Int, uid: Int)
    fun listenFor( x: Double, y: Double, dbThresh: Double, types: List<Int>) : List<AlertResponse>
    fun lookFor( x: Double, y: Double, lookAngle: Double, fov: Double, maxRange: Double, types: List<Int>) : List<AlertResponse>
    fun getAllInRange( x: Double, y: Double, r: Double, types: List<Int>) : List<AlertResponse>
}

class AlertingAccess (
    private val _engine: IAlertingEngine,
    private val _previousSet: IAlertEmissionSet) : IAlertingAccess
{
    private val _emissions = mutableListOf<AlertEmitter>()
    val emissions : List<AlertEmitter> get() = _emissions

    override fun emit(x: Double, y: Double, volume: Double, motion: Double, type: Int, uid: Int) {
        _emissions.add( AlertEmitter( x, y, volume, motion, uid, type ) )
    }

    override fun listenFor(x: Double, y: Double, dbThresh: Double, types: List<Int>): List<AlertResponse> {
        return _engine.listen(_previousSet, x, y, dbThresh, types)
    }

    override fun lookFor( x: Double, y: Double, lookAngle: Double, fov: Double, maxRange: Double, types: List<Int> ): List<AlertResponse> {
        return  _engine.look(_previousSet, x, y, lookAngle, fov, maxRange, types)
    }

    override fun getAllInRange(x: Double, y: Double, r: Double, types: List<Int>): List<AlertResponse> {
        return _engine.getInRange(_previousSet, x, y, r, types)
    }
}