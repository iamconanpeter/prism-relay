package com.iamconanpeter.prismrelay

import android.graphics.Color
import android.os.Bundle
import android.view.Gravity
import android.widget.Button
import android.widget.GridLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    private lateinit var engine: PrismRelayEngine
    private lateinit var progress: RelayProgressManager

    private lateinit var statusText: TextView
    private lateinit var traceText: TextView
    private lateinit var statsText: TextView
    private lateinit var titleText: TextView
    private lateinit var grid: GridLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        progress = RelayProgressManager(this)
        engine = PrismRelayEngine(progress.dailyPuzzleIndex(2))

        titleText = findViewById(R.id.titleText)
        statusText = findViewById(R.id.statusText)
        traceText = findViewById(R.id.traceText)
        statsText = findViewById(R.id.statsText)
        grid = findViewById(R.id.grid)

        findViewById<Button>(R.id.resetBtn).setOnClickListener {
            engine.reset()
            renderGrid()
            renderEval("Puzzle reset")
        }

        findViewById<Button>(R.id.undoBtn).setOnClickListener {
            if (engine.undoLastRotation()) {
                renderGrid()
                renderEval("Undo used")
            } else {
                statusText.setTextColor(Color.parseColor("#FCA5A5"))
                statusText.text = "No undo charges left"
            }
        }

        renderGrid()
        renderEval()
    }

    private fun renderGrid() {
        grid.removeAllViews()
        val (rows, cols) = engine.size()
        titleText.text = "Prism Relay — ${engine.puzzleName()}"

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

    private fun renderEval(prefix: String? = null) {
        val result = engine.evaluate()

        if (result.hitGoal) {
            val stars = result.stars ?: 1
            progress.updateBestStars(engine.puzzleId(), stars)
            val daily = progress.recordDailySolve()
            val streakMsg = if (daily.alreadyCompletedToday) "" else " • Streak ${daily.streak}"
            statusText.setTextColor(Color.parseColor("#34D399"))
            statusText.text = "${prefix?.plus(" • ") ?: ""}Powered! ${"⭐".repeat(stars)}$streakMsg"
        } else {
            statusText.setTextColor(Color.parseColor("#A7F3D0"))
            statusText.text = "${prefix?.plus(" • ") ?: ""}Not solved: ${result.terminatedReason}"
        }

        val bestStars = progress.bestStarsFor(engine.puzzleId())
        statsText.text = "Moves ${engine.movesUsed()}/${engine.parMoves()} • Undo ${engine.remainingUndos()} • Best ${"⭐".repeat(bestStars)}"
        traceText.text = result.path.joinToString(" → ") { "(${it.r},${it.c})" } + " • Eff ${result.efficiency}"
    }

    private fun symbol(cell: CellType): String = when (cell) {
        CellType.EMPTY -> "·"
        CellType.MIRROR_SLASH -> "/"
        CellType.MIRROR_BACKSLASH -> "\\"
        CellType.SOURCE -> "S"
        CellType.GOAL -> "G"
    }
}
