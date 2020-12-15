package rb.clearwater.differentialEngine

import rb.clearwater.zone.actors.IActor
import rb.clearwater.zone.actors.IActorAccess
import rb.clearwater.zone.actors.IActorState
import kotlin.reflect.KClass

class DeActorAccess ( actors: List<SublimeActor<*>>) : IActorAccess {
    private val _actorMap = actors
        .map { Pair(it.mid, it.state) }
        .toMap()

    private var _mit = actors.asSequence().map { it.mid }.max() ?: 0

    val newActorList = mutableListOf<ActorK<*>>()

    override fun getState(mid: Int): IActorState<*>? = _actorMap[mid]

    override fun getByClass(c: KClass<*>) : IActorState<*>?{
        return _actorMap
            .values
            .firstOrNull { it::class == c }
    }


    override fun <T : IActorState<T>> addActor(actor: IActor<T>, state: T) {
        newActorList.add(ActorK(actor, state, _mit++))
    }

    override val actors: List<IActorState<*>> get() = _actorMap.values.toList()
}
