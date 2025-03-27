package Main

import Configs.GameConfig
import DataBases.GameHistory
import dev.kord.common.entity.Snowflake
import dev.kord.core.Kord
import dev.kord.core.behavior.channel.MessageChannelBehavior
import dev.kord.core.behavior.channel.createMessage
import dev.kord.gateway.builder.LoginBuilder
import dev.kord.rest.builder.message.addFile
import kotlinx.coroutines.*
import java.io.File
import java.time.Duration
import java.time.LocalDate
import java.time.LocalDateTime
import java.util.*
import kotlin.concurrent.timerTask
import kotlin.io.path.Path

val BACKUP_ID = Snowflake(731103530051436637)

object MainSetup {

    @OptIn(DelicateCoroutinesApi::class)
    fun run(block: LoginBuilder,kord: Kord){
        val isGameReset = true

        GlobalScope.launch {
            val game = launch {
                while (isGameReset){

                    val now = LocalDate.now()
                    val next = LocalDate.of(now.year,now.month,1).plusMonths(1)
                    val duration = Duration.ofDays(now.datesUntil(next).count())
                    delay(duration.toMillis())

                    val backup = GameHistory.getRegionBarsFile()
                    val messageChannel = MessageChannelBehavior(BACKUP_ID,kord)
                    messageChannel.createMessage {
                        addFile(Path(backup))
                    }

                    val userFile = File(GameConfig.GAME_HISTORY_USER_DB)
                    userFile.delete()

                    val regionFile = File(GameConfig.GAME_HISTORY_REGION_DB)
                    regionFile.delete()
                }
            }
        }

        println("봇 시작 됨")
    }
}