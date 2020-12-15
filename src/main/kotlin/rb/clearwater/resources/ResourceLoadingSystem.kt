package rb.clearwater.resources

interface IResourceLoadingSystem {
    val imgPool: IImageResourcePool
    val anim: IAnimationLoadingSystem

    fun isLoading() : Boolean
}

class ResourceLoadingSystem(
    override val imgPool: IImageResourcePool,
    override val anim: IAnimationLoadingSystem
) : IResourceLoadingSystem
{
    override fun isLoading() = !anim.allLoaded || imgPool.isLoading()
}


object ResourceLoadingSystemProvider {
    val System by lazy { ResourceLoadingSystem(
        ImageResourcePoolProvider.Pool.value,
        AnimationLoaderSystemProvider.System
    ) }
}