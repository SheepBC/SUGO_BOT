package Interactions


import Configs.MusicConfig
import Tools.*
import dev.kord.common.entity.ButtonStyle
import dev.kord.core.behavior.interaction.response.edit
import dev.kord.core.behavior.interaction.response.respond
import dev.kord.core.behavior.interaction.updatePublicMessage
import dev.kord.core.event.interaction.ComponentInteractionCreateEvent
import dev.kord.rest.builder.message.EmbedBuilder
import dev.kord.rest.builder.message.actionRow
import dev.kord.rest.builder.message.embed

object MusicInteraction {

    suspend fun list(event: ComponentInteractionCreateEvent){

        val guildId = event.getGuildId()
        val member = event.getMember()

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
                color = OtherTool.getRandomColor()
                footer = EmbedBuilder.Footer(text = "총 ${trackList.size}곡")
            }

        }

    }

    suspend fun repeat(event: ComponentInteractionCreateEvent) {
        val guildId = event.getGuildId()
        val member = event.getMember()

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

        val task = MusicManager.list[guildId.value]!!

        when (event.getCustomId()){
            MusicConfig.REPEAT_NOT -> task.repeat = MusicRepeat.NOT
            MusicConfig.REPEAT_ONE -> task.repeat = MusicRepeat.THIS_TRACK
            MusicConfig.TRACK_LIST -> task.repeat = MusicRepeat.TRACKS
            MusicConfig.REPEAT_RANDOM -> task.repeat = MusicRepeat.RANDOM
        }
        event.replay {
            embed {
                title = "반복이 __${task.repeat.value}__으로 변경되었습니다."
                color = OtherTool.getRandomColor()
            }
        }

    }

}