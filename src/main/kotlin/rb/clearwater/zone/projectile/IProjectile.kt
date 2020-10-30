package rb.clearwater.zone.projectile



interface IProjectileSpace
{
    fun registerHit( projectileUID: Int, hitUID: Int)
    fun hasBeenHit( projectileUID: Int, hitUID: Int) : Boolean
}

class ProjectileSpace : IProjectileSpace {
    private val hits = HashSet<Proj>()

    override fun registerHit(projectileUID: Int, hitUID: Int) {
        hits.add(Proj(projectileUID, hitUID))
    }

    override fun hasBeenHit(projectileUID: Int, hitUID: Int) = hits.contains(Proj(projectileUID, hitUID))

    private data class Proj(val uid1: Int, val uid2: Int)


}