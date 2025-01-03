package Interactions

import dev.kord.core.event.interaction.ComponentInteractionCreateEvent
import dev.kord.core.event.interaction.InteractionCreateEvent

object mainInteraction {

    suspend fun run(event: ComponentInteractionCreateEvent){
        val customId = event.interaction.data.data.customId.value
        if(customId == "MusicTrackList") SingInteraction.list(event)
    }

}