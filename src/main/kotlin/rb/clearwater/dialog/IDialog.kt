package rb.clearwater.dialog

import rb.clearwater.resources.IAnimationLoadingSystem
import rb.clearwater.input.GameKey
import rb.clearwater.input.InputSnapshot
import rb.clearwater.input.SystemKey
import rb.glow.IGraphicsContext

// Pass 1 will work like this:
// IDialogAccess will expose the IDialogSystem, allowing any Game Object to inject (or attempt to inject) an IDialog int
// the System.  If the Meta recognizes an IDialog in the system, it will go into Dialog state wherein the IDialog has
// control.  (Note, p1, the IDialog only has access to input.  in pX, it'll have access to the WorldState)

interface IDialog {
    fun step( systemInput: InputSnapshot<SystemKey>, gameInput: InputSnapshot<GameKey> ) : Boolean
    fun draw( gc: IGraphicsContext, anim: IAnimationLoadingSystem)
}

interface IDialogAccess {
    // returns true if inject successful
    fun setDialog(dialog: IDialog) : Boolean
}

interface IDialogSystem : IDialogAccess{
    val dialog: IDialog?

    // signifies the start of a tick
    fun tickStart()
    fun resetDialog()
}


object DialogSystemProvider {
    var System = lazy { DialogSystem() }
}
