package rb.clearwater.collision

import rb.animo.DrawContract
import rb.clearwater.resources.IResourceLoadingSystem
import rb.clearwater.zone.stagePieces.StagePiece
import rb.extendo.extensions.append
import rb.vectrix.intersect.CollisionArc
import rb.vectrix.intersect.CollisionObject
import rb.vectrix.intersect.intersectsPrecise
import rb.vectrix.mathUtil.MathUtil
import rb.vectrix.mathUtil.floor
import rb.vectrix.shapes.*
import kotlin.math.max
import kotlin.math.min

class StageSpace(
        val stagePieces: List<StagePiece> = emptyList(),
        val tileWidth: Float = 512f,
        val tileHeight: Float = 512f
        ) : ICollisionAccess
{
    private val tile = mutableMapOf<Pair<Int,Int>, MutableList<CollisionObject>>()

    init {
        val collisions = stagePieces.mapNotNull { it.collision }

        for ( obj in collisions) {
            val bounds = obj.bounds
            val left = min(bounds.x1, bounds.x2)
            val right = max(bounds.x1, bounds.x2)
            val bottom = min(bounds.y1, bounds.y2)
            val top = max(bounds.y1, bounds.y2)

            for( x in (left / tileWidth).floor..(right / tileWidth).floor) {
                for (y in (bottom / tileHeight).floor..(top / tileHeight).floor) {
                    tile.append(Pair(x,y), obj)
                }
            }
        }
    }


    override fun checkCollision(collisionObject: CollisionObject): Boolean {
        // Step 1: Determine what tiles it spans
        // Step 2: Get all things in the tile
        // Step 3: Perform Collision
        val x1 = collisionObject.bounds.x1
        val x2 = collisionObject.bounds.x2
        val y1 = collisionObject.bounds.y1
        val y2 = collisionObject.bounds.y2

        for( x in (x1 / tileWidth).floor..(x2 / tileWidth).floor) {
            for(y in (y1 / tileHeight).floor..(y2 / tileHeight).floor){
                tile[Pair(x,y)]?.forEach {
                    if( collisionObject intersects  it)
                        return true
                }
            }
        }
        return false
    }

    override fun checkCollision( arc: Arc) : Double? {
        // Step 1: Determine what tiles it spans
        // Step 2: Get all things in the tile
        // Step 3: Perform Collision
        val carc = CollisionArc(arc)
        val x1 = carc.bounds.x1
        val x2 = carc.bounds.x2
        val y1 = carc.bounds.y1
        val y2 = carc.bounds.y2

        var min : Double? = null
        for( x in (x1 / tileWidth).floor..(x2 / tileWidth).floor) {
            for(y in (y1 / tileHeight).floor..(y2 / tileHeight).floor){
                tile[Pair(x,y)]?.forEach {min = MathUtil.minOrNull(min, arc intersectsPrecise it)}
            }
        }

        return min
    }

    override fun checkCollision(rect: RayRect): Double? {
        // Step 1: Determine what tiles it spans
        // Step 2: Get all things in the tile
        // Step 3: Perform Collision
        var min : Double? = null

        for( x in (rect.left / tileWidth).floor..(rect.right / tileWidth).floor) {
            for(y in (rect.bottom / tileHeight).floor..(rect.top / tileHeight).floor){
                tile[Pair(x,y)]?.forEach {min = MathUtil.minOrNull(min, rect intersectsPrecise it)}
            }
        }

        return min
    }

    override fun checkCollision(line: LineSegment): Double? {
        // Step 1: Determine what tiles it spans
        // Step 2: Get all things in the tile
        // Step 3: Perform Collision
        val left = min(line.x1, line.x2)
        val right = max(line.x1, line.x2)
        val bottom = min(line.y1, line.y2)
        val top = max(line.y1, line.y2)

        var min : Double? = null

        for( x in (left / tileWidth).floor..(right / tileWidth).floor) {
            for(y in (bottom / tileHeight).floor..(top / tileHeight).floor){
                tile[Pair(x,y)]?.forEach {min = MathUtil.minOrNull(min, line intersectsPrecise it)}
            }
        }

        return min
    }

    override fun checkCollision(circle: Circle): Double? {
        // Step 1: Determine what tiles it spans
        // Step 2: Get all things in the tile
        // Step 3: Perform Collision
        val left = circle.x - circle.r
        val right = circle.x + circle.r
        val bottom = circle.y - circle.r
        val top = circle.y + circle.r

        var min : Double? = null

        for( x in (left / tileWidth).floor..(right / tileWidth).floor) {
            for(y in (bottom / tileHeight).floor..(top / tileHeight).floor){
                tile[Pair(x,y)]?.forEach {min = MathUtil.minOrNull(min, circle intersectsPrecise it)}
            }
        }
        return min
    }

    fun drawStageSpace(res: IResourceLoadingSystem) : Sequence<DrawContract> {
        return stagePieces.asSequence()
            .flatMap { it.draw(res) }
    }
}