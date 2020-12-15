package rb.clearwater.zone.particle

import rb.animo.DrawContract
import rb.clearwater.zone.base.IZoneAccessBase
import rb.clearwater.zone.particle.IParticle.IParticleContract


interface IParticleSpace : IParticleAccess{
    fun step(zone: IZoneAccessBase)
    fun draw() : List<DrawContract>
}

class ParticleSpace : IParticleSpace {
    private val particles = mutableListOf<ParticleContainer>()

    override fun addParticle(particle: IParticle) {
        particles.add(ParticleContainer(particle))
    }

    override fun step(zone: IZoneAccessBase) {
        _toDie.clear()
        particles.forEach { it.particle.step(it, zone) }
        particles.removeIf { _toDie.contains(it.particle) }
    }

    override fun draw() = mutableListOf<DrawContract>()
            .also{list -> particles.forEach { list.addAll(it.particle.draw()) }}

    private val _toDie = mutableListOf<IParticle>()
    private inner class ParticleContainer(val particle: IParticle): IParticleContract {
        override fun die() {_toDie.add(particle)}
    }
}