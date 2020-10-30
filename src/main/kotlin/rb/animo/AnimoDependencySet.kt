package rb.animo

import rb.animo.io.IAafLoader

object AnimoDependencySet {
    lateinit var AafLoader: Lazy<IAafLoader>
}