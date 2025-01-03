package Commands

import Tools.*
import dev.kord.common.entity.ButtonStyle
import dev.kord.core.behavior.channel.connect
import dev.kord.core.event.message.MessageCreateEvent
import dev.kord.rest.builder.message.EmbedBuilder
import dev.kord.rest.builder.message.actionRow
import dev.kord.rest.builder.message.embed
import dev.kord.voice.AudioFrame

class Sing_Command{
    companion object {
        @Command(["노래","play"])
        suspend fun sing(event: MessageCreateEvent, msg_list: List<String>){

            if(event.member!!.getVoiceState().getChannelOrNull() == null){
                event.sendMessage("🎶음성 채널에 들어가 주세요.")
                return;
            }

            if (msg_list.size == 1){
                event.sendMessage("숩노래 [url|검색]")
                return;
            }

            if(MusicManager.list[event.guildId!!.value] == null){
                val channel = event.member?.getVoiceState()?.getChannelOrNull()

                val player = MusicManager.lavaplayerManager.createPlayer()


                val connection = channel!!.connect {
                    audioProvider { AudioFrame.fromData(player.provide()?.data) }
                }
                MusicManager.list.put(
                    key = event.guildId!!.value,
                    value = MusicTask(
                        player = player,
                        guildId = event.guildId!!,
                        voiceConnection = connection
                    )
                )
            }

            val uri = event.message.content.replaceFirst("${msg_list[0]} ","")
            val track = MusicManager.lavaplayerManager.getTrack(uri)

            MusicManager.list[event.guildId!!.value]!!.addTrack(track)

            event.sendEmbed {
                url = track.info.uri
                title = track.info.title

                val imageURI = MusicManager.getImage(track.info.uri)
                thumbnail = EmbedBuilder.Thumbnail(imageURI)

                fields = mutableListOf(
                    EmbedBuilder.run { Field(name = "재생 시간",inline = true, value = track.getTime() ) }
                )

                val footerText = "${MusicManager.list[event.guildId!!.value]!!.tracks.size}번째 곡"
                footer = EmbedBuilder.Footer(text = footerText)

                color = Other_Tool.getRandomColor()
            }

        }

        @Command(["정지","일시정지","pause"])
        suspend fun pause(event: MessageCreateEvent,msg_list: List<String>){

            if(event.member!!.getVoiceState().getChannelOrNull() == null){
                event.sendMessage("🎶음성 채널에 들어가 주세요.")
                return;
            }

            if(MusicManager.list[event.guildId!!.value] == null){
                event.sendMessage("정지할 노래가 없습니다.")
                return
            }

            val task = MusicManager.list[event.guildId!!.value]!!
            task.player.isPaused = !task.player.isPaused

            var isPause = "정지"
            if (!task.player.isPaused) isPause = "재생"

            event.sendMessage("노래가 ${isPause}되었습니다.")

        }

        @Command(["다음","다음곡","next"])
        suspend fun nextTrack(event: MessageCreateEvent,msg_list: List<String>){

            if(event.member!!.getVoiceState().getChannelOrNull() == null){
                event.sendMessage("🎶음성 채널에 들어가 주세요.")
                return;
            }

            if(MusicManager.list[event.guildId!!.value] == null){
                event.sendMessage("넘길 노래가 없습니다.")
                return
            }

            val task = MusicManager.list[event.guildId!!.value]!!
            task.player.stopTrack()

            val track = task.player.playingTrack

            if(track == null){
                event.sendMessage("모든 노래를 들었습니다.")
                return
            }

            event.sendMessage("__**${track.info.title}**__가(이) 재생됩니다.")


        }

        @Command(["끄기","stop"])
        suspend fun stop(event: MessageCreateEvent,msg_list: List<String>){

            if(event.member!!.getVoiceState().getChannelOrNull() == null){
                event.sendMessage("🎶음성 채널에 들어가 주세요.")
                return;
            }

            if(MusicManager.list[event.guildId!!.value] == null){
                event.sendMessage("노래가 없습니다.")
                return
            }

            val task = MusicManager.list[event.guildId!!.value]!!
            task.tracks.removeAll { true }
            task.player.stopTrack()

            event.sendMessage("🎶노래가 꺼젔습니다.")
        }

        @Command(["목록","리스트","list"])
        suspend fun list(event: MessageCreateEvent,msg_list: List<String>){
            if(event.member!!.getVoiceState().getChannelOrNull() == null){
                event.sendMessage("🎶음성 채널에 들어가 주세요.")
                return;
            }

            if(MusicManager.list[event.guildId!!.value] == null){
                event.sendMessage("노래가 없습니다.")
                return
            }

            event.replay {

                val trackList = mutableListOf<EmbedBuilder.Field>()
                val task = MusicManager.list[event.guildId!!.value]!!

                for ( index in 0..task.tracks.size){
                    if(index == 3) break

                    val track = task.tracks[index]
                    trackList.add(
                        EmbedBuilder.Field(
                            name = track.info.title,
                            inline = false,
                            value = track.getTime()
                        )
                    )

                }

                embed {
                    title = "노래 목록"
                    fields = trackList
                    color = Other_Tool.getRandomColor()
                    footer = EmbedBuilder.Footer(text = "총 ${task.tracks.size}곡")
                }

                if(task.tracks.size > 3){
                    actionRow {
                        interactionButton(
                            style = ButtonStyle.Primary,
                            customId = "MusicTrackList",
                            builder = {
                                label = "노래 목록 전체 보기"
                            }
                        )
                    }
                }
            }

        }

        @Command(["반복","repeat"])
        suspend fun repeat(event: MessageCreateEvent,msg_list: List<String>){

            if(event.member!!.getVoiceState().getChannelOrNull() == null){
                event.sendMessage("🎶음성 채널에 들어가 주세요.")
                return;
            }

            if(MusicManager.list[event.guildId!!.value] == null){
                event.sendMessage("노래가 없습니다.")
                return
            }
            val task = MusicManager.list[event.guildId!!.value]!!

            if(msg_list.size == 1){
                event.sendEmbed {
                    title = " 현재 반복은 **${task.repeat.value}**입니다."
                    footer = EmbedBuilder.Footer(text = "숩반복 <한곡(one)|목록(list)|랜덤(random)|끄기(off)>")
                    color = Other_Tool.getRandomColor()
                }
                return
            }

            task.repeat = MusicManager.getRepeat(msg_list[1])

            event.sendMessage("반복이 **${task.repeat.value}**으로 변경되었습니다.")

        }

        @Command(["지금","현재","now"])
        suspend fun now(event: MessageCreateEvent,msg_list: List<String>){

            if(event.member!!.getVoiceState().getChannelOrNull() == null){
                event.sendMessage("🎶음성 채널에 들어가 주세요.")
                return;
            }

            if(MusicManager.list[event.guildId!!.value] == null){
                event.sendMessage("노래가 없습니다.")
                return
            }

            val task = MusicManager.list[event.guildId!!.value]!!
            val track = task.player.playingTrack

            event.replay {
                embed {
                    url = track.info.uri
                    title = track.info.title

                    val imageURI = MusicManager.getImage(track.info.uri)
                    thumbnail = EmbedBuilder.Thumbnail(imageURI)

                    val time = "${track.getNowTime()}/${track.getTime()}"
                    fields = mutableListOf(
                        EmbedBuilder.run { Field(name = "재생 시간",inline = true, value = time ) }
                    )

                    color = Other_Tool.getRandomColor()
                }
            }

        }
    }
}