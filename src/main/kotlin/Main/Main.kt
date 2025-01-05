package Bc.Sheep_BC.Main

import Commands.Main_Command
import Interactions.MainInteraction
import Configs.BotConfig
import Tools.Message_Tool
import dev.kord.core.Kord
import dev.kord.core.event.interaction.ComponentInteractionCreateEvent
import dev.kord.core.event.message.MessageCreateEvent
import dev.kord.core.on
import dev.kord.gateway.Intent
import dev.kord.gateway.PrivilegedIntent

suspend fun main() {

    val bot_toml = BotConfig().Bot_Toml

    val kord = Kord(bot_toml.getString(BotConfig.BOT_TOKEN))
    val prefixes = bot_toml.getList<String>(BotConfig.BOT_PREFIXES)

    val mainCommand = Main_Command()

    kord.on<MessageCreateEvent> {

        if (member!!.isBot) return@on
        val msg = message.content
        if(!Message_Tool().prefix_check(prefixes,msg)) return@on

        mainCommand.run(this)

    }

    kord.on<ComponentInteractionCreateEvent> {
        MainInteraction.run(this)
    }

    kord.login {
        presence {
            playing(bot_toml.getString(BotConfig.BOT_PLAYING))
        }

        @OptIn(PrivilegedIntent::class)
        intents += Intent.MessageContent

    }

}