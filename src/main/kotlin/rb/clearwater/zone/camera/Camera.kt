package rb.clearwater.zone.camera

import rb.vectrix.linear.ITransform
import rb.vectrix.linear.ImmutableTransformD


data class CameraState(
    var screenW: Int,
    var screenH: Int,
    var focusX: Double = 0.0,
    var focusY: Double = 0.0,
    var offsetX: Double = 0.0,
    var offsetY: Double = 0.0,
    var targetOffsetX: Double = 0.0,
    var targetOffsetY: Double = 0.0,
    var lockVX : Double = 0.0,
    var lockVY: Double  = 0.0)

interface ICameraProcessor {
    fun tick(state: CameraState)
    fun transformFrom(state: CameraState) : ITransform
}

object CameraProcessor : ICameraProcessor
{
    override fun tick(state: CameraState) {
        // TODO: Gliding logic
    }


    override fun transformFrom(state: CameraState): ITransform {
        //return ImmutableTransformD.Translation(-(centerX + offX ) + w / 2.0, -(centerY + offY) + h / 2.0)
        return ImmutableTransformD.Translation(-(state.focusX ) + state.screenW / 2.0, -(state.focusY ) + state.screenH / 2.0)
    }
}