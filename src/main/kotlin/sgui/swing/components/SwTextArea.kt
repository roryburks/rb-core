package sgui.swing.components

import rb.owl.bindable.Bindable
import rb.owl.bindable.addObserver
import sgui.core.components.ITextArea
import sgui.core.systems.KeypressSystem
import sgui.swing.mouseSystem.adaptMouseSystem
import sgui.swing.skin.Skin.BevelBorder.Dark
import sgui.swing.skin.Skin.BevelBorder.Light
import sgui.swing.skin.Skin.TextField.Background
import sguiSwing.components.ISwComponent
import sguiSwing.components.SwComponent
import java.awt.event.FocusEvent
import java.awt.event.FocusListener
import javax.swing.BorderFactory
import javax.swing.JScrollPane
import javax.swing.JTextArea
import javax.swing.border.BevelBorder
import javax.swing.event.DocumentEvent
import javax.swing.event.DocumentListener


class SwTextArea
private constructor(private val imp : SwTextAreaImp) : ITextArea, ISwComponent by SwComponent(imp)
{
    // TODO: Re-implement TextBind -> UI binding
    constructor() : this(SwTextAreaImp())

    override val textBind = Bindable("")
    override var text by textBind

    private var textToBeSetToDocument : String? = null

    fun update() {
        text = imp.textArea.text
    }

    init {
        imp.textArea.document.addDocumentListener(object: DocumentListener {
            override fun changedUpdate(e: DocumentEvent?) {update()}
            override fun insertUpdate(e: DocumentEvent?) {update()}
            override fun removeUpdate(e: DocumentEvent?) {update()}
        })
        textBind.addObserver { _, _ ->
                //imp.textArea.text = new
        }

        imp.textArea.addFocusListener(object : FocusListener {
            override fun focusLost(e: FocusEvent) {
                KeypressSystem.hotkeysEnabled = true
            }
            override fun focusGained(e: FocusEvent?) {
                KeypressSystem.hotkeysEnabled = false
            }
        })
    }


    private class SwTextAreaImp(val textArea :JTextArea = JTextArea()) : JScrollPane(textArea)
    {
        init {
            adaptMouseSystem()
            textArea.background = Background.jcolor
            textArea.border = BorderFactory.createBevelBorder(BevelBorder.LOWERED, Light.jcolor, Dark.jcolor)
        }
    }
}