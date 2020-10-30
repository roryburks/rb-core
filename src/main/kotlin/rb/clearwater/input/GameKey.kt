package rb.clearwater.input

interface InputKey // I'm not sure this adds anything

enum class GameKey : InputKey {
    Up, Down, Left, Right,

    Jump, Shoot, Dodge, Charge;

    companion object {
        val Map =  values()
            .map { Pair(it, it.ordinal) }
            .toMap()
    }
}

enum class MetaKey : InputKey {
    L1, R1, L2, R2;

    companion object {
        val Map =  values()
            .map { Pair(it, it.ordinal) }
            .toMap()
    }
}

enum class SystemKey : InputKey{
    Up, Down, Left, Right,
    Select, Back,
    Menu;

    companion object{
        val Map =  values()
            .map { Pair(it, it.ordinal) }
            .toMap()
    }
}