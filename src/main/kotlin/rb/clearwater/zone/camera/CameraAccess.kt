package rb.clearwater.zone.camera

import rb.vectrix.linear.ITransform

interface ICameraAccess
{
    fun setDimensions(w: Int, h: Int)
    fun declareStar(x: Double, y: Double, ox: Double, oy: Double)
}

// Treats cameraState as mutable
class CameraAccess(
    val cameraState: CameraState)  : ICameraAccess
{
    override fun setDimensions(w: Int, h: Int) {
        cameraState.screenW = w
        cameraState.screenH = h
    }

    override fun declareStar(x: Double, y: Double, ox: Double, oy: Double) {
        cameraState.focusX = x
        cameraState.focusY = y
        cameraState.targetOffsetX = ox
        cameraState.targetOffsetY = oy
    }
}
