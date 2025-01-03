package Interactions


import Tools.*
import dev.kord.core.behavior.interaction.response.respond
import dev.kord.core.event.interaction.ComponentInteractionCreateEvent
import dev.kord.core.event.interaction.InteractionCreateEvent
import dev.kord.rest.builder.message.EmbedBuilder
import dev.kord.rest.builder.message.embed

object SingInteraction {

    suspend fun list(event: ComponentInteractionCreateEvent){

        val guildId = event.getGuildId()
        val member = event.getMember()
        println(guildId)
        if(member.getVoiceState().getChannelOrNull() == null){
            event.interaction.deferEphemeralResponse().respond {
                content = "🎶음성 채널에 들어가 주세요."
            }
            return;
        }

        if(MusicManager.list[guildId!!.value] == null){
            event.responseEphemeral("노래가 없습니다.")
            return
        }

        event.responseEphemeral{

            val trackList = mutableListOf<EmbedBuilder.Field>()
            val task = MusicManager.list[guildId.value]!!

            task.tracks.forEach { track ->
                trackList.add(
                    EmbedBuilder.Field(
                    name = track.info.title,
                    inline = false,
                    value = track.getTime()
                ))
            }

            embed {
                title = "노래 목록"
                fields = trackList
                color = Other_Tool.getRandomColor()
                footer = EmbedBuilder.Footer(text = "총 ${trackList.size}곡")
            }

        }

    }

}