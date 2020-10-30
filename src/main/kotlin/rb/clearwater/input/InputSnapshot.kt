package rb.clearwater.input

interface IInputSnapshot<T> {
    fun isPressing( key: T) : Boolean
    fun pressed( key: T) : Boolean
    fun released( key: T) : Boolean
}


interface IGameInputSnapshot : IInputSnapshot<GameKey>  {
}
//object NilGameInputAccess : IGameInputAccess {
//    override fun isPressing(key: GameKey) = false
//    override fun pressed(key: GameKey) = false
//    override fun released(key: GameKey) = false
//}

interface IMetaInputSnapshot : IInputSnapshot<MetaKey>{ }

interface ISystemInputSnapshot : IInputSnapshot<SystemKey>{ }

open class InputSnapshot<T>(
    val prev: InputState<T>,
    val now: InputState<T>) : IInputSnapshot<T>
{
    override fun isPressing(key: T) = now.pressing(key)
    override fun pressed(key: T) = now.pressing(key) && !prev.pressing(key)
    override fun released(key: T) = !now.pressing(key) && prev.pressing(key)
}


class GameInputSnapshot(
    prev: InputState<GameKey>,
    next: InputState<GameKey>) : InputSnapshot<GameKey>(prev,next), IGameInputSnapshot {}

