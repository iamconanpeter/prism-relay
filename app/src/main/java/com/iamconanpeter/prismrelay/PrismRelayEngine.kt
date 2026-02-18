package com.iamconanpeter.prismrelay

enum class CellType { EMPTY, MIRROR_SLASH, MIRROR_BACKSLASH, SOURCE, GOAL }
enum class Dir { UP, RIGHT, DOWN, LEFT }

data class Pos(val r: Int, val c: Int)

data class BeamResult(
    val hitGoal: Boolean,
    val path: List<Pos>,
    val terminatedReason: String
)

class PrismRelayEngine {
    private val rows = 5
    private val cols = 5

    private val source = Pos(2, 0)
    private val goal = Pos(2, 4)

    private val initialMirrors = mapOf(
        Pos(2, 1) to CellType.MIRROR_SLASH,
        Pos(1, 1) to CellType.MIRROR_BACKSLASH,
        Pos(1, 3) to CellType.MIRROR_SLASH,
        Pos(2, 3) to CellType.MIRROR_BACKSLASH,
        Pos(3, 3) to CellType.MIRROR_SLASH
    )

    private val mirrors = initialMirrors.toMutableMap()

    fun size(): Pair<Int, Int> = rows to cols

    fun getCell(pos: Pos): CellType = when {
        pos == source -> CellType.SOURCE
        pos == goal -> CellType.GOAL
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
        if (next == CellType.EMPTY) {
            mirrors.remove(pos)
        } else {
            mirrors[pos] = next
        }
        return true
    }

    fun reset() {
        mirrors.clear()
        mirrors.putAll(initialMirrors)
    }

    fun evaluate(): BeamResult {
        var pos = source
        var dir = Dir.RIGHT
        val path = mutableListOf(pos)
        val visitedState = mutableSetOf<Pair<Pos, Dir>>()

        while (true) {
            val state = pos to dir
            if (!visitedState.add(state)) {
                return BeamResult(false, path, "Loop detected")
            }

            val next = step(pos, dir)
            if (!inBounds(next)) {
                return BeamResult(false, path, "Beam exited grid")
            }

            pos = next
            path += pos

            if (pos == goal) {
                return BeamResult(true, path, "Goal reached")
            }

            when (getCell(pos)) {
                CellType.MIRROR_SLASH -> dir = reflectSlash(dir)
                CellType.MIRROR_BACKSLASH -> dir = reflectBackslash(dir)
                else -> Unit
            }
        }
    }

    private fun step(pos: Pos, dir: Dir): Pos = when (dir) {
        Dir.UP -> Pos(pos.r - 1, pos.c)
        Dir.RIGHT -> Pos(pos.r, pos.c + 1)
        Dir.DOWN -> Pos(pos.r + 1, pos.c)
        Dir.LEFT -> Pos(pos.r, pos.c - 1)
    }

    private fun inBounds(pos: Pos): Boolean = pos.r in 0 until rows && pos.c in 0 until cols

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
