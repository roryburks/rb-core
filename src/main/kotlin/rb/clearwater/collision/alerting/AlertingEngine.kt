package rb.clearwater.collision.alerting

import rb.global.ILogger
import rb.extendo.extensions.lookup
import rb.extendo.extensions.toLookup
import rb.global.GlobalDependencySet
import rb.vectrix.linear.Vec2d
import rb.vectrix.mathUtil.MathUtil
import kotlin.math.PI
import kotlin.math.abs
import kotlin.math.atan2
import kotlin.math.log

interface IAlertEmissionSet

object NilAlertEmissionSet : IAlertEmissionSet

interface IAlertingEngine {
    fun buildSet( emit: List<AlertEmitter>) : IAlertEmissionSet
    fun listen(
        set: IAlertEmissionSet,
        x: Double, y: Double,
        dbThresh: Double,
        types : List<Int>) : List<AlertResponse>
    fun look(
        set: IAlertEmissionSet,
        x: Double, y: Double,
        lookAngle: Double,
        fov: Double,
        maxRange: Double,
        types : List<Int>) : List<AlertResponse>
    fun getInRange(
        set: IAlertEmissionSet,
        x: Double, y: Double,
        r: Double,
        types : List<Int>) : List<AlertResponse>

}

class AlertingEngine(private val _logger : ILogger)
    : IAlertingEngine{

    override fun buildSet(emit: List<AlertEmitter>): IAlertEmissionSet {
        return EmissionSet(emit.toLookup { it.type })
    }

    override fun listen( set: IAlertEmissionSet, x: Double, y: Double, dbThresh: Double, types: List<Int> ): List<AlertResponse> {
        if( set == NilAlertEmissionSet){
            return emptyList()
        }
        if( set is EmissionSet){
            return types
                .flatMap { set.emissions.lookup(it) }
                .mapNotNull {
                    // Inverse squared + logarithmic DB or something
                    val relDb = it.volume / log(MathUtil.distance(x,y,it.x,it.y)/C.HearingRatio, 2.0)

                    if( relDb >= dbThresh) AlertResponse( Vec2d(it.x, it.y), relDb, it.motion, it.uid)
                    else null
                }
        }
        _logger.logError("Incompatible Emission Set passed into Alerting Engine")
        return emptyList()
    }

    override fun look( set: IAlertEmissionSet, x: Double, y: Double, lookAngle: Double, fov: Double, maxRange: Double, types: List<Int> ): List<AlertResponse> {
        if( set == NilAlertEmissionSet){
            return emptyList()
        }
        if( set is EmissionSet){
            return types
                .flatMap { set.emissions.lookup(it) }
                .mapNotNull {
                    val dist = MathUtil.distance(x,y,it.x, it.y)

                    if( dist > maxRange)
                        return@mapNotNull null

                    val angle = atan2(it.y - y, it.x-x)
                    val angleDiff = MathUtil.cycle(-PI, PI, angle - lookAngle)
                    if( fov < abs(angleDiff))
                        return@mapNotNull null

                    AlertResponse(Vec2d(it.x, it.y), it.volume, it.motion, it.uid )
                }
        }
        _logger.logError("Incompatible Emission Set passed into Alerting Engine")
        return emptyList()
    }

    override fun getInRange(
        set: IAlertEmissionSet,
        x: Double,
        y: Double,
        r: Double,
        types: List<Int>
    ): List<AlertResponse> {
        if( set == NilAlertEmissionSet){
            return emptyList()
        }
        if( set is EmissionSet){
            return types
                .flatMap { set.emissions.lookup(it) }
                .mapNotNull {
                    val dist = MathUtil.distance(x,y,it.x, it.y)

                    if( dist > r)
                        return@mapNotNull null

                    AlertResponse(Vec2d(it.x, it.y), it.volume, it.motion, it.uid )
                }
        }
        _logger.logError("Incompatible Emission Set passed into Alerting Engine")
        return emptyList()
    }

    private class EmissionSet(
        val emissions : Map<Int,List<AlertEmitter>> ) : IAlertEmissionSet
}

object AlertingEngineConstants {
    const val HearingRatio = 1.875f * 6 //Global.Mari
    const val SeeingRatio = 1/(100*100* 36)// Global.Mari* Global.Mari)
}
private  val C = AlertingEngineConstants

object AlertingEngineProvider {
    val Engine by  lazy { AlertingEngine( GlobalDependencySet.Logger.value)}
}
