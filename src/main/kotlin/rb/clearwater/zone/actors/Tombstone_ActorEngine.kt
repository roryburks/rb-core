//package rb.clearwater.zone.actors
//
//import rb.animo.DrawContract
//import rb.animo.loading.IAnimationLoadingSystem
//import rb.clearwater.collision.hitboxes.MHitboxEngine
//import rb.clearwater.input.tombstone.IInputSystem
//import rb.clearwater.zone.IUidGenerator
//import rb.clearwater.zone.Tombstone_IZoneAccessBase
//import rb.clearwater.zone.UID
//
//interface IActorEngine {
//    fun accessState(uid: UID) : IActorState<*>?
//
//
//    fun <T: IActorState<T>> addActor(actor: IActor<T>, startingState: T, zone: Tombstone_IZoneAccessBase)
//}
//
//interface MActorEngine : IActorEngine {
//    fun step(zone: Tombstone_IZoneAccessBase, hitboxEngine: MHitboxEngine, input: IInputSystem)
//    fun draw() : List<DrawContract>
//}
//
//class ActorEngine(
//        val uidGenerator: IUidGenerator,
//        private val _animSystem : IAnimationLoadingSystem)
//    : MActorEngine
//{
//    private val aliveActors = mutableListOf<ActorContainer<*>>()
//    private val deadActors = mutableListOf<ActorContainer<*>>()
//
//    // TODO: Binary Search Tree or Dictionary
//    override fun accessState(uid: UID): IActorState<*>? = aliveActors.firstOrNull { it.uid == uid }?.currentState
//
//    override fun step(zone: Tombstone_IZoneAccessBase, hitboxEngine: MHitboxEngine, input: IInputSystem) {
//        hitboxEngine.hitboxes.clear()
//        aliveActors.forEach {it.resetHitboxes()}
//        aliveActors.toList().forEach { it.step(input, zone)}
//        aliveActors.forEach { it.doHitboxThing(zone, hitboxEngine) }
//        aliveActors.removeAll {
//            val dead = it.isDead
//            if( dead) {
//                deadActors.add(it)
//            }
//            dead
//        }
//
//    }
//
//    override fun <T : IActorState<T>> addActor(actor: IActor<T>, startingState: T, zone: Tombstone_IZoneAccessBase) {
//        actor.requiredScopes.forEach { _animSystem.declareAnim(it) }
//        val container = ActorContainer(actor, startingState, uidGenerator.nextUid)
//        aliveActors.add(container)
//        TODO("Obsolete")
//        //actor.onAdd(container.currentState,  zone)
//    }
//
//    override fun draw() : List<DrawContract> = mutableListOf<DrawContract>()
//                .also { list -> aliveActors.forEach { list.addAll(it.draw()) } }
//}
