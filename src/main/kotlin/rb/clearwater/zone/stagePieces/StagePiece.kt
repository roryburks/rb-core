package rb.clearwater.zone.stagePieces

import rb.animo.DrawContract
import rb.extendo.dataStructures.SinglySequence
import rb.glow.Colors
import rb.glow.IGraphicsContext
import rb.glow.drawer
import rb.vectrix.intersect.CollisionObject
import rb.vectrix.intersect.CollisionPolygon
import rb.vectrix.intersect.CollisionRigidRect
import rb.vectrix.linear.Vec2
import rb.vectrix.shapes.PolygonD
import rb.vectrix.shapes.Rect

abstract class StagePiece(
        val bounds: Rect,
        val collision: CollisionObject?)
{
    abstract fun draw() : Sequence<DrawContract>
}

abstract  class StagePieceSimple(bounds: Rect, collision: CollisionObject?) : StagePiece(bounds, collision)
{
    override fun draw(): Sequence<DrawContract> = SinglySequence(DrawContract(drawDepth) {draw(it)})

    abstract val drawDepth: Int
    abstract fun draw(gc: IGraphicsContext)
}

class RectPiece(bounds: Rect, override val drawDepth: Int = 0) : StagePieceSimple(bounds, CollisionRigidRect(bounds))
{
    override fun draw(gc: IGraphicsContext) {
        gc.color = Colors.BLACK
        gc.drawer.fillRect(bounds)
    }
}

class PolyStagePiece(val polygon: CollisionPolygon, override val drawDepth: Int = 0) : StagePieceSimple(polygon.bounds, polygon)
{
    override fun draw(gc: IGraphicsContext) {
        gc.color = Colors.DARK_GRAY
        gc.drawer.fillPolygon(polygon.polygon)
    }

    companion object {
        fun Make(points: List<Vec2>, drawDepth: Int = 0) = PolyStagePiece(CollisionPolygon(PolygonD.Make(points)), drawDepth)
    }
}