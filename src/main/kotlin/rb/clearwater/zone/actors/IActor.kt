package rb.clearwater.zone.actors

import rb.animo.DrawContract
import rb.clearwater.input.IGameInputAccess
import rb.clearwater.zone.base.IZoneAccess
import rb.clearwater.zone.base.IZoneAccessBase

interface IActorState<T : IActorState<T>>
{
    // For simplicity's sake let's just make all Actors declare a coordinate set, even if it's not relevant for a very small number
    val cx: Double
    val cy: Double
    fun dupe() : T
}

object BlankState : IActorState<BlankState> {
    override val cx: Double get() = 0.0
    override val cy: Double get() = 0.0
    override fun dupe() = this
}


interface IActor<State> where State : IActorState<State>
{
    val requiredScopes: Set<String> get() = emptySet()
    fun onAdd(state: State, zone: IZoneAccessBase) {}
    fun step(input: IGameInputAccess, state: State, zone: IZoneAccess<State>)
    fun draw(state: State) : Sequence<DrawContract>
}