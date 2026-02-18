package com.iamconanpeter.prismrelay

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
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
        val e = PrismRelayEngine()
        val result = e.evaluate()
        assertFalse(result.hitGoal)
    }

    @Test
    fun `specific rotations solve the puzzle`() {
        val e = PrismRelayEngine()
        // rotate twice to turn blocking mirrors into empty cells
        assertTrue(e.rotateMirror(Pos(2, 1)))
        assertTrue(e.rotateMirror(Pos(2, 1)))
        assertTrue(e.rotateMirror(Pos(2, 3)))

        val result = e.evaluate()
        assertTrue(result.hitGoal)
    }
}
