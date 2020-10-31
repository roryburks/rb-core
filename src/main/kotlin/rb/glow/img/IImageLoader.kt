package rb.glow.img

import rb.glow.gl.GLImage

interface IImageLoader {
    fun loadImageGl( filename: String) : GLImage
}