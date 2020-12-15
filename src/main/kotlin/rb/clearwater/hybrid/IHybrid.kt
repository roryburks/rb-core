package rb.clearwater.hybrid

import rb.animo.io.IAafScope
import rb.animo.io.ILoader
import rb.global.ILogger
import rb.glow.gle.IGLEngine

interface IHybrid
{
    val logger: ILogger
    val systemTime: ISystemTime
    val aafLoader: ILoader<IAafScope>
    val gle: IGLEngine
}