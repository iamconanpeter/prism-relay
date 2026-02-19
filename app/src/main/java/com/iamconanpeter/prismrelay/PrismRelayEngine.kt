package com.iamconanpeter.prismrelay

enum class CellType { EMPTY, MIRROR_SLASH, MIRROR_BACKSLASH, SOURCE, GOAL }
enum class Dir { UP, RIGHT, DOWN, LEFT }

data class Pos(val r: Int, val c: Int)

data class BeamResult(
    val hitGoal: Boolean,
    val path: List<Pos>,
    val terminatedReason: String,
    val stars: Int?,
    val efficiency: Int
)

data class PuzzleDefinition(
    val id: String,
    val name: String,
    val rows: Int,
    val cols: Int,
    val source: Pos,
    val goal: Pos,
    val parMoves: Int,
    val initialMirrors: Map<Pos, CellType>
)

class PrismRelayEngine(
    puzzleIndex: Int = 0,
    private val undoLimit: Int = 1
) {
    data class RotationAction(val pos: Pos, val oldType: CellType, val newType: CellType)

    private val puzzles = listOf(
        PuzzleDefinition(
            id = "classic-bridge",
            name = "Classic Bridge",
            rows = 5,
            cols = 5,
            source = Pos(2, 0),
            goal = Pos(2, 4),
            parMoves = 3,
            initialMirrors = mapOf(
                Pos(2, 1) to CellType.MIRROR_SLASH,
                Pos(1, 1) to CellType.MIRROR_BACKSLASH,
                Pos(1, 3) to CellType.MIRROR_SLASH,
                Pos(2, 3) to CellType.MIRROR_BACKSLASH,
                Pos(3, 3) to CellType.MIRROR_SLASH
            )
        ),
        PuzzleDefinition(
            id = "zig-zag-core",
            name = "Zig-Zag Core",
            rows = 5,
            cols = 5,
            source = Pos(0, 0),
            goal = Pos(4, 4),
            parMoves = 4,
            initialMirrors = mapOf(
                Pos(0, 2) to CellType.MIRROR_BACKSLASH,
                Pos(1, 2) to CellType.MIRROR_SLASH,
                Pos(2, 2) to CellType.MIRROR_BACKSLASH,
                Pos(3, 2) to CellType.MIRROR_SLASH,
                Pos(4, 2) to CellType.MIRROR_BACKSLASH,
                Pos(2, 4) to CellType.MIRROR_SLASH
            )
        )
    )

    private val selected = puzzles[puzzleIndex.mod(puzzles.size)]
    private val mirrors = selected.initialMirrors.toMutableMap()
    private val history = mutableListOf<RotationAction>()

    private var movesUsed = 0
    private var undosUsed = 0
    private var remainingUndos = undoLimit

    fun puzzleCount(): Int = puzzles.size
    fun puzzleId(): String = selected.id
    fun puzzleName(): String = selected.name
    fun parMoves(): Int = selected.parMoves
    fun movesUsed(): Int = movesUsed
    fun remainingUndos(): Int = remainingUndos

    fun size(): Pair<Int, Int> = selected.rows to selected.cols

    fun getCell(pos: Pos): CellType = when {
        pos == selected.source -> CellType.SOURCE
        pos == selected.goal -> CellType.GOAL
        else -> mirrors[pos] ?: CellType.EMPTY
    }

    fun rotateMirror(pos: Pos): Boolean {
        val current = mirrors[pos] ?: return false
        val next = when (current) {
            CellType.MIRROR_SLASH -> CellType.MIRROR_BACKSLASH
            CellType.MIRROR_BACKSLASH -> CellType.EMPTY
            CellType.EMPTY -> CellType.MIRROR_SLASH
            else -> current
        }

        history += RotationAction(pos, current, next)
        if (next == CellType.EMPTY) {
            mirrors.remove(pos)
        } else {
            mirrors[pos] = next
        }
        movesUsed += 1
        return true
    }

    fun undoLastRotation(): Boolean {
        if (remainingUndos <= 0 || history.isEmpty()) return false
        val action = history.removeAt(history.lastIndex)
        if (action.oldType == CellType.EMPTY) {
            mirrors.remove(action.pos)
        } else {
            mirrors[action.pos] = action.oldType
        }
        movesUsed = maxOf(0, movesUsed - 1)
        undosUsed += 1
        remainingUndos -= 1
        return true
    }

    fun reset() {
        mirrors.clear()
        mirrors.putAll(selected.initialMirrors)
        history.clear()
        movesUsed = 0
        undosUsed = 0
        remainingUndos = undoLimit
    }

    fun evaluate(): BeamResult {
        var pos = selected.source
        var dir = Dir.RIGHT
        val path = mutableListOf(pos)
        val visitedState = mutableSetOf<Pair<Pos, Dir>>()

        while (true) {
            val state = pos to dir
            if (!visitedState.add(state)) {
                return BeamResult(false, path, "Loop detected", null, efficiency())
            }

            val next = step(pos, dir)
            if (!inBounds(next)) {
                return BeamResult(false, path, "Beam exited grid", null, efficiency())
            }

            pos = next
            path += pos

            if (pos == selected.goal) {
                return BeamResult(true, path, "Goal reached", stars(), efficiency())
            }

            when (getCell(pos)) {
                CellType.MIRROR_SLASH -> dir = reflectSlash(dir)
                CellType.MIRROR_BACKSLASH -> dir = reflectBackslash(dir)
                else -> Unit
            }
        }
    }

    private fun stars(): Int = when {
        movesUsed <= selected.parMoves && undosUsed == 0 -> 3
        movesUsed <= selected.parMoves + 2 -> 2
        else -> 1
    }

    private fun efficiency(): Int = maxOf(0, 100 - (movesUsed * 10) - (undosUsed * 15))

    private fun step(pos: Pos, dir: Dir): Pos = when (dir) {
        Dir.UP -> Pos(pos.r - 1, pos.c)
        Dir.RIGHT -> Pos(pos.r, pos.c + 1)
        Dir.DOWN -> Pos(pos.r + 1, pos.c)
        Dir.LEFT -> Pos(pos.r, pos.c - 1)
    }

    private fun inBounds(pos: Pos): Boolean = pos.r in 0 until selected.rows && pos.c in 0 until selected.cols

    internal fun reflectSlash(dir: Dir): Dir = when (dir) {
        Dir.UP -> Dir.RIGHT
        Dir.RIGHT -> Dir.UP
        Dir.DOWN -> Dir.LEFT
        Dir.LEFT -> Dir.DOWN
    }

    internal fun reflectBackslash(dir: Dir): Dir = when (dir) {
        Dir.UP -> Dir.LEFT
        Dir.LEFT -> Dir.UP
        Dir.DOWN -> Dir.RIGHT
        Dir.RIGHT -> Dir.DOWN
    }
}
