package Interactions

import Configs.GameConfig
import Configs.MusicConfig
import Tools.getCustomId
import dev.kord.core.behavior.channel.createEmbed
import dev.kord.core.event.interaction.ComponentInteractionCreateEvent

object MainInteraction {

    suspend fun run(event: ComponentInteractionCreateEvent){
        val customId = event.getCustomId()

        if(customId == MusicConfig.TRACK_LIST) MusicInteraction.list(event)
        if(customId.startsWith(MusicConfig.REPEAT)) MusicInteraction.repeat(event)
        if(customId.startsWith(GameConfig.COUNTRY_QUIZ)) GameInteraction.countryQuiz(event)

    }

}