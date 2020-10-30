package rb.clearwater.hybrid

import rb.animo.io.IAafLoader
import rb.glow.gle.IGLEngine

interface IHybrid
{
    val logger: ILogger
    val systemTime: ISystemTime
    val aafLoader: IAafLoader
    val gle: IGLEngine
}