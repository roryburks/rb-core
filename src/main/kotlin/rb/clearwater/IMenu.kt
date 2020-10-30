package rb.clearwater

import rb.clearwater.input.InputSnapshot
import rb.clearwater.input.SystemInputState
import rb.clearwater.input.SystemKey
import rb.clearwater.zone.base.IZoneAccessBase
import rb.glow.IGraphicsContext

interface IMenu {
    fun step( /*zone: IZoneAccessBase,*/ input: InputSnapshot<SystemKey>) : Boolean
    fun draw( gc: IGraphicsContext)
}