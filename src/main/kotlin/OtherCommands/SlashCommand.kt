package OtherCommands

import DataBases.GameHistory
import Tools.getValue
import dev.kord.core.behavior.interaction.respondEphemeral
import dev.kord.core.cache.data.OptionData
import dev.kord.core.event.interaction.GuildApplicationCommandInteractionCreateEvent

object SlashCommand {
    suspend fun run(event: GuildApplicationCommandInteractionCreateEvent){
        val command = event.interaction.data.data.name.value
        val options = event.interaction.data.data.options.value
        if (command == "게임기록") gameHistory(event, options)
    }

    private suspend fun gameHistory(event: GuildApplicationCommandInteractionCreateEvent, options: List<OptionData>?) {
        val user = event.interaction.user
        if (!user.asMember().isOwner()) return
        val guild = event.interaction.guild.asGuild().id.value

        if (options == null){
            event.interaction.respondEphemeral {
                val isAccept = GameHistory.getGuildHistoryAccept(guild)
                val status = if (isAccept) "활성화" else "비활성화"
                content = "현재 게임기록은 **${status}** 입니다."
            }
            return
        }

        val option = options[0].getValue() as Boolean
        GameHistory.setGuildHistoryAccept(guild,option)

        event.interaction.respondEphemeral {
            val status = if (option) "활성화" else "비활성화"
            content = "현재 게임기록은 **${status}** 으로 바뀌었습니다."
        }


    }
}