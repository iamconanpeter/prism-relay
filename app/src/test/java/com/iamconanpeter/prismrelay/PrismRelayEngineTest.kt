package com.iamconanpeter.prismrelay

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test

class PrismRelayEngineTest {

    @Test
    fun `reflection mappings are correct`() {
        val e = PrismRelayEngine()
        assertEquals(Dir.UP, e.reflectSlash(Dir.RIGHT))
        assertEquals(Dir.DOWN, e.reflectSlash(Dir.LEFT))
        assertEquals(Dir.DOWN, e.reflectBackslash(Dir.RIGHT))
        assertEquals(Dir.LEFT, e.reflectBackslash(Dir.UP))
    }

    @Test
    fun `initial board is not solved`() {
        val e = PrismRelayEngine(0)
        val result = e.evaluate()
        assertFalse(result.hitGoal)
        assertEquals(null, result.stars)
    }

    @Test
    fun `specific rotations solve puzzle and award stars`() {
        val e = PrismRelayEngine(0)
        assertTrue(e.rotateMirror(Pos(2, 1)))
        assertTrue(e.rotateMirror(Pos(2, 1)))
        assertTrue(e.rotateMirror(Pos(2, 3)))

        val result = e.evaluate()
        assertTrue(result.hitGoal)
        assertNotNull(result.stars)
        assertTrue((result.stars ?: 0) >= 1)
    }

    @Test
    fun `undo reverts rotation and decreases undo charge`() {
        val e = PrismRelayEngine(0)
        val before = e.getCell(Pos(2, 1))
        assertTrue(e.rotateMirror(Pos(2, 1)))

        assertTrue(e.undoLastRotation())
        val after = e.getCell(Pos(2, 1))

        assertEquals(before, after)
        assertEquals(0, e.remainingUndos())
        assertFalse(e.undoLastRotation())
    }
}
