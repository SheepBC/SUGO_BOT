package Tools

import Tools.MusicManager.getQuery
import com.sedmelluq.discord.lavaplayer.player.AudioLoadResultHandler
import com.sedmelluq.discord.lavaplayer.player.AudioPlayer
import com.sedmelluq.discord.lavaplayer.player.DefaultAudioPlayerManager
import dev.kord.common.entity.Snowflake
import com.sedmelluq.discord.lavaplayer.player.event.AudioEventAdapter
import com.sedmelluq.discord.lavaplayer.source.AudioSourceManagers
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist
import com.sedmelluq.discord.lavaplayer.track.AudioTrack
import com.sedmelluq.discord.lavaplayer.track.AudioTrackEndReason
import dev.kord.voice.VoiceConnection
import dev.lavalink.youtube.YoutubeAudioSourceManager
import kotlinx.coroutines.runBlocking
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine
import kotlin.random.Random


object MusicManager{
    var list = mutableMapOf<ULong,MusicTask>()

    val lavaplayerManager = DefaultAudioPlayerManager()
    val youtubeSourceManager = YoutubeAudioSourceManager(true)

    init {
        lavaplayerManager.registerSourceManagers(youtubeSourceManager)
        AudioSourceManagers.registerRemoteSources(lavaplayerManager)
    }

    fun getQuery(data: String): String {

        if(data.contains("youtube.com") || data.contains("youtu.be")){
            return data
        }

        return "ytsearch: ${data}"
    }

    fun getImage(url: String): String{

        val videoID =  url.split("/").last().split("?si=").first().replace("watch?v=","")

        return "https://img.youtube.com/vi/$videoID/default.jpg"
    }

    fun getRepeat(repeat: String):MusicRepeat{
        when (repeat.lowercase()){
            "한곡" ,"one" -> return MusicRepeat.THIS_TRACK
            "목록" ,"list" -> return MusicRepeat.TRACKS
            "랜덤" ,"random" -> return MusicRepeat.RANDOM
            else -> return MusicRepeat.NOT
        }
    }

}

suspend fun DefaultAudioPlayerManager.getTrack(query: String): AudioTrack {
    val youtube_query = getQuery(query)
    val track = suspendCoroutine<AudioTrack> {
        this.loadItem(youtube_query, object : AudioLoadResultHandler {
            override fun trackLoaded(track: AudioTrack) {
                it.resume(track)
            }

            override fun playlistLoaded(playlist: AudioPlaylist) {
                it.resume(playlist.tracks.random())
            }

            override fun noMatches() {
                //println("NO")
            }

            override fun loadFailed(exception: FriendlyException?) {
                //println("fail")
            }
        })
    }

    return track
}
fun AudioTrack.getTime(): String{
    return OtherTool.secondsToHHMMSS(this.duration)
}

fun AudioTrack.getNowTime(): String{
    return OtherTool.secondsToHHMMSS(this.position)
}


class MusicTask(
    var player: AudioPlayer,
    var guildId: Snowflake,
    var voiceConnection: VoiceConnection) :
    AudioEventAdapter() {

    var tracks = mutableListOf<AudioTrack>()
    var track_index = 0;
    var repeat = MusicRepeat.NOT

    init {
        player.addListener(this)
    }

    fun addTrack(track: AudioTrack){
        tracks.add(track)

        if (tracks.size == 1){
            player.playTrack(tracks[0])
        }
    }

    fun onOff(){
        runBlocking {
            voiceConnection.leave()
        }
        MusicManager.list.remove(guildId.value)
    }

    override fun onTrackEnd(player: AudioPlayer?, track: AudioTrack?, endReason: AudioTrackEndReason?) {
        when(repeat){
            MusicRepeat.NOT ->{
                if(tracks.isNotEmpty()){
                    tracks.removeAt(track_index)
                    track_index = 0
                    player?.playTrack(tracks[0].makeClone())
                }
                else {
                    onOff()
                }

            }
            MusicRepeat.THIS_TRACK ->{

                if(tracks.isNotEmpty()){
                    player?.playTrack(tracks[track_index].makeClone())
                }
                else {
                    onOff()
                }

            }
            MusicRepeat.TRACKS ->{
                if(tracks.isEmpty()) MusicManager.list.remove(guildId.value)
                track_index = (track_index+1) % (tracks.size)
                player?.playTrack(tracks[track_index].makeClone())

            }
            MusicRepeat.RANDOM ->{
                if(tracks.isEmpty()) MusicManager.list.remove(guildId.value)
                track_index = Random.nextInt(0,tracks.size)
                player?.playTrack(tracks[track_index].makeClone())
            }
        }

        super.onTrackEnd(player, track, endReason)
    }



}

enum class MusicRepeat(val value: String) {
    NOT( "끄기"),
    THIS_TRACK("현재 곡"),
    TRACKS("목록"),
    RANDOM("랜덤");
}