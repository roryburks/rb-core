//package rb.clearwater.zone.actors
//
//import rb.animo.DrawContract
//import rb.clearwater.collision.hitboxes.AHurtbox
//import rb.clearwater.collision.hitboxes.MHitboxEngine
//import rb.clearwater.input.tombstone.IInputSystem
//import rb.clearwater.zone.Tombstone_IZoneAccess
//import rb.clearwater.zone.Tombstone_IZoneAccessBase
//
//class ActorContainer<T>(
//        val actor: rb.clearwater.zone.actors.IActor<T>,
//        startingState: T,
//        val uid :Int) where T : rb.clearwater.zone.actors.IActorState<T>
//{
//    private var hitboxes: MutableList<AHurtbox<T>>? = null
//    var currentState : T = startingState ; private set
//    var isDead: Boolean = false ; private set
//
//    fun step(input: IInputSystem, zone: Tombstone_IZoneAccessBase) {
//        TODO("Obsolete")
//        //actor.step(input, currentState, ActorZoneAccess(zone))
//    }
//    fun draw() : Sequence<DrawContract> = actor.draw(currentState)
//    fun resetHitboxes() {hitboxes = null}
//
//    fun doHitboxThing(zone : Tombstone_IZoneAccessBase, hitboxEngine: MHitboxEngine) {
//        hitboxes?.forEach {hurtbox ->
//            hurtbox.start(currentState, zone, uid)
//
//            hitboxEngine.hitboxes.asSequence()
//                    .filter { it.supportsHurtbox(hurtbox) }
//                    .filter { it.collision intersects hurtbox.collision }
//                    .forEach { it.onCollision(hurtbox, uid, zone)}
//        }
//    }
//
//    inner class ActorZoneAccess(zone: Tombstone_IZoneAccessBase) : Tombstone_IZoneAccess<T>, Tombstone_IZoneAccessBase by zone {
//
//        override fun die() {
//            isDead = true
//            hitboxes = null
//        }
//
//        override fun addHurtbox(hurtbox: AHurtbox<T>) {
//            if( !isDead) hitboxes = hitboxes?.also { it.add(hurtbox) } ?: mutableListOf(hurtbox)
//        }
//        override val thisUid: Int get() = this@ActorContainer.uid
//    }
//}