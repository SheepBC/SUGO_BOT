package Tools

import dev.kord.common.entity.Snowflake
import dev.kord.core.behavior.channel.createEmbed
import dev.kord.core.behavior.interaction.response.createPublicFollowup
import dev.kord.core.behavior.interaction.response.respond
import dev.kord.core.behavior.reply
import dev.kord.core.entity.Member
import dev.kord.core.entity.Message
import dev.kord.core.entity.interaction.followup.PublicFollowupMessage
import dev.kord.core.entity.interaction.response.EphemeralMessageInteractionResponse
import dev.kord.core.entity.interaction.response.PublicMessageInteractionResponse
import dev.kord.core.event.interaction.ComponentInteractionCreateEvent
import dev.kord.rest.builder.message.EmbedBuilder
import dev.kord.rest.builder.message.create.UserMessageCreateBuilder
import dev.kord.rest.builder.message.modify.InteractionResponseModifyBuilder
import dev.kord.rest.builder.message.modify.UserMessageModifyBuilder

suspend fun ComponentInteractionCreateEvent.responseEphemeral(msg: String): EphemeralMessageInteractionResponse {
    return this.interaction.deferEphemeralResponse().respond {
        content = msg
    }
}

suspend fun ComponentInteractionCreateEvent.responseEphemeral(builder: InteractionResponseModifyBuilder.() -> Unit)
        : EphemeralMessageInteractionResponse {
    return this.interaction.deferEphemeralResponse().respond { builder()}
}

fun ComponentInteractionCreateEvent.getGuildId(): Snowflake? {
    return this.interaction.data.guildId.value
}

suspend fun ComponentInteractionCreateEvent.getMember(): Member {
    return this.interaction.user.asMember(this.getGuildId()!!)
}

fun ComponentInteractionCreateEvent.getCustomId(): String{
    return this.interaction.data.data.customId.value!!
}

suspend fun ComponentInteractionCreateEvent.sendEmbed(block: EmbedBuilder.() -> Unit): Message {
    return this.interaction.channel.createEmbed {block()}
}

suspend fun ComponentInteractionCreateEvent.replay(builder: UserMessageCreateBuilder.() -> Unit): Message {
    return this.interaction.message.reply { builder() }
}
