package rb.clearwater.input.controller

interface IRawControllerOutputFeed {
    fun tick() : List<InputDatum>
}