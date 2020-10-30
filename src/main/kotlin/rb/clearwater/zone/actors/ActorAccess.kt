package rb.clearwater.zone.actors

import rb.clearwater.differentialEngine.ActorK
import kotlin.reflect.KClass

interface IActorAccess {
    fun getState(mid: Int) : IActorState<*>?
    fun getByClass(c : KClass<*>) : IActorState<*>?
    //fun <T: IActorState<T>> getStates() : List<IActorState<T>>
    fun <T: IActorState<T>> addActor( actor: IActor<T>, state: T)
}

