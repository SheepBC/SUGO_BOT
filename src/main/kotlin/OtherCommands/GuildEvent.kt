package OtherCommands

import dev.kord.core.event.guild.GuildCreateEvent
import dev.kord.rest.builder.interaction.boolean
import dev.kord.rest.builder.interaction.integer

object GuildEvent {
    suspend fun run(event: GuildCreateEvent){
        addSlashCommand(event)
    }

    private suspend fun addSlashCommand(event: GuildCreateEvent){
        event.kord.createGuildChatInputCommand(
            event.guild.id,
            "게임기록",
            "게임기록 활성화 및 비활성화에 대한 명령어"
        ) {
            boolean("활성화","게임기록 활성화 및 비활성화"){}

        }
    }
}