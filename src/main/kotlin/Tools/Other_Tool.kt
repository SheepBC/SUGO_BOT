package Tools

import dev.kord.common.Color
import java.util.*
import java.util.concurrent.TimeUnit


object Other_Tool {

    fun secondsToHHMMSS(millseconds: Long): String {
        val hours = TimeUnit.MILLISECONDS.toHours(millseconds)
        val minutes = TimeUnit.MILLISECONDS.toMinutes(millseconds) % 60
        val secs = TimeUnit.MILLISECONDS.toSeconds(millseconds) % 60
        return String.format("%02d:%02d:%02d", hours, minutes, secs)
    }

    fun getRandomColor(): Color{
        val random = Random()
        return Color(
            red = random.nextInt(0,256),
            green = random.nextInt(0,256),
            blue = random.nextInt(0,256),
        )
    }

}