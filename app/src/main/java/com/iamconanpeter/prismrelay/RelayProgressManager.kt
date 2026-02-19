package com.iamconanpeter.prismrelay

import android.content.Context
import java.util.Calendar

class RelayProgressManager(context: Context) {
    private val prefs = context.getSharedPreferences("prism_relay", Context.MODE_PRIVATE)

    data class DailyResult(
        val streak: Int,
        val alreadyCompletedToday: Boolean
    )

    fun dailyPuzzleIndex(total: Int): Int {
        val day = Calendar.getInstance().get(Calendar.DAY_OF_YEAR)
        return day % total
    }

    fun bestStarsFor(puzzleId: String): Int = prefs.getInt("best_stars_$puzzleId", 0)

    fun updateBestStars(puzzleId: String, stars: Int) {
        val current = bestStarsFor(puzzleId)
        if (stars > current) {
            prefs.edit().putInt("best_stars_$puzzleId", stars).apply()
        }
    }

    fun currentStreak(): Int = prefs.getInt("daily_streak", 0)

    fun recordDailySolve(): DailyResult {
        val today = dailyKey()
        val last = prefs.getString("last_day", null)
        if (today == last) {
            return DailyResult(currentStreak(), alreadyCompletedToday = true)
        }

        val continues = last != null && isConsecutive(last, today)
        val nextStreak = if (continues) currentStreak() + 1 else 1

        prefs.edit()
            .putString("last_day", today)
            .putInt("daily_streak", nextStreak)
            .apply()

        return DailyResult(nextStreak, alreadyCompletedToday = false)
    }

    private fun dailyKey(): String {
        val cal = Calendar.getInstance()
        return "${cal.get(Calendar.YEAR)}-${cal.get(Calendar.DAY_OF_YEAR)}"
    }

    private fun isConsecutive(last: String, today: String): Boolean {
        val l = last.split("-")
        val t = today.split("-")
        if (l.size != 2 || t.size != 2) return false
        val ly = l[0].toIntOrNull() ?: return false
        val ld = l[1].toIntOrNull() ?: return false
        val ty = t[0].toIntOrNull() ?: return false
        val td = t[1].toIntOrNull() ?: return false

        return (ly == ty && ld + 1 == td) || (ly + 1 == ty && ld >= 365 && td == 1)
    }
}
