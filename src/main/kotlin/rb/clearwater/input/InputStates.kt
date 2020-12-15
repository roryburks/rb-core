package rb.clearwater.input

abstract class InputState<T> (
    val raw: Int,
    val map: Map<T,Int> )
{
    fun pressing(t: T) : Boolean{
        val n = map[t] ?: error("No map for $t")
        return (raw shr n) and 1 == 1
    }

    override fun hashCode(): Int {
        var result = raw
        result = 31 * result + map.hashCode()
        return result
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is InputState<*>) return false

        if( raw != other.raw) return false
        if (map != other.map) return false

        return true
    }
}

class GameInputState(raw: Int) : InputState<GameKey>(raw, GameKey.Map)
class MetaInputState(raw: Int) : InputState<MetaKey>(raw, MetaKey.Map)
class SystemInputState(raw: Int) : InputState<SystemKey> (raw, SystemKey.Map)
