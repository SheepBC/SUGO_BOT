package Interactions

import DataBases.GameHistory
import Tools.*
import dev.kord.core.behavior.edit
import dev.kord.core.event.interaction.ComponentInteractionCreateEvent
import dev.kord.rest.builder.message.EmbedBuilder
import dev.kord.rest.builder.message.embed
import kotlinx.serialization.json.jsonObject

object GameInteraction {

    suspend fun countryQuiz(event: ComponentInteractionCreateEvent){
        val answer = event.interaction.message
            .embeds.first().image!!.data.url.value!!
            .replace("https://flagcdn.com/w160/","")
            .replace(".png","")

        val select = event.getCustomId().split(" ")[1]

        var sendMsg = "틀렸습니다."
        if(select == answer){
            sendMsg = "정답입니다."
        }
        if (GameHistory.getGuildHistoryAccept(event.getGuildId()?.value!!)){
            val userId = event.interaction.user.id.value
            val userGameList = GameHistory.getGameHistoryUser(userId)
            if (select == answer){
                userGameList[0] += 1
            }
            else{
                userGameList[1] += 1
            }
            GameHistory.setGameHistoryUser(userId,userGameList.toList())
            addRegion(answer, select)
        }

        event.replay { content = sendMsg }
        event.interaction.message.edit {
            components = mutableListOf()

            val nameJson = OtherTool.getJson("https://restcountries.com/v3.1/alpha/$answer?fields=translations")
            val tldNameList = nameJson.jsonObject["translations"]!!.jsonObject["kor"]!!
            val official = tldNameList.jsonObject["official"]!!.toString().replace("\"","")
            val common = tldNameList.jsonObject["common"]!!.toString().replace("\"","")

            embed {
                title = official
                description = common
                image = "https://flagcdn.com/w160/$answer.png"
                footer = EmbedBuilder.Footer(text = "CountryQuiz")
            }
        }

    }

    private fun addRegion(answer: String,select: String){
        val nameJson = OtherTool.getJson("https://restcountries.com/v3.1/alpha/$answer?fields=region")
        val region = nameJson.jsonObject["region"]!!.toString().replace("\"","")
        var regionList = GameHistory.getGameHistoryRegion(region)

        if (answer == select){
            regionList[0] += 1
        }
        else{
            regionList[1] += 1
        }

        GameHistory.setGameHistoryRegion(region,regionList)
    }

}