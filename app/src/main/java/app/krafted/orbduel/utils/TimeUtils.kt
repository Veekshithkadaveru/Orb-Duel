package app.krafted.orbduel.utils

import java.util.concurrent.TimeUnit

fun formatRelativeTime(timestamp: Long): String {
    val now = System.currentTimeMillis()
    val diff = now - timestamp
    
    val seconds = TimeUnit.MILLISECONDS.toSeconds(diff)
    val minutes = TimeUnit.MILLISECONDS.toMinutes(diff)
    val hours = TimeUnit.MILLISECONDS.toHours(diff)
    val days = TimeUnit.MILLISECONDS.toDays(diff)

    return when {
        diff < 0 -> "just now"
        seconds < 60 -> "just now"
        minutes == 1L -> "1 min ago"
        minutes < 60 -> "$minutes mins ago"
        hours == 1L -> "1 hour ago"
        hours < 24 -> "$hours hours ago"
        days == 1L -> "yesterday"
        days < 30 -> "$days days ago"
        else -> "a long time ago"
    }
}
