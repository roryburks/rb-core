package rbJvm.clearwater.input.controller

import com.badlogic.gdx.controllers.Controller
import com.badlogic.gdx.controllers.ControllerListener
import com.badlogic.gdx.controllers.PovDirection
import com.badlogic.gdx.math.Vector3
import org.libsdl.SDL
import rb.clearwater.input.controller.*
import uk.co.electronstudio.sdl2gdx.SDL2ControllerManager

// Things that could be done: Add multi controller support
class SdlControllerAdapter() : IRawControllerOutputFeed{
    val manager: SDL2ControllerManager
    private var inputFeed = mutableListOf<InputDatum>()

    fun map(pov: PovDirection) = when(pov) {
        PovDirection.center -> PovInputState.Neutral
        PovDirection.north -> PovInputState.Up
        PovDirection.northEast -> PovInputState.UpRight
        PovDirection.northWest -> PovInputState.UpLeft
        PovDirection.east -> PovInputState.Right
        PovDirection.south -> PovInputState.Down
        PovDirection.southEast -> PovInputState.DownRight
        PovDirection.southWest -> PovInputState.DownLeft
        PovDirection.west -> PovInputState.Left
    }

    init {
        SDL.SDL_SetHint("SDL_XINPUT_ENABLED","0")
        manager = SDL2ControllerManager()

        manager.addListenerAndRunForConnectedControllers(object : ControllerListener {
            override fun connected(controller: Controller?) { }
            override fun disconnected(controller: Controller?) { }

            override fun buttonUp(controller: Controller?, buttonCode: Int): Boolean {
                inputFeed.add(ButtonInput(buttonCode, false))
                return false
            }

            override fun povMoved(controller: Controller?, povCode: Int, value: PovDirection?): Boolean {
                value?.also {  inputFeed.add(PovInput(povCode, map(it) ))}
                return false
            }

            override fun buttonDown(controller: Controller?, buttonCode: Int): Boolean {
                inputFeed.add(ButtonInput(buttonCode, true))
                return false
            }

            override fun axisMoved(controller: Controller?, axisCode: Int, value: Float): Boolean {
                inputFeed.add(AxisInput(axisCode, value))
                return false
            }

            override fun xSliderMoved(controller: Controller?, sliderCode: Int, value: Boolean): Boolean {
                return false
            }
            override fun ySliderMoved(controller: Controller?, sliderCode: Int, value: Boolean): Boolean {
                return false
            }

            override fun accelerometerMoved(controller: Controller?, accelerometerCode: Int, value: Vector3?): Boolean {
                return false
            }
        })
    }

    override fun tick(): List<InputDatum> {
        manager.pollState()
        val ret = inputFeed
        inputFeed = mutableListOf()
        return ret
    }
}