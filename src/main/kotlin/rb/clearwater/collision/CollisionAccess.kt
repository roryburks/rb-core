package rb.clearwater.collision

import rb.vectrix.intersect.CollisionObject
import rb.vectrix.shapes.Arc
import rb.vectrix.shapes.Circle
import rb.vectrix.shapes.LineSegment
import rb.vectrix.shapes.RayRect

interface ICollisionAccess
{
    // Note: Greedy, so more efficient if you only want the Bool
    fun checkCollision(collisionObject: CollisionObject) : Boolean

    fun checkCollision( rect: RayRect) : Double?
    fun checkCollision( line: LineSegment) : Double?
    fun checkCollision( circle: Circle) : Double?
    fun checkCollision( arc: Arc) : Double?
}

//Note: The intent here is to have CollisionAccess aggregate both StageSpace which is immutable and an Actor-based
// Hard Collision System.
class CollisionAccess ( private val _space: StageSpace) : ICollisionAccess
{
    override fun checkCollision(collisionObject: CollisionObject) = _space.checkCollision(collisionObject)
    override fun checkCollision(rect: RayRect) = _space.checkCollision(rect)
    override fun checkCollision(line: LineSegment) = _space.checkCollision(line)
    override fun checkCollision(circle: Circle) = _space.checkCollision(circle)
    override fun checkCollision(arc: Arc) = _space.checkCollision(arc)
}