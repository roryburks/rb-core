package rb.clearwater.input

interface IGameInputAccess {
    fun isPressing( key: GameKey) : Boolean
    fun pressed( key: GameKey) : Boolean
    fun released( key: GameKey) : Boolean

    fun xorPressing(key: GameKey) = when(key) {
        GameKey.Up -> isPressing(GameKey.Up) && !isPressing(GameKey.Down)
        GameKey.Down -> isPressing(GameKey.Down) && !isPressing(GameKey.Up)
        GameKey.Left -> isPressing(GameKey.Left) && !isPressing(GameKey.Right)
        GameKey.Right -> isPressing(GameKey.Right) && !isPressing(GameKey.Left)
        else -> isPressing(key)
    }

    fun GetSnapshot( relTicks: Int) : IGameInputSnapshot
}

class GameInputAccess(
    val previousInputs: List<GameInputState>,
    val currentInput: GameInputState) : IGameInputAccess
{
    private val _snapshot = GameInputSnapshot(previousInputs.lastOrNull() ?: GameInputState(0), currentInput)

    override fun isPressing(key: GameKey) = _snapshot.isPressing(key)
    override fun pressed(key: GameKey) = _snapshot.pressed(key)
    override fun released(key: GameKey) = _snapshot.pressed(key)

    override fun GetSnapshot(relTicks: Int): IGameInputSnapshot {
        if( relTicks == 0)
            return _snapshot
        val first = previousInputs.getOrNull(previousInputs.size + relTicks - 1) ?: GameInputState(0)
        val second = previousInputs.getOrNull(previousInputs.size + relTicks) ?: GameInputState(0)
        return GameInputSnapshot(first, second)
    }

}
