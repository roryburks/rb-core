package rbJvm.glow

import rb.glow.gl.GLImage
import rb.glow.gle.IGLEngine
import rb.glow.img.IImageLoader
import rbJvm.glow.awt.ImageBI
import java.io.File
import javax.imageio.ImageIO

class JvmImageLoader(
    private val _gle: IGLEngine
) : IImageLoader {
    override fun loadImageGl(filename: String): GLImage {
        val file = File(filename)
        val imageBi = ImageBI(ImageIO.read(file))
        return _gle.converter.convertToGL(imageBi, _gle)
    }
}