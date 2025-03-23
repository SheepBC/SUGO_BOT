package Tools

import dev.kord.common.Color
import io.ktor.client.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonElement
import java.util.*
import java.util.concurrent.TimeUnit


object OtherTool {

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

    val client = HttpClient()

    fun getJson(url: String): JsonElement{
        val text = fetchWithKtor(url)
        return Json.parseToJsonElement(text);
    }

    private fun fetchWithKtor(url: String): String = runBlocking {
        client.get(url).bodyAsText()
    }


}