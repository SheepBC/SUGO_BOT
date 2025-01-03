package Tools

import dev.kord.common.entity.Snowflake
import dev.kord.core.behavior.channel.createEmbed
import dev.kord.core.behavior.interaction.response.respond
import dev.kord.core.behavior.reply
import dev.kord.core.entity.Member
import dev.kord.core.entity.Message
import dev.kord.core.entity.interaction.response.EphemeralMessageInteractionResponse
import dev.kord.core.event.interaction.ComponentInteractionCreateEvent
import dev.kord.core.event.message.MessageCreateEvent
import dev.kord.rest.builder.message.EmbedBuilder
import dev.kord.rest.builder.message.create.UserMessageCreateBuilder
import dev.kord.rest.builder.message.modify.InteractionResponseModifyBuilder

class Message_Tool {

    fun prefix_check(prefixes: List<String>,message: String): Boolean{

        val prefix= message[0].toString()
        return prefixes.contains<String>(prefix)
    }

}

suspend fun MessageCreateEvent.sendMessage(msg: String): Message{
    return this.message.channel.createMessage(msg)
}

suspend fun MessageCreateEvent.sendEmbed(block: EmbedBuilder.() -> Unit): Message {
    return this.message.channel.createEmbed { block() }
}

suspend fun MessageCreateEvent.replay(block: UserMessageCreateBuilder.() -> Unit): Message {
    return this.message.reply { block() }
}

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

fun EmbedBuilder.Companion.Thumbnail(uri: String): EmbedBuilder.Thumbnail {
    val thumbnail = EmbedBuilder.Thumbnail()
    thumbnail.url = uri
    return thumbnail
}

fun EmbedBuilder.Companion.Field(name: String,inline: Boolean = false,value: String):EmbedBuilder.Field{
    val field = EmbedBuilder.Field()
    field.name = name
    field.inline = inline
    field.value = value

    return  field
}

fun  EmbedBuilder.Companion.Footer(text: String? = null,icon: String? = null): EmbedBuilder.Footer {
    val footer = EmbedBuilder.Footer()
    if (text != null) footer.text = text
    if (icon != null)footer.icon = icon

    return footer
}
