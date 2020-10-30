package rb.clearwater.zone.stagePieces

import rb.vectrix.shapes.Rect

interface IStagePieceCollection
{
    fun addPieces( pieces: List<StagePiece>)

    fun getPiecesForArea( bounds: Rect) : List<StagePiece>
}

class StagePieceCollection : IStagePieceCollection
{
    private val stagePieces = mutableListOf<StagePiece>()

    override fun addPieces( pieces: List<StagePiece>) {
        stagePieces.addAll(pieces)
    }

    override fun getPiecesForArea(bounds: Rect) : List<StagePiece> {
        return stagePieces
    }
}