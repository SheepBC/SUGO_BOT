package Bc.Sheep_BC.Main

import Bc.Sheep_BC.Tools.getToml
import Bc.Sheep_BC.Tools.setString
import Commands.Main_Command
import Main.Bot_Config
import Tools.Message_Tool
import com.moandjiezana.toml.Toml
import com.moandjiezana.toml.TomlWriter
import dev.kord.core.Kord
import dev.kord.core.behavior.channel.createMessage
import dev.kord.core.event.message.MessageCreateEvent
import dev.kord.core.on
import dev.kord.gateway.Intent
import dev.kord.gateway.PrivilegedIntent
import java.io.File

suspend fun main() {

    val bot_toml = Bot_Config().Bot_Toml

    val kord = Kord(bot_toml.getString(Bot_Config.BOT_TOKEN))
    val prefixes = bot_toml.getList<String>(Bot_Config.BOT_PREFIXES)

    val mainCommand = Main_Command()

    kord.on<MessageCreateEvent> {

        if (member!!.isBot) return@on
        val msg = message.content
        if(!Message_Tool().prefix_check(prefixes,msg)) return@on

        mainCommand.run(this)

    }

    kord.login {
        presence {
            playing(bot_toml.getString(Bot_Config.BOT_PLAYING))
        }

        @OptIn(PrivilegedIntent::class)
        intents += Intent.MessageContent

    }

}