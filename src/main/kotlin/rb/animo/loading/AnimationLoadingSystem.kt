package rb.animo.loading


import rb.global.IContract
import rb.animo.AnimoDependencySet
import rb.animo.animation.NilAnimation
import rb.animo.io.IAafLoader
import rb.animo.io.IAafScope
import rb.animo.io.NilAafScope
import rb.extendo.extensions.append
import rb.extendo.extensions.deref

class AafScopeHandle(private val _scope: IAafScope) {
    operator fun get(anim: String) = _scope.animations[anim] ?: NilAnimation
}

interface IAnimationLoadingSystem {
    operator fun get(scope: String) : AafScopeHandle
    fun declareAnim(location: String) : IContract

    val allLoaded: Boolean
}

class AnimationLoadingSystem(
    private val _loader: IAafLoader)
    : IAnimationLoadingSystem
{
    private val _registrationMap = mutableMapOf<String, MutableList<Contract>>()
    private val _scopeMap = mutableMapOf<String, IAafScope>()
    private val _stillLoading = mutableSetOf<String>()


    override val allLoaded: Boolean get() = _stillLoading.isEmpty()

    override fun get(scope: String) =  AafScopeHandle(_scopeMap[scope] ?: NilAafScope)

    override fun declareAnim(location: String): IContract {
        _stillLoading.add(location)
        _loader.loadAaf(
            location,
            {
                _stillLoading.remove(location)
                _scopeMap[location] = it
                println("Successfully Loaded $location")
            },
            {
                _stillLoading.remove(location)
                println("Failed to Load $location")
            })

        return  Contract(location)
    }


    private inner class Contract(val string: String) : IContract {
        init { _registrationMap.append(string, this) }

        override fun void() {_registrationMap.deref(string, this)}
    }
}

object AnimationLoaderSystemProvider {
    val System by lazy {  AnimationLoadingSystem( AnimoDependencySet.AafLoader.value)}
}