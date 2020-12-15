package rb.clearwater.dialog

class DialogSystem : IDialogSystem, IDialogAccess{
    override var dialog: IDialog? = null ; private set
    private var _settingDialog : IDialog? = null

    override fun setDialog(dialog: IDialog): Boolean {
        if( _settingDialog == null) {
            _settingDialog = dialog
            this.dialog = dialog
            return true
        }
        return false
    }

    override fun tickStart() { _settingDialog = null }
    override fun resetDialog() { dialog = null }
}

