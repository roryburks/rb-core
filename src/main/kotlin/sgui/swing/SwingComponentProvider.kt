package sgui.swing

import rb.glow.img.IImage
import rb.glow.SColor
import sgui.components.*
import sgui.core.components.crossContainer.CrossInitializer
import sgui.core.components.crossContainer.ICrossPanel
import sgui.core.Orientation
import sgui.core.components.*
import sgui.swing.advancedComponents.SwTreeView
import sgui.swing.components.*

object SwingComponentProvider : IComponentProvider {

    override fun <T:Any> BoxList(boxWidth: Int, boxHeight: Int, entries: Collection<T>? ): IBoxList<T>
        = SwBoxList(boxWidth, boxHeight, entries)

    override fun Button(str: String?) : IButton = SwButton(str)
    override fun CheckBox(): ICheckBox = SwCheckBox()
    override fun RadioButton(label: String, selected: Boolean): IRadioButton = SwRadioButton(label, selected)
    override fun GradientSlider(minValue: Float, maxValue: Float, label: String) : IGradientSlider = SwGradientSlider(minValue, maxValue, label)
    override fun Label(text: String): ILabel = SwLabel(text)
    override fun EditableLabel(text: String): IEditableLabel = SwEditableLabel(text)
    override fun ScrollBar(orientation: Orientation, context: IComponent, minScroll: Int, maxScroll: Int, startScroll: Int, scrollWidth: Int) : IScrollBar
        = SwScrollBar(orientation, context, minScroll, maxScroll, startScroll, scrollWidth)
    override fun ScrollContainer(component: IComponent) = SwScrollContainer(component)

    override fun ToggleButton(startChecked: Boolean): IToggleButton = SwToggleButton(startChecked)
    override fun CrossPanel(constructor: (CrossInitializer.()->Unit)?): ICrossPanel = SwPanel().apply { constructor?.also { setLayout(it) } }
    override fun TabbedPane(): ITabbedPane = SwTabbedPane()
    override fun <T> ComboBox(things: Array<T>): IComboBox<T> = SwComboBox(things)
    override fun <T> TreeView(): ITreeView<T> = SwTreeView()

    override fun TextField(): ITextField = SwTextField()
    override fun IntField(min: Int, max: Int, allowsNegative: Boolean): IIntField = SwIntField(min, max, allowsNegative)
    override fun FloatField( min: Float, max: Float, allowsNegative: Boolean): IFloatField = SwFloatField(min, max, allowsNegative)

    override fun TextArea(): ITextArea = SwTextArea()

    override fun Separator(orientation: Orientation): ISeparator = SwSeparator(orientation)
    override fun ColorSquare(color: SColor): IColorSquare = SwColorSquare(color)

    override fun Slider(min: Int, max: Int, value: Int): ISlider = SwSlider(min, max, value)

    override fun ImageBox(img: IImage?): IImageBox = SwImageBox(img)
}