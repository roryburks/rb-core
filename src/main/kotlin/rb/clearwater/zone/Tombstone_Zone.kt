//package rb.clearwater.zone
//
//import hybrid.Hybrid
//import rb.animo.DrawContract
//import rb.animo.loading.AnimationLoadingSystem
//import rb.animo.loading.IAnimationLoadingSystem
//import rb.clearwater.collision.alerting.AlertingEngine
//import rb.clearwater.collision.alerting.IAlertingEngine
//import rb.clearwater.collision.hitboxes.AHurtbox
//import rb.clearwater.collision.hitboxes.HitboxEngine
//import rb.clearwater.collision.hitboxes.IHitbox
//import rb.clearwater.collision.hitboxes.MHitboxEngine
//import rb.clearwater.input.tombstone.IInputSystem
//import rb.clearwater.zone.actors.ActorEngine
//import rb.clearwater.zone.actors.IActorEngine
//import rb.clearwater.zone.actors.MActorEngine
//import rb.clearwater.zone.particle.IParticleAccess
//import rb.clearwater.zone.particle.IParticleSpace
//import rb.clearwater.zone.particle.ParticleSpace
//import rb.clearwater.zone.projectile.IProjectileSpace
//import rb.clearwater.zone.projectile.ProjectileSpace
//import rb.clearwater.zone.stagePieces.IStagePieceCollection
//import rb.clearwater.zone.stagePieces.StagePiece
//import rb.clearwater.zone.stagePieces.StagePieceCollection
//import rb.glow.ColorARGB32Normal
//import rb.glow.IGraphicsContext
//import rb.glow.MTextPlacer
//import rb.vectrix.linear.ITransform
//import rb.vectrix.linear.ImmutableTransformD
//import rb.vectrix.linear.Vec2i
//import rb.vectrix.shapes.RectD
//
//
//interface Tombstone_IZoneAccessBase
//{
//    //val dialogSystem: IDialogSystem
//    val actorEngine: IActorEngine
//    val collisionSpace : rb.clearwater.collision.ICollisionSpace
//    val particleSpace: IParticleAccess
//    val projectileSpace : IProjectileSpace
//    val alertingEngine: IAlertingEngine
//    val animationSystem : IAnimationLoadingSystem
//
//    val met: Int
//    val nextUid: Int
//    val tickRate: Double
//    fun addHitbox(hitbox: IHitbox)
//}
//interface Tombstone_IZoneAccess<T: rb.clearwater.zone.actors.IActorState<T>> : Tombstone_IZoneAccessBase {
//    fun addHurtbox(hurtbox : AHurtbox<T>)
//    fun die()
//    val thisUid: Int
//}
//
//interface Tombstone_IZone : Tombstone_IZoneAccessBase
//{
//    val hitboxEngine : MHitboxEngine
//    fun step( input: IInputSystem)
//    fun draw( gc: IGraphicsContext)
//    fun addPieces( pieces: List<StagePiece>)
//
//    override val particleSpace: IParticleSpace
//    override val actorEngine : MActorEngine
//}
//
//typealias UID = Int
//interface IUidGenerator {
//    val nextUid: UID
//}
//class UidGenerator  : IUidGenerator {
//    override var nextUid: UID = 0 ; get() = field++; private set
//}
//
//class Tombstone_Zone(
//    val textPlacer: MTextPlacer,
//    val screenDims: Vec2i,
//    override val tickRate: Double = 60.0,
//    val uidGenerator : IUidGenerator = UidGenerator()
//) : Tombstone_IZone, Tombstone_IZoneAccessBase
//{
//    // Major Components
//    //val camera = Camera(screenDims)
//
//    //override val dialogSystem: IDialogSystem = DialogSystem()
//    override val collisionSpace: rb.clearwater.collision.ICollisionSpace get() = _collisionSpace
//    val _collisionSpace = rb.clearwater.collision.CollisionSpace(250f, 250f)
//
//    override val animationSystem: IAnimationLoadingSystem = AnimationLoadingSystem(Hybrid.aafLoader)
//    override val actorEngine: MActorEngine = ActorEngine(uidGenerator, animationSystem)
//    override val particleSpace: IParticleSpace = ParticleSpace()
//    override val projectileSpace: IProjectileSpace = ProjectileSpace()
//    override val alertingEngine: IAlertingEngine = AlertingEngine(_collisionSpace)
//    override val hitboxEngine: MHitboxEngine = HitboxEngine()
//    override fun addHitbox(hitbox: IHitbox)
//    {hitboxEngine.hitboxes.add(hitbox)}
//
//    override var met: Int = 0; private set
//    override val nextUid: UID get() = uidGenerator.nextUid
//    //val hud = Hud(screenDims)
//
//    override fun step( input: IInputSystem) {
////        if( dialogSystem.isDialoging)
////        {
////            dialogSystem.step(this, input)
////        }
////        else {
//            alertingEngine.reset()
//            actorEngine.step(this, hitboxEngine, input)
//
//            //camera.step(this)
//            particleSpace.step(this)
//            met++
//        //}
//    }
//    override fun draw( gc: IGraphicsContext) {
//        // TODO: figure out why
//        gc.clear(ColorARGB32Normal(0x4286f4))
//        gc.color = ColorARGB32Normal(0x4286f4)
//        gc.fillRect(0f,0f,2000f,2000f)
//        textPlacer.reset()
//        gc.transform = ITransform.Identity
//        //gc.transform = camera.cameraTransform
//
//        val list = mutableListOf<DrawContract>()
//
//        // TODO: Make this actually crop to screen
//        pieceCollection.getPiecesForArea(RectD(0.0,0.0,1.0,1.0))
//            .forEach {list.addAll(it.draw())}
//        list.addAll(actorEngine.draw())
//        list.addAll(particleSpace.draw())
//
//        var m = 0
//        list.sortWith(compareBy { -it.depth })
////        list.sortWith(compareBy({-it.depth}, {-(m++)}))
//        list.forEach {
//            if( it.worldTransform != null) {
//                gc.pushTransform()
//                gc.transform = gc.transform * it.worldTransform
//            }
//
//            it.drawRubrick(gc)
//
//            if( it.worldTransform != null) {
//                gc.popTransform()
//            }
//        }
//
//        gc.transform = ImmutableTransformD.Identity
//
////        hud.draw(gc, textPlacer)
////        if( dialogSystem.isDialoging)
////            dialogSystem.draw(gc)
//    }
//
//    override fun addPieces(pieces: List<StagePiece>) {
//        pieceCollection.addPieces(pieces)
//        _collisionSpace.addCollisionObjects(pieces.mapNotNull { it.collision })
//    }
//    private val pieceCollection: IStagePieceCollection = StagePieceCollection()
//}