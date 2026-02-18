package com.iamconanpeter.prismrelay

import android.os.Bundle
import android.view.Gravity
import android.widget.Button
import android.widget.GridLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    private val engine = PrismRelayEngine()
    private lateinit var statusText: TextView
    private lateinit var traceText: TextView
    private lateinit var grid: GridLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        statusText = findViewById(R.id.statusText)
        traceText = findViewById(R.id.traceText)
        grid = findViewById(R.id.grid)

        findViewById<Button>(R.id.resetBtn).setOnClickListener {
            engine.reset()
            renderGrid()
            renderEval()
        }

        renderGrid()
        renderEval()
    }

    private fun renderGrid() {
        grid.removeAllViews()
        val (rows, cols) = engine.size()

        for (r in 0 until rows) {
            for (c in 0 until cols) {
                val p = Pos(r, c)
                val b = Button(this)
                b.text = symbol(engine.getCell(p))
                b.textSize = 22f
                b.gravity = Gravity.CENTER
                val params = GridLayout.LayoutParams().apply {
                    width = 0
                    height = 0
                    columnSpec = GridLayout.spec(c, 1f)
                    rowSpec = GridLayout.spec(r, 1f)
                    setMargins(4, 4, 4, 4)
                }
                b.layoutParams = params

                b.setOnClickListener {
                    if (engine.rotateMirror(p)) {
                        renderGrid()
                        renderEval()
                    }
                }

                grid.addView(b)
            }
        }
    }

    private fun renderEval() {
        val result = engine.evaluate()
        statusText.text = if (result.hitGoal) {
            "Powered! ⚡ Goal reached"
        } else {
            "Not solved: ${result.terminatedReason}"
        }

        traceText.text = result.path.joinToString(" → ") { "(${it.r},${it.c})" }
    }

    private fun symbol(cell: CellType): String = when (cell) {
        CellType.EMPTY -> "·"
        CellType.MIRROR_SLASH -> "/"
        CellType.MIRROR_BACKSLASH -> "\\"
        CellType.SOURCE -> "S"
        CellType.GOAL -> "G"
    }
}
