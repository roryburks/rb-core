package rb.glow

import rb.glow.gle.IGLEngine
import rb.glow.img.IImageLoader

object GLowDependencySet {
    lateinit var imageLoader: Lazy<IImageLoader>
    lateinit var gle: Lazy<IGLEngine>
}