package rb.clearwater.resources

import rb.animo.AnimoDependencySet
import rb.animo.io.ILoader
import rb.global.ILogger
import rb.glow.img.IImage
import rb.glow.img.NillImage

interface IImageResourcePool {
    fun get( id: String) : IImage
    fun put( id: String, img: IImage)
    fun load(id: String)
    fun isLoading() : Boolean
}

class ImageResourcePool(
    private val _loader : ILoader<IImage>,
    private val _logger : ILogger
) : IImageResourcePool {
    private val _map = mutableMapOf<String,IImage>()
    private val _midLoad = HashSet<String>()

    override fun get(id: String) = _map[id] ?: NillImage

    override fun put(id: String, img: IImage) { _map[id] = img }

    override fun load(id: String) {
        if( !_map.containsKey(id) && !_midLoad.contains(id)) {
            _midLoad.add(id)
            _loader.load(id,
                {_map[id] = it; _midLoad.remove(id) ; _logger.logInformation("Loaded Image $id")},
                {_midLoad.remove(id) ; _logger.logError("Failed to load Image $id", it)})
        }
    }

    override fun isLoading() = !_midLoad.isEmpty()
}

object ImageResourcePoolProvider {
    val Pool = lazy { ImageResourcePool(
        AnimoDependencySet.ImageLoader.value,
        AnimoDependencySet.Logger.value) }
}