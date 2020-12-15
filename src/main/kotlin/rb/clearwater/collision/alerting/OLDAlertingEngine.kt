//package rb.clearwater.collision.alerting
//
//import rb.clearwater.collision.CollisionSpace
//import rb.extendo.extensions.append
//import rb.extendo.extensions.then
//import rb.vectrix.linear.Vec2
//import rb.vectrix.linear.Vec2d
//import rb.vectrix.mathUtil.MathUtil
//import kotlin.math.PI
//import kotlin.math.abs
//import kotlin.math.atan2
//import kotlin.math.log
//
//interface IAlertingEngine
//{
//    // Screen ~= 800/6 = 133 Maros long (67 for half-screen)
//    // Assume average being has a threshold of 10 Db
//    // Assume Doe is Running is 50 db
//    // Assume most can barely hear Doe running at 60 Maros
//    // Then 50 db would be at 1.875... Maros away
//    // Thus at 60 Maro = 32 times the distance of the base, volume would be ln(2)/ln(x)
//
//    // For sight I'll use inverse square rule
//    // Assume 10 = Running.
//    // Say a well-sighted human's peripheral threshold is 1
//    // A well-sighted thing looking in peripheral vision should be able to see a running doe
//    // at 100 maros.  So 1/x^2 = 1, ratio of 100*Mari
//    /**
//     * @param volume Measured in decibles at 1m away
//     *  0 = Completely Silent,
//     *  10 = Soft Breathing
//     *  20 = Moderate Breathing
//     *  30 = Walking / Heavy Breathing
//     *  50 = Running
//     *  // effectiveDb = log(2)/log(dist)*volume
//     *
//     *  @param motion Measured in puyos
//     *  0 = Still
//     *  10 = Normal Activity
//     *  20 = Frantic Jostling
//     *  effective cap is 20
//     *
//     */
//    fun reset()
//    fun emit(x: Double, y: Double, volume: Double, motion: Double, type: UID, uid: UID)
//    fun listenFor(x: Double, y: Double, dbThresh: Double, type: UID) : List<Vec2>
//    fun lookFor(x: Double, y: Double, lookAngle: Double, fov: Double, maxRange: Double, type: UID) : List<SeePoint>
//    fun getAllInRange(x: Double, y: Double, range: Double, type: UID) : List<Vec2>
//}
//
//data class SeePoint(val x: Double, val y: Double, val mot: Double)
//
//class AlertingEngine(var collisionSpace: CollisionSpace) : IAlertingEngine
//{
//    private data class AlertSet(val x: Double, val y: Double, val volume: Double, val motion: Double)
//    private var reported1 : MutableMap<UID,HashMap<UID,AlertSet>> = mutableMapOf()
//    private var reported2 : MutableMap<UID,HashMap<UID,AlertSet>> = mutableMapOf()
//
//    private fun getReported(type: UID) : Sequence<AlertSet> =
//            (reported1[type]?.asSequence() ?: emptySequence())
//                    .then((reported2[type]?.asSequence() ?: emptySequence()))
//                    .map { it.value }
//
//    override fun reset() {
//        reported2 = reported1
//        reported1 = mutableMapOf()
//    }
//
//    override fun emit(x: Double, y: Double, volume: Double, motion: Double, type: UID, uid: UID) {
//        val reportType = reported2[type]
//        if(reportType?.containsKey(uid) == true)
//        {
//            reportType.remove(uid)
//        }
//        reported1.append(type, uid, AlertSet(x, y, volume, motion))
//    }
//
//    override fun listenFor(x: Double, y: Double, dbThresh: Double, type: UID) : List<Vec2> {
//        return getReported(type)
//                .filter {dbThresh < 1 / log(MathUtil.distance(x,y,it.x,it.y)/C.HearingRatio,2.0)*it.volume}
//                .map { Vec2d(it.x, it.y) }
//                .toList()
//    }
//
//    override fun lookFor(x: Double, y: Double, lookAngle: Double, fov: Double, maxRange: Double, type: UID) : List<SeePoint>{
//        // TODO: Make better
//        return getReported(type)
//                .mapNotNull {
//                    val dist = MathUtil.distance(x,y,it.x, it.y)
//                    if( dist > maxRange)
//                        return@mapNotNull null
//
//                    val angle = atan2(it.y-y, it.x-x)
//                    val angleDiff = MathUtil.cycle(-PI, PI, angle - lookAngle)
//                    if( fov < abs(angleDiff))
//                        return@mapNotNull null
//
//                    SeePoint(it.x, it.y, 1 /it.motion)
//                }
//                .toList()
//    }
//
//    override fun getAllInRange(x: Double, y: Double, range: Double, type: UID): List<Vec2> {
//        return getReported(type)
//                .filter { MathUtil.distance(x,y,it.x, it.y) <= range }
//                .map { Vec2d(it.x, it.y) }
//                .toList()
//    }
//}
//
//object AlertingEngineConstants {
//    const val HearingRatio = 1.875f * 6 //Global.Mari
//    const val SeeingRatio = 1/(100*100* 36)// Global.Mari* Global.Mari)
//}
//private val C = AlertingEngineConstants