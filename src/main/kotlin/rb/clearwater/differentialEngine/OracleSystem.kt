package rb.clearwater.differentialEngine

import rb.clearwater.resources.*
import rb.clearwater.dialog.DialogSystemProvider
import rb.clearwater.dialog.IDialogSystem
import rb.clearwater.input.GameInputAccess
import rb.clearwater.input.GameInputState

val MaxInputLookBack = 60

interface IOracleSystem
{
    val met: Int
    fun tickUp(input: GameInputState, defaultToReplay: Boolean = false) : WorldState
    fun tickBack() : WorldState
    fun peekBack(metsBack: Int) : WorldState?

    fun scryForward(input: GameInputState)
    fun getForesight(input: GameInputState, metForward: Int) :WorldState?
}

interface IOracleSystemFactory{
    fun begin(startingWorldState: WorldState) : IOracleSystem
}
class OracleSystemFactory(
    private val _res: IResourceLoadingSystem,
    private val _worldStateProcessor : IWorldStateProcessor,
    private val _dialogSystem : IDialogSystem)
    : IOracleSystemFactory
{
    override fun begin(startingWorldState: WorldState): IOracleSystem = OracleSystem(
        startingWorldState,
        _res,
        _worldStateProcessor)
}

class OracleSystem(
    startingWorldState: WorldState,
    private val _res: IResourceLoadingSystem,
    private val processor : IWorldStateProcessor )
    : IOracleSystem
{
    private class WorldBlob(
        val ws: WorldState,
        val input: GameInputState ) // TODO: Add MPID stuff

    private var canon = mutableListOf<WorldBlob>()
    private val offshoots = mutableMapOf<GameInputState, MutableList<WorldBlob>>()

    override var met: Int = 0 ; private set

    init {
        canon.add(WorldBlob(startingWorldState, GameInputState(0)))
        ++met
    }

    override fun tickUp(input: GameInputState,defaultToReplay: Boolean ): WorldState {
        val predicted = offshoots[input] // Prediction will either append on canon or get dropped
        offshoots.clear()

        // Continue on the unchanged canon if passive
        val nextCanon = canon.getOrNull(met+1)
        if( nextCanon != null && (nextCanon.input == input || (input == GameInputState(0) && defaultToReplay))) {
            ++met
            return nextCanon.ws
        }

        // If the canon is changed, invalidate the future
        if( canon.size > met)
            canon = canon.subList(0,met)

        // Check if we're following a predicted future, if so turn it into the canon
        if( predicted?.any() == true)
        {
            canon.addAll(predicted)
            ++met
            return predicted.first().ws
        }

        // All predictions have been invalidated, continue as normal
        val inputAccess = GameInputAccess(
            getSubInputList(met-1),
            input)
        val newWorldState = processor.tick(canon[met-1].ws, inputAccess, _res)
        canon.add(WorldBlob(newWorldState, input))
        met++
        return newWorldState
    }

    override fun tickBack(): WorldState {
        offshoots.clear()
        if( met <= 1) return canon[met].ws
        return canon[--met].ws
    }

    override fun peekBack(metsBack: Int): WorldState? = canon.getOrNull(met-metsBack)?.ws

    override fun scryForward(input: GameInputState) {
        if( input == GameInputState(0) && canon.size > met){
            // Extend Canon such that it's at least 60 into the future
            // i.e. canon.size = met + 60
            while (canon.size < met + 60)
            {
                val last = canon.last()
                val previousInput = getSubInputList(canon.lastIndex)
                val next = processor.tick(last.ws, GameInputAccess(previousInput, input), _res)
                canon.add(WorldBlob(next, input))
            }
        }
        val existing = offshoots[input]
        if(existing == null){
            val new = mutableListOf<WorldBlob>()
            offshoots[input] = new
            var ws = canon[met-1].ws
            val previousInput = getSubInputList(met-1).toMutableList()
            for( i in 0 until 60){
                ws = processor.tick(ws, GameInputAccess(previousInput, input), _res)
                previousInput.removeAt(0)
                previousInput.add(input)
                new.add(WorldBlob(ws, input))
            }
        }
    }

    override fun getForesight(input: GameInputState, metForward: Int) : WorldState?{
        if( input == GameInputState(0))
        {
            val future = canon.getOrNull(met + metForward)?.ws
            if( future != null) return future
        }

        return offshoots[input]
            ?.getOrNull(metForward-1)
            ?.ws
    }

    private fun getSubInputList(met: Int) : List<GameInputState> {
        if( met < MaxInputLookBack)
            return canon.map { it.input }
        return canon.subList(met - MaxInputLookBack, met)
            .map { it.input }
    }
}

object OracleSystemFactoryProvider {
    val Factory = OracleSystemFactory(
        _res = ResourceLoadingSystemProvider.System,
        _worldStateProcessor = WorldStateProcessorProvider.Processor,
        _dialogSystem = DialogSystemProvider.System.value)
}