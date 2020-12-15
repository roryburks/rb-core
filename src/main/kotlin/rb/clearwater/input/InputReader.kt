package rb.clearwater.input

import rb.vectrix.mathUtil.b
import rb.vectrix.mathUtil.f
import rb.vectrix.mathUtil.i
import kotlin.math.min

interface IInputReader {
    fun tick() : Triple<GameInputState,MetaInputState,SystemInputState>
}

interface IInputWriter {
    fun pipeRawPress(meta: InputKey)
    fun pipeRawRelease(system: InputKey)
    fun pipeClear()
}

interface IInputReadWrite : IInputReader, IInputWriter

open class InputReader : IInputReadWrite  {
    val game = SubInputReader(GameKey.Map)
    val meta = SubInputReader(MetaKey.Map)
    val system = SubInputReader(SystemKey.Map)

    override fun tick(): Triple<GameInputState, MetaInputState, SystemInputState> {
        val g = GameInputState(game.getRaw())
        val m = MetaInputState(meta.getRaw())
        val s = SystemInputState(system.getRaw())
        game.tick()
        meta.tick()
        system.tick()
        return Triple(g,m,s)
    }

    override fun pipeRawPress(key: InputKey) {
        (key as? MetaKey)?.also { this.meta.pipeRawPress(it) }
        (key as? GameKey)?.also { this.game.pipeRawPress(it) }
        (key as? SystemKey)?.also { this.system.pipeRawPress(it) }
    }

    override fun pipeRawRelease(key: InputKey) {
        (key as? MetaKey)?.also { this.meta.pipeRawRelease(it) }
        (key as? GameKey)?.also { this.game.pipeRawRelease(it) }
        (key as? SystemKey)?.also { this.system.pipeRawRelease(it) }
    }

    override fun pipeClear() {
        meta.pipeClear()
        game.pipeClear()
        system.pipeClear()
    }
}

class SubInputReader<T>(val map: Map<T,Int>)
{
    // Each Key has a byte
    // 0 bit = has had a press event happen
    // 1 bit = has a release event happen
    // 2 bit = whether it was in a press state last tick
    // 3 bit set = most recent event was a press
    // 4 bit set = most recent event was a release
    val clearRecents = 0b1110_0111
    val setPress = 0b1000
    val setRelease = 0b10000
    val substateArray: ByteArray
    init {
        val len = map.map { it.value +1 }.max() ?: 0
        substateArray = ByteArray(len) {0}
    }

    fun pipeRawPress(t: T)
    {
        val i = map[t] ?: error("Bad $t")
        var cur = substateArray[i].i
        cur = cur or 1
        cur = cur and clearRecents
        cur = cur or setPress
        substateArray[i] = cur.b
    }
    fun pipeRawRelease(t: T)
    {
        val i = map[t] ?: error("Bad $t")
        var cur = substateArray[i].i
        cur = cur or 2
        cur = cur and clearRecents
        cur = cur or setRelease
        substateArray[i] = cur.b
    }
    fun pipeClear()
    {
        for (i in substateArray.indices) {
            substateArray[i] = 0
        }
    }
    fun tick()
    {
        // logic:
        // everything but 2bit is cleared.
        // 2bit is:
        //   true if 2bit was true before AND 4bit isn't true
        //   true if 3bit is true
        //   false otherwise
        for (i in substateArray.indices) {
            val prev = substateArray[i].i
            substateArray[i] = 0
            if( prev and 0b100 != 0 && prev and setRelease == 0 )
                substateArray[i] = 4
            if( prev and setPress != 0)
                substateArray[i] = 4
        }
    }

    fun getRaw() : Int {
        // Per bit:
        // if press bit is true and 2-bit is false -> true
        // if 2-bit is true and 4-bit isn't set -> true
        // else false
        var o = 0
        for (i in 0 until min(31, substateArray.size))
        {
            val byte = substateArray[i].i
            val set = ((byte and 0b1 != 0) && (byte and 0b100 == 0))
                    || ((byte and 0b100 != 0) && (byte and 0b10000 == 0))
            o = o or ((if(set) 1 else 0) shl i)
        }
        return o
    }

}

